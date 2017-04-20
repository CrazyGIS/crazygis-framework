package com.crazygis.data.springjdbc;

import com.crazygis.common.exceptions.ArgumentNullException;
import com.crazygis.data.IBasicDao;
import com.crazygis.data.page.PageResult;
import com.crazygis.data.page.PostgresqlPageCommand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by xuguolin on 2017/4/18.
 */
public class PostgresqlBasicDao implements IBasicDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PostgresqlPageCommand pageCommand;

    public PostgresqlBasicDao() {
        pageCommand = new PostgresqlPageCommand();
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> query(String statement) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.queryForList(statement);
    }

    @Override
    public List<Map<String, Object>> query(String statement, Object[] parameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.queryForList(statement, parameters);
    }

    @Override
    public List<Map<String, Object>> query(String statement, Map<String, ?> mapParameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.namedParameterJdbcTemplate.queryForList(statement, mapParameters);
    }

    @Override
    public PageResult<Map<String, Object>> queryPage(String statement, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<Map<String, Object>> result = query(statement);
        return new PageResult<Map<String, Object>>(total, result);
    }

    @Override
    public PageResult<Map<String, Object>> queryPage(String statement, Object[] parameters, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement, parameters));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<Map<String, Object>> result = query(statement, parameters);
        return new PageResult<Map<String, Object>>(total, result);
    }

    @Override
    public PageResult<Map<String, Object>> queryPage(String statement, Map<String, ?> mapParameters, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement, mapParameters));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<Map<String, Object>> result = query(statement, mapParameters);
        return new PageResult<Map<String, Object>>(total, result);
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String statement) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.query(statement, new BeanPropertyRowMapper<T>(clazz));
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String statement, Object[] parameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.query(statement, parameters, new BeanPropertyRowMapper<T>(clazz));
    }

    @Override
    public <T> List<T> find(Class<T> clazz, String statement, Map<String, ?> mapParameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.namedParameterJdbcTemplate.query(statement, new BeanPropertyRowMapper<T>(clazz));
    }

    @Override
    public <T> PageResult<T> findPage(Class<T> clazz, String statement, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<T> result = find(clazz, statement);
        return new PageResult<T>(total, result);
    }

    @Override
    public <T> PageResult<T> findPage(Class<T> clazz, String statement, Object[] parameters, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement, parameters));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<T> result = find(clazz, statement, parameters);
        return new PageResult<T>(total, result);
    }

    @Override
    public <T> PageResult<T> findPage(Class<T> clazz, String statement, Map<String, ?> mapParameters, int pageIndex, int pageSize) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }

        String rowCountStatement = pageCommand.rowCountStatement(statement);
        long total = getLong(scalar(rowCountStatement, mapParameters));

        statement = pagingStatement(statement, pageIndex, pageSize);
        List<T> result = find(clazz, statement, mapParameters);
        return new PageResult<T>(total, result);
    }

    @Override
    public Map<String, Object> get(String statement) {
        List<Map<String, Object>> data = query(statement);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public Map<String, Object> get(String statement, Object[] parameters) {
        List<Map<String, Object>> data = query(statement, parameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public Map<String, Object> get(String statement, Map<String, ?> mapParameters) {
        List<Map<String, Object>> data = query(statement, mapParameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public <T> T get(Class<T> clazz, String statement) {
        List<T> data = find(clazz, statement);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public <T> T get(Class<T> clazz, String statement, Object[] parameters) {
        List<T> data = find(clazz, statement, parameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public <T> T get(Class<T> clazz, String statement, Map<String, ?> mapParameters) {
        List<T> data = find(clazz, statement, mapParameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        return data.get(0);
    }

    @Override
    public Object scalar(String statement) {
        List<Map<String, Object>> data = query(statement);
        if(data == null || data.size() == 0) {
            return null;
        }
        Map<String, Object> obj = data.get(0);
        if(obj == null || obj.size() == 0) {
            return null;
        }
        Object result = null;
        for(String key : obj.keySet()) {
            result = obj.get(key);
            break;
        }
        return result;
    }

    @Override
    public Object scalar(String statement, Object[] parameters) {
        List<Map<String, Object>> data = query(statement, parameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        Map<String, Object> obj = data.get(0);
        if(obj == null || obj.size() == 0) {
            return null;
        }
        Object result = null;
        for(String key : obj.keySet()) {
            result = obj.get(key);
            break;
        }
        return result;
    }

    @Override
    public Object scalar(String statement, Map<String, ?> mapParameters) {
        List<Map<String, Object>> data = query(statement, mapParameters);
        if(data == null || data.size() == 0) {
            return null;
        }
        Map<String, Object> obj = data.get(0);
        if(obj == null || obj.size() == 0) {
            return null;
        }
        Object result = null;
        for(String key : obj.keySet()) {
            result = obj.get(key);
            break;
        }
        return result;
    }

    @Override
    public <T> T scalar(Class<T> clazz, String statement, Object[] parameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        T result = jdbcTemplate.queryForObject(statement, parameters, clazz);
        return result;
    }

    @Override
    public <T> T scalar(Class<T> clazz, String statement, Map<String, ?> mapParameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        T result = namedParameterJdbcTemplate.queryForObject(statement, mapParameters, clazz);
        return result;
    }

    @Override
    public int update(String statement) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.update(statement);
    }

    @Override
    public int update(String statement, Object[] parameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.jdbcTemplate.update(statement, parameters);
    }

    @Override
    public int update(String statement, Map<String, ?> mapParameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        return this.namedParameterJdbcTemplate.update(statement, mapParameters);
    }

    @Override
    public int batchUpdate(String... statements) {
        if(statements == null || statements.length == 0) {
            return 0;
        }
        int[] result = this.jdbcTemplate.batchUpdate(statements);
        int affectRows = 0;
        for(int i = 0; i < result.length; i++) {
            affectRows += result[i];
        }
        return affectRows;
    }

    @Override
    public int batchUpdate(String statement, List<Object[]> parameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        int[] result = this.jdbcTemplate.batchUpdate(statement, parameters);
        int affectRows = 0;
        for(int i = 0; i < result.length; i++) {
            affectRows += result[i];
        }
        return affectRows;
    }

    @Override
    public int batchUpdate(String statement, Map<String, ?>[] mapParameters) {
        if(StringUtils.isEmpty(statement)) {
            throw new ArgumentNullException("statement");
        }
        int[] result = this.namedParameterJdbcTemplate.batchUpdate(statement, mapParameters);
        int affectRows = 0;
        for(int i = 0; i < result.length; i++) {
            affectRows += result[i];
        }
        return affectRows;
    }

    private String pagingStatement(String statement, int pageIndex, int pageSize) {
        if(pageIndex == 0) {
            statement = pageCommand.firstPageStatement(statement, pageIndex, pageSize);
        } else {
            statement = pageCommand.pageStatement(statement, pageIndex, pageSize);
        }
        return statement;
    }

    private long getLong(Object value) {
        if(value == null) {
            return 0L;
        }
        return Long.valueOf(String.valueOf(value));
    }
}
