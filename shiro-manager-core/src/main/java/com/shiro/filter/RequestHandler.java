package com.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * Created by 白超 on 2018/6/14.
 */
public interface RequestHandler {

    /**
     * 重定向到登录页
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void redirectToLogin(ServletRequest request, ServletResponse response) throws Exception;

    /**
     * 重定向到登录前地址
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void redirectToSavedRequest(ServletRequest request, ServletResponse response) throws Exception;

    /**
     * 重定向到指定地址
     *
     * @param url             地址
     * @param queryParams     参数
     * @param contextRelative 是否相对路径
     * @param request
     * @param response
     * @throws Exception
     */
    void redirectTo(String url, Map queryParams, boolean contextRelative, ServletRequest request, ServletResponse response) throws Exception;

    /**
     * 保存请求地址并重定向到登录地址
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws Exception;

}
