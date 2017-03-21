package com.crazygis.web;

import com.crazygis.common.exceptions.ArgumentNullException;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Soon on 2014/10/14.
 */
public class ApplicationExceptionHandler {

    private final static String EXCEPTION = "javax.servlet.error.exception";
    private final static String HTTP_STATUS_CODE = "javax.servlet.error.status_code";

    public static Throwable getException(HttpServletRequest request) {
        if(request == null) {
            throw new ArgumentNullException("request");
        }
        Throwable exception = (Exception)request.getAttribute("exception");
        if(exception == null) {
            exception = (Throwable)request.getAttribute(EXCEPTION);
        }
        return exception;
    }

    public static int getHttpStatusCode(HttpServletRequest request) {
        if(request == null) {
            throw new ArgumentNullException("request");
        }
        int statusCode = ((Integer)request.getAttribute(HTTP_STATUS_CODE)).intValue();
        return statusCode;
    }

    public static ApplicationError getApplicationError(HttpServletRequest request) {
        Throwable exception = getException(request);
        int statusCode = getHttpStatusCode(request);
        return getApplicationError(request, exception, statusCode);
    }

    public static ApplicationError getApplicationError(HttpServletRequest request, Throwable exception, int statusCode) {
        String errorMessage = getExceptionMessage(exception);

        if(exception instanceof AccessDeniedException) {
            statusCode = 403;
        }

        ApplicationError error = new ApplicationError();
        error.setErrorMessage(errorMessage);
        error.setHttpStatusCode(statusCode);
        error.setException(exception);

        if(statusCode == 404) {
            error.setErrorInformation("您访问的内容不存在。");
        } else if(statusCode == 401 || statusCode == 403) {
            error.setErrorInformation("您没有权限访问此内容。");
        } else if(statusCode >= 500) {
            error.setErrorInformation("处理您的请求时发生错误，请联系管理员。");
        } else {
            error.setErrorInformation("信息提示: ");
        }
        return error;
    }

    private static String getExceptionMessage(Throwable exception) {
        if(exception == null) {
            return "Unknown Exception";
        }
        Throwable[] innerException = exception.getSuppressed();
        StringBuilder messageBuilder = new StringBuilder(exception.getMessage());
        if(innerException != null) {
            for(int i = 0; i < innerException.length; i++) {
                messageBuilder.append(" -> ").append(innerException[i].getMessage());
            }
        }
        return messageBuilder.toString();
    }
}
