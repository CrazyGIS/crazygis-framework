package com.crazygis.data;

import com.crazygis.data.page.PageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by William on 2014/12/31.
 */
public interface IBasicDao {

    List<Map<String, Object>> query(String statement);

    List<Map<String, Object>> query(String statement, Object[] parameters);

    List<Map<String, Object>> query(String statement, Map<String, ?> mapParameters);


    PageResult<Map<String, Object>> queryPage(String statement, int pageIndex, int pageSize);

    PageResult<Map<String, Object>> queryPage(String statement, Object[] parameters, int pageIndex, int pageSize);

    PageResult<Map<String, Object>> queryPage(String statement, Map<String, ?> mapParameters, int pageIndex, int pageSize);


    <T> List<T> find(Class<T> clazz, String statement);

    <T> List<T> find(Class<T> clazz, String statement, Object[] parameters);

    <T> List<T> find(Class<T> clazz, String statement, Map<String, ?> mapParameters);


    <T> PageResult<T> findPage(Class<T> clazz, String statement, int pageIndex, int pageSize);

    <T> PageResult<T> findPage(Class<T> clazz, String statement, Object[] parameters, int pageIndex, int pageSize);

    <T> PageResult<T> findPage(Class<T> clazz, String statement, Map<String, ?> mapParameters, int pageIndex, int pageSize);


    Map<String, Object> get(String statement);

    Map<String, Object> get(String statement, Object[] parameters);

    Map<String, Object> get(String statement, Map<String, ?> mapParameters);


    <T> T get(Class<T> clazz, String statement);

    <T> T get(Class<T> clazz, String statement, Object[] parameters);

    <T> T get(Class<T> clazz, String statement, Map<String, ?> mapParameters);


    Object scalar(String statement);

    Object scalar(String statement, Object[] parameters);

    Object scalar(String statement, Map<String, ?> mapParameters);

    <T> T scalar(Class<T> clazz, String statement, Object[] parameters);

    <T> T scalar(Class<T> clazz, String statement, Map<String, ?> mapParameters);


    int update(String statement);

    int update(String statement, Object[] parameters);

    int update(String statement, Map<String, ?> mapParameters);


    int batchUpdate(String... statements);

    int batchUpdate(String statement, List<Object[]> parameters);

    int batchUpdate(String statement, Map<String, ?>[] mapParameters);
}
