package com.crazygis.common.exceptions;

/**
 * Created by Soon on 2014/10/11.
 */
public class ArgumentException extends ApplicationException {

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(String message, Throwable e) {
        super(message);
    }
}
