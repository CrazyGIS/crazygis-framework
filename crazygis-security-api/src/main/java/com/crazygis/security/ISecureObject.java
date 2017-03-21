package com.crazygis.security;

/**
 * 表示一个安全对象
 * Created by xuguolin on 2017/3/18.
 */
public interface ISecureObject {

    /**
     * 安全对象的标识符
     *
     * 对于MethodInvocation，SecureId为方法的功能编号或方法名称（含类型全称）
     * 对于FilterInvocation，SecureId为HTTP请求的URI
     *
     * @return
     */
    String getSecureId();
}
