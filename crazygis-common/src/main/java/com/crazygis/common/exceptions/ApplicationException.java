package com.crazygis.common.exceptions;

/**
 * Created by Soon on 2014/10/11.
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable e) {
        super(message, e);
    }
}
