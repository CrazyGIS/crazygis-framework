package com.crazygis.security.springsecurity;

import com.crazygis.security.IUserManager;
import com.crazygis.security.model.User;
import com.crazygis.security.springsecurity.model.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 获取验证的用户信息
 * Created by xuguolin on 2017/3/18.
 */
public class CrazygisCustomUserDetailsService implements UserDetailsService {

    private IUserManager userManager;

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userManager == null){
            throw new RuntimeException("Can not find userManager when loadUserByUsername.");
        }

        User user = this.userManager.loadUserByUsername(username);
        UserDetails userDetails;
        if(user instanceof UserDetails) {
            userDetails = (UserDetails)user;
        } else {
            UserDetail userDetail = new UserDetail();
            userDetail.setUsername(user.getUsername());
            // TODO 设置密码
            userDetail.setPassword("");
            userDetails = userDetail;
        }
        if(userDetails != null){
            return userDetails;
        }

        throw new UsernameNotFoundException("Can't find " + username);
    }
}
