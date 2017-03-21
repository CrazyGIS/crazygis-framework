package com.crazygis.web.page;

import com.crazygis.common.PropertiesManager;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class PageResource {
    private static final String CONTEXT_NAME = "";
    private static final Pattern httpPattern = Pattern.compile("^(http|https)\\:\\/\\/\\w*", Pattern.CASE_INSENSITIVE);

    public static String cssLink(String... srcArray) {
        StringBuilder cssBuilder = new StringBuilder();
        if (srcArray == null) {
            return null;
        }
        String src = null;
        for (int i = 0; i < srcArray.length; i++) {
            src = srcArray[i];
            if (StringUtils.isEmpty(src)) {
                continue;
            }
            cssBuilder.append(createLink(src));
        }
        return cssBuilder.toString();
    }

    public static String scriptLink(String... srcArray) {
        StringBuilder scriptBuilder = new StringBuilder();
        if (srcArray == null) {
            return null;
        }
        String src = null;
        for (int i = 0; i < srcArray.length; i++) {
            src = srcArray[i];
            if (StringUtils.isEmpty(src)) {
                continue;
            }
            scriptBuilder.append(createScript(src));
        }
        return scriptBuilder.toString();
    }

    public static String formatSrc(String src) {
        if (StringUtils.isEmpty(src)) {
            return CONTEXT_NAME;
        }
        Matcher httpMatcher = httpPattern.matcher(src);
        if (httpMatcher.find()) {
            return src;
        }
        return CONTEXT_NAME + src;
    }

    public static String map() {
        PropertiesManager pm = PropertiesManager.getWebInstance();
        String scripts = pm.get("scripts");
        String styles = pm.get("styles");
        String[] items;
        String src;
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isEmpty(scripts)) {
            items = scripts.split(",");
            for (int i = 0; i < items.length; i++) {
                src = items[i].trim();
                if (StringUtils.isEmpty(src)) {
                    continue;
                }
                builder.append(createScript(src));
            }
        }
        if (!StringUtils.isEmpty(styles)) {
            items = styles.split(",");
            for (int i = 0; i < items.length; i++) {
                src = items[i].trim();
                if (StringUtils.isEmpty(src)) {
                    continue;
                }
                builder.append(createLink(src));
            }
        }
        return builder.toString();
    }

    private static String createScript(String src) {
        return "<script type=\"text/javascript\" src=\"" + formatSrc(src) + "\"></script>\n";
    }

    private static String createLink(String src) {
        return "<link type=\"text/css\" href=\"" + formatSrc(src) + "\" rel=\"stylesheet\"/>\n";
    }
}
