package com.crazygis.common.exceptions;

/**
 * Created by Soon on 2014/10/11.
 */
public class BusinessException extends ApplicationException {
    private final static String DEFAULT_MESSAGE = "业务逻辑出现错误";
    public BusinessException() {
        super(DEFAULT_MESSAGE);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return DEFAULT_MESSAGE + " " + super.getMessage();
    }
}
