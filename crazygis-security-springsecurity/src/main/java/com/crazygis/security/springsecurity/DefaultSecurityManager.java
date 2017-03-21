package com.crazygis.security.springsecurity;

import com.crazygis.security.ISecureObject;
import com.crazygis.security.ISecureObjectManager;
import com.crazygis.security.IUserManager;
import com.crazygis.security.model.User;
import com.crazygis.security.springsecurity.model.UserDetail;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class DefaultSecurityManager implements IUserManager, ISecureObjectManager {
    private static Map<String, Collection<ConfigAttribute>> metadataSource = null;

    @Override
    public Map<String, Collection<ConfigAttribute>> loadAllConfigAttributes() {

        if(metadataSource == null){
            metadataSource = new HashMap<String, Collection<ConfigAttribute>>();

            //在Spring Security 4.X的登录授权配置:anonymousRoleCA是必须的,主要为signin页面进行授权.
            //登录配置必须在其他配置之前
            Collection<ConfigAttribute> signinCF = new HashSet<ConfigAttribute>();
            signinCF.add(new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_ANONYMOUSLY));
            metadataSource.put("/signin",signinCF);

            //普通用户权限配置
            Collection<ConfigAttribute> userCF = new HashSet<ConfigAttribute>();
            userCF.add(new SecurityConfig("user"));
            metadataSource.put("/**",userCF);//通配所有路径
        }

        return metadataSource;
    }

    @Override
    public Collection<ConfigAttribute> loadConfigAttributes(ISecureObject secureObject){

        if(secureObject.getSecureId().equalsIgnoreCase("F1001")){
            List<ConfigAttribute> attributes = new ArrayList<>();

            //默认对所有安全对象赋予User角色.
            attributes.add(new SecurityConfig("user"));
            return attributes;
        }

        return null;
    }

    @Override
    public User loadUserByUsername(String userName) {
        UserDetail user = new UserDetail();
        user.setUsername(userName);
        user.setPassword("c4ca4238a0b923820dcc509a6f75849b");
        user.setAuthorities(AuthorityUtils.createAuthorityList("user"));
        return user;

        // TODO:用户名默认为admin,默认密码为1,经过加密(加盐)后是:c3ce8345d3599ab8a4c337d9fb0d0d93
        //return new User(userName, "c3ce8345d3599ab8a4c337d9fb0d0d93", true, true, true, true, AuthorityUtils.createAuthorityList("user"));
    }
}
