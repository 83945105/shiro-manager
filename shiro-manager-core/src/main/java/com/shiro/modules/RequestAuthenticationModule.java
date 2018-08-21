package com.shiro.modules;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 请求认证模块
 * Created by 白超 on 2018/6/7.
 */
public interface RequestAuthenticationModule extends AuthenticationModule {

    /**
     * 请求被拒绝
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception;

    class DefaultRequestAuthenticationModule implements RequestAuthenticationModule {

        @Override
        public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
            return false;
        }
    }
}
