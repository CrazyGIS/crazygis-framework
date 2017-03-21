package com.crazygis.webapp.controller;
import com.crazygis.web.ApplicationError;
import com.crazygis.web.ApplicationExceptionHandler;
import com.crazygis.web.utils.WebUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 网站默认页面控制器，如：首页、登录页面、异常页面等
 * Created by William on 2014/9/10.
 */
@Controller
public class DefaultController {

    protected final Logger logger = LogManager.getLogger(getClass());
    /**
     * 进入根路径，重定向到登录页
     */
    @RequestMapping("/")
    public String rootPage() {
        return "redirect:/signin";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String indexPage(){
        return "home/index";
    }

    @RequestMapping(value = "/building", method = RequestMethod.GET)
    public String building() {
        return "home/building";
    }

    @RequestMapping("/signin")
    public String signInPage() {
        return "signin";
    }

    @RequestMapping("/signout")
    public String signOutPage(){
        return "signout";
    }

    @RequestMapping(value = "/error")
    public ModelAndView Error(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = null;
        ApplicationError error = ApplicationExceptionHandler.getApplicationError(request);
        logger.error(error.getErrorMessage(), error.getException());
        WebUtils.setApplicationError(request, error);
        if(WebUtils.isAjaxRequest(request)) {
            mv = new ModelAndView("shared/ajaxerror");
        } else {
            mv = new ModelAndView("shared/error");
        }
        return mv;
    }
}
