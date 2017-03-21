package com.crazygis.security.springsecurity.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份认证失败处理器
 *
 * Created by zhaojian on 2016/5/27.
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        super.onAuthenticationFailure(request, response, e);
    }

    /**
     * 记录登录日志
     *
     * @param request HttpServletRequest
     * @param e AuthenticationException
     */
    public void recordLoginLog(HttpServletRequest request, final AuthenticationException e) {

    }
}
