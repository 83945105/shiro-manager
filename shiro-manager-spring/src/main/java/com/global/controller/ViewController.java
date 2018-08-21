package com.global.controller;

import com.global.conf.MessageConfig;
import com.global.conf.ShiroConfig;
import com.shiro.modules.LoginAuthenticationModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 视图
 * Created by 白超 on 2018-3-14.
 */
@RequestMapping("/view")
@Controller
public class ViewController {

    @Autowired
    private LoginAuthenticationModule loginAuthenticationModule;

    //没有权限页面
    @RequestMapping("/noAuthority")
    public String noAuthority(Model model, HttpServletRequest request) throws Exception {
        model.addAttribute("message", MessageConfig.EXCEPTION_NO_AUTHENTICATION_MESSAGE);
        return "error/noAuthority";
    }

    //登录
    @RequestMapping("/login")
    public String login(HttpServletRequest request) throws Exception {
        request.setAttribute("loginUrl", ShiroConfig.LOGIN_POST_URL);
        request.setAttribute("usernameParam", ShiroConfig.LOGIN_USER_NAME_PARAM);
        request.setAttribute("passwordParam", ShiroConfig.LOGIN_PASS_WORD_PARAM);
        return "login/index";
    }

    //首页
    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) throws Exception {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        model.addAttribute("basePath", basePath);
        return "manager/home";
    }

    //模块管理页面
    @RequestMapping("/module-manager")
    public String moduleManager(HttpServletRequest request, Model model) throws Exception {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        model.addAttribute("basePath", basePath);
        return "manager/module";
    }

    //权限管理页面
    @RequestMapping("/authority-manager")
    public String authorityManager(HttpServletRequest request, Model model) throws Exception {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        model.addAttribute("basePath", basePath);
        return "manager/authority";
    }

}
