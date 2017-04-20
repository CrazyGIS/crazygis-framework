package com.crazygis.web.utils;

import com.crazygis.common.exceptions.ArgumentNullException;
import com.crazygis.web.ApplicationError;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;

/**
 * Created by xuguolin on 2017/3/18.
 */
public final class WebUtils {
    private static final String TITLE = "HTML_PAGE_TITLE";
    private static final String APPLICATION_ERROR = "Application_Error";

    private static final String NO_MENU_KEY = "noMenu";

    private WebUtils() {

    }

    /**
     * 判断客户端的请求是否为AJAX请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        if (request == null) {
            throw new RuntimeException("request is null", null);
        }
        String accept = request.getHeader("accept");
        return (accept != null && accept.length() > 0 && accept.indexOf("application/json") > -1)
                || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals("XMLHttpRequest"));
    }

    public static void setApplicationError(HttpServletRequest request, ApplicationError error) {
        if(request != null) {
            request.setAttribute(APPLICATION_ERROR, error);
        }
    }

    public static ApplicationError getApplicationError(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        return (ApplicationError)request.getAttribute(APPLICATION_ERROR);
    }

    public static void setTitle(HttpServletRequest request, String title) {
        if(request == null) {
            throw new ArgumentNullException("request");
        }
        request.setAttribute(TITLE, title);
    }

    public static String getTitle(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        String title = (String)request.getAttribute(TITLE);
        if(StringUtils.isEmpty(title)) {
            title = "";
        }
        return title;
    }

    public static void setNoMenu(HttpServletRequest request) {
        request.setAttribute(NO_MENU_KEY, "true");
    }

    public static String getNoMenu(HttpServletRequest request) {
        String noMenu = (String)request.getAttribute(NO_MENU_KEY);
        if(StringUtils.isBlank(noMenu)) {
            return "false";
        }
        return "true";
    }

    public static String formatDefaultDate(Date date) {
        return formatDate(date, null);
    }

    public static String formatDate(Date date, String formatString) {
        if(date == null) {
            return "";
        }
        if(StringUtils.isBlank(formatString)) {
            formatString = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(formatString);
        try {
            return localSimpleDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 对HTML中的特殊字符进行编码转换
     * @param aText
     * @return
     */
    public static String HTMLEncode(String aText){
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character = iterator.current();
        while (character != CharacterIterator.DONE ){
            if (character == '<') {
                result.append("&lt;");
            }
            else if (character == '>') {
                result.append("&gt;");
            }
            else if (character == '&') {
                result.append("&amp;");
            }
//            else if (character == '\"') {
//                result.append("&quot;");
//            }
            else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
}
