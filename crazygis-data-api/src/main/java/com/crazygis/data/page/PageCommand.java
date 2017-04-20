package com.crazygis.data.page;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by soon on 2015/6/17.
 */
public class PageCommand {

    public String rowCountStatement(String statement) {
        if(StringUtils.isEmpty(statement)) {
            return statement;
        }
        Matcher m = Pattern.compile("ORDER BY", Pattern.CASE_INSENSITIVE).matcher(statement);
        int index = -1;
        if (m.find())
        {   index = m.start();
            statement = statement.substring(0, index - 1);
        }
        statement = "SELECT COUNT(1) FROM (" + statement + ") COUNT_TEMP_TABLE";
        return statement;
    }
}
