package com.crazygis.security.springsecurity.model;

/**
 * Created by xuguolin on 2017/3/21.
 */
public class SystemMenu extends SystemFunction {
    private String icon;
    private String url;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
