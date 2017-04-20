package com.crazygis.security.springsecurity.model;

import com.crazygis.security.IFunction;

import java.util.List;

/**
 * Created by xuguolin on 2017/3/21.
 */
public class SystemFunction implements IFunction {
    private String functionId;
    private String functionCode;
    private String functionName;
    private String parentId;
    private boolean enabled;

    private IFunction parent;
    private List<IFunction> children;

    private String icon;
    private String url;

    @Override
    public String getFunctionId() {
        return functionId;
    }

    @Override
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    @Override
    public String getFunctionCode() {
        return functionCode;
    }

    @Override
    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    @Override
    public String getFunctionName() {
        return functionName;
    }

    @Override
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public IFunction getParent() {
        return parent;
    }

    public void setParent(IFunction parent) {
        this.parent = parent;
    }

    public List<IFunction> getChildren() {
        return children;
    }

    public void setChildren(List<IFunction> children) {
        this.children = children;
    }

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
