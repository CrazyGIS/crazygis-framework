package com.crazygis.security;

/**
 * Created by xuguolin on 2017/3/21.
 */
public interface IFunction {

    String getFunctionId();

    void setFunctionId(String functionId);

    String getFunctionCode();

    void setFunctionCode(String functionCode);

    String getFunctionName();

    void setFunctionName(String functionName);

    String getParentId();

    void setParentId(String parentId);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
