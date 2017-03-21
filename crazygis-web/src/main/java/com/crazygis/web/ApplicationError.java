package com.crazygis.web;

/**
 * Created by Soon on 2014/10/14.
 */
public class ApplicationError {

    private String errorInformation;
    private String errorMessage;
    private int httpStatusCode;
    private Throwable exception;

    public String getErrorInformation() {
        return errorInformation;
    }

    public void setErrorInformation(String errorInfomation) {
        this.errorInformation = errorInfomation;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
