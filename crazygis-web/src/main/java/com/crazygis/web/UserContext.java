package com.crazygis.web;

import com.crazygis.security.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by xuguolin on 2017/3/18.
 */
public class UserContext {
    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof User)) {
            return null;
        }

        return (User) principal;
    }
}
