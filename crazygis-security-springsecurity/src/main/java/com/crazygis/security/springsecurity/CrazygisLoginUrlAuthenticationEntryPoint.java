package com.crazygis.security.springsecurity;

import com.crazygis.web.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public static final String DEFAULT_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public CrazygisLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if(WebUtils.isAjaxRequest(request)) {
            response.setCharacterEncoding(DEFAULT_CHAR_ENCODING);
            response.setContentType(DEFAULT_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            PrintWriter out = response.getWriter();
            out.print(authException.getMessage());
        } else {
            super.commence(request, response, authException);
        }
    }
}
