package com.crazygis.security.springsecurity;

import com.crazygis.web.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限验证失败的处理方法
 *
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisAccessDeniedHandler implements AccessDeniedHandler {

    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";

    // 共通错误页定义
    private String errorPage = null;
    // CSRF错误页定义
    private String csrfErrorPage = null;

    /**
     * 资源访问拒绝处理
     *
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String errPage = null;

        // Put exception into request scope (perhaps of use to a view)
        request.setAttribute("exception", accessDeniedException);

        // Set the 403 status code.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 如果是AJAX请求，采取返回JSON内容的处理方式
        if(WebUtils.isAjaxRequest(request)) {
            response.setCharacterEncoding(DEFAULT_CHAR_ENCODING);
            response.setContentType(DEFAULT_CONTENT_TYPE);

            PrintWriter out = response.getWriter();
            out.print(accessDeniedException.getMessage());
        } else {
            // 如果启用了Spring Security的CSRF防御策略，则针对性地处理有关csrf的特殊异常类型
            if(accessDeniedException instanceof MissingCsrfTokenException || accessDeniedException instanceof InvalidCsrfTokenException){
                errPage = this.csrfErrorPage;
            } else {
                if (!response.isCommitted()) {
                    errPage = this.errorPage;
                }
            }

            // forward to error page.
            RequestDispatcher dispatcher = request.getRequestDispatcher(errPage);
            dispatcher.forward(request, response);
        }


        //do some business logic, then redirect to errorPage url
        //response.sendRedirect(this.errorPage);

        //response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
    }

    public void setErrorPage(String errorPage) {
        if (StringUtils.isBlank(errorPage) || !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("error page must begin with '/'");
        }
        this.errorPage = errorPage;
    }

    public void setCsrfErrorPage(String csrfErrorPage) {
        if (StringUtils.isBlank(csrfErrorPage) || !csrfErrorPage.startsWith("/")) {
            throw new IllegalArgumentException("csrf error page must begin with '/'");
        }
        this.csrfErrorPage = csrfErrorPage;
    }
}
