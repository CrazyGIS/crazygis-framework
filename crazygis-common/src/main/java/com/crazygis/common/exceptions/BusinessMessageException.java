package com.crazygis.common.exceptions;

/**
 * Created by Soon on 2014/10/11.
 */
public class BusinessMessageException extends BusinessException {

    public BusinessMessageException(String message) {
        super(message);
    }

    public BusinessMessageException(String message, Throwable e) {
        super(message, e);
    }
}
