package com.global.conf;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/6/15.
 */
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //静态资源路径前缀
        request.setAttribute("staticPathPrefix", YmlConfig.STATIC_PATH_PREFIX);
        return true;
    }
}
