package com.crazygis.web;

import com.crazygis.web.page.PageResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xuguolin on 2017/3/21.
 */
public class WebManager {

    private WebManager() {

    }

    public static String getCurrentFunctionCode(HttpServletRequest request) {
        String functionCode = "";
        if (request == null)
        {
            return functionCode;
        }
        else
        {
            functionCode = request.getParameter("_m");
        }
        if (StringUtils.isEmpty(functionCode)) {
            functionCode = "";
        } else {
            functionCode = new String(Base64.decodeBase64(functionCode));
        }
        return functionCode;
    }

    public static String getParentFunctionCode(String funcCode) {
        if (funcCode == null) {
            return null;
        }
        int index = funcCode.lastIndexOf("_");
        if (index == -1) {
            return null;
        }
        else {
            return funcCode.substring(0, index);
        }
    }

    public static boolean hasParent(String funcCode) {
        if(StringUtils.isEmpty(funcCode)) {
            return false;
        }
        return funcCode.indexOf("_") > -1;
    }

    public static String formatUrlByFunctionCode(String url, String code) {
        if(StringUtils.isEmpty(code)) {
            code = "00";
        }
        String encode = Base64.encodeBase64String(code.getBytes());
        String newUrl = PageResource.formatSrc(url) + "?_m=" + encode;
        return newUrl;
    }
}
