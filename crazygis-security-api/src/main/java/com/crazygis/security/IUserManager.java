package com.crazygis.security;

import com.crazygis.security.model.SysUser;

/**
 * Created by xuguolin on 2017/3/18.
 */
public interface IUserManager {
    /**
     * 根据用户名获取用户详细信息
     * @param userName
     * @return
     */
    SysUser loadUserByUsername(String userName);
}
