package com.crazygis.security.springsecurity;

import com.crazygis.security.ISecureObjectManager;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisMethodSecurityMetadataSource implements MethodSecurityMetadataSource {

    private ISecureObjectManager secureObjectManager;

    /**
     * 设置资源信息管理器
     * @param secureObjectManager
     */
    public void setSecureObjectManager(ISecureObjectManager secureObjectManager) {
        this.secureObjectManager = secureObjectManager;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        // The method may be on an interface, but we need attributes from the target
        // class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        // First try is the method in the target class.
        Collection<ConfigAttribute> attr = findAttributes(specificMethod, targetClass);
        if (attr != null) {
            return attr;
        }

        // Second try is the config attribute on the target class.
//        attr = findAttributes(specificMethod.getDeclaringClass());
//        if (attr != null) {
//            return attr;
//        }

        if (specificMethod != method || targetClass == null) {
            // Fallback is to look at the original method.
            attr = findAttributes(method, method.getDeclaringClass());
            if (attr != null) {
                return attr;
            }
            // Last fallback is the class of the original method.
//            return findAttributes(method.getDeclaringClass());
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (object instanceof MethodInvocation) {
            MethodInvocation mi = (MethodInvocation) object;
            Object target = mi.getThis();
            Class<?> targetClass = null;

            if (target != null) {
                targetClass = target instanceof Class<?> ? (Class<?>) target
                        : AopProxyUtils.ultimateTargetClass(target);
            }
            Collection<ConfigAttribute> attrs = getAttributes(mi.getMethod(), targetClass);
            if (attrs != null && !attrs.isEmpty()) {
                return attrs;
            }
            if (target != null && !(target instanceof Class<?>)) {
                attrs = getAttributes(mi.getMethod(), target.getClass());
            }
            return attrs;
        }

        throw new IllegalArgumentException("Object must be a non-null MethodInvocation");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        //getAllConfigAttribute方法会被DelegatingMethodSecurityMetadataSource内部调用,
        //最终在AbstractSecurityInterceptor中用于检查所有的配置是否被AccessDecisionManager支持
        //具体代码参考:AbstractSecurityInterceptor的afterPropertiesSet方法实现(第155行)
        return null;
    }

    public boolean supports(Class<?> aClass) {
        return false;
    }

    protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {

        return null;
    }

}
