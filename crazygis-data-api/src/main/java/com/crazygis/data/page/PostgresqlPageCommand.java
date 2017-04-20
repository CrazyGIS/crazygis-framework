package com.crazygis.data.page;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuguolin on 2017/4/18.
 */
public class PostgresqlPageCommand extends PageCommand {
    public long pageCount(long rowCount, long pageSize) {
        return (rowCount + pageSize - 1) / pageSize;
    }

    public long rowStart(long pageIndex, long pageSize) {
        long rowEnd = pageIndex * pageSize;
        return (rowEnd + 1) - pageSize;
    }

    public long rowEnd(long pageIndex, long pageSize)
    {
        return pageIndex * pageSize;
    }

    public String firstPageStatement(String statement, long pageIndex, long pageSize) {
        if(StringUtils.isEmpty(statement)) {
            return statement;
        }
        long rowEnd = rowEnd(pageIndex, pageSize);
        StringBuilder builder = new StringBuilder("SELECT * FROM (")
                .append(statement)
                .append(") PAGE_RESULT_SET LIMIT ")
                .append(pageSize);
        return builder.toString();
    }

    public String pageStatement(String statement, long pageIndex, long pageSize) {
        if(StringUtils.isEmpty(statement)) {
            return statement;
        }
        Matcher m = Pattern.compile("\\bSELECT\\b\\s", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE).matcher(statement);
        int start = -1;
        if(m.find()) {
            start = m.start() + 7;
        }
        m = Pattern.compile("\\s\\bFROM\\b\\s", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE).matcher(statement);
        int end = -1;
        if(m.find()) {
            end = m.start();
        }
        String selectColumns = statement.substring(start, end).trim();

        long rowStart = rowStart(pageIndex, pageSize);
        long rowEnd = rowEnd(pageIndex, pageSize);

        StringBuilder builder = new StringBuilder("SELECT ")
                .append("*")
                .append(" FROM (")
                .append("SELECT PAGE_TEMP.* FROM (")
                .append(statement)
                .append(") PAGE_TEMP) PAGE_RESULT_SET")
                .append(" LIMIT ")
                .append(pageSize)
                .append(" OFFSET ")
                .append(rowStart);
        return builder.toString();
    }

    protected String getSelectColumns(String selectColumns)
    {
        selectColumns = selectColumns.trim();
        String all = "*";
        if (StringUtils.isEmpty(selectColumns) || StringUtils.equals(selectColumns, all))
        {
            return all;
        }

        Pattern patternAs = Pattern.compile("\\s\\bAS\\b\\s", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
        Pattern patternEq = Pattern.compile("\\=", Pattern.MULTILINE);
        Pattern patternPoint = Pattern.compile("\\.", Pattern.MULTILINE);
        Matcher m = null;

        String[] columnItems = selectColumns.split(",");
        StringBuilder columns = new StringBuilder();
        int index = -1;
        String temp = null;
        for (int i = 0; i < columnItems.length; i++)
        {
            temp = columnItems[i];
            m = patternAs.matcher(temp);

            if (m.find()) {
                index = m.end();
                temp = temp.substring(index);
            } else {
                m = patternEq.matcher(temp);
                if(m.find()) {
                    index = m.start();
                    temp = temp.substring(index + 1);
                } else {
                    m = patternPoint.matcher(temp);
                    if(m.find()) {
                        index = m.start();
                        temp = temp.substring(index + 1);
                    }
                }
            }
            temp = temp.trim();
            if (temp == all)
                return all;
            columns.append(temp);
            if (i < columnItems.length - 1)
                columns.append(",");
        }
        return columns.toString();
    }
}
