package com.crazygis.security.springsecurity;

import com.crazygis.security.ISecureObjectManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisFilterInvocationSecurityMetadataSource
        implements FilterInvocationSecurityMetadataSource {

    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
    private ISecureObjectManager secureObjectManager;

    /**
     * 设置资源信息管理器
     * @param secureObjectManager
     */
    public void setSecureObjectManager(ISecureObjectManager secureObjectManager) {
        this.secureObjectManager = secureObjectManager;

        initRequestMap();
    }

    /**
     * 初始化requestMap，采用AntPathRequestMatcher来匹配HTTP请求的URL
     */
    private void initRequestMap(){
        this.requestMap = new LinkedHashMap<>();

        //通过secureObjectManager获取所有的安全配置，并初始化requestMap对象
        Map<String, Collection<ConfigAttribute>> attributes = this.secureObjectManager.loadAllConfigAttributes();

        //初始化RequestMatcher
        for(Map.Entry<String, Collection<ConfigAttribute>> entry : attributes.entrySet()){
            if(!requestMap.containsKey(entry.getKey())){
                requestMap.put(new AntPathRequestMatcher(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
