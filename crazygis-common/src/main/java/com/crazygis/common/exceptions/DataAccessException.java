package com.crazygis.common.exceptions;

/**
 * Created by Soon on 2014/10/11.
 */
public class DataAccessException extends ApplicationException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable e) {
        super(message, e);
    }
}
