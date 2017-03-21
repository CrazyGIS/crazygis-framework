package com.crazygis.security;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xuguolin on 2017/3/18.
 */
public interface ISecureObjectManager extends ISecurityManager {
    /**
     * 加载所有资源的权限配置
     *
     * @return
     */
    Map<String, Collection<ConfigAttribute>> loadAllConfigAttributes();

    /**
     * 加载某一对象资源的权限配置
     *
     * @return
     */
    Collection<ConfigAttribute> loadConfigAttributes(ISecureObject secureObject);
}
