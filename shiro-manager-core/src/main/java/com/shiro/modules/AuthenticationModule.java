package com.shiro.modules;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 白超 on 2018/6/8.
 */
public interface AuthenticationModule extends Module {

    /**
     * 是否是Ajax请求
     *
     * @param request
     * @return
     */
    default boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

}
