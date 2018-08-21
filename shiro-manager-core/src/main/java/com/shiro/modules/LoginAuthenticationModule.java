package com.shiro.modules;

import com.shiro.filter.RequestHandler;
import com.shiro.norm.Certificate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录认证模块
 * Created by 白超 on 2018/6/7.
 */
public interface LoginAuthenticationModule<T extends Certificate> extends AuthenticationModule {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    T login(String username, String password) throws AuthenticationException;

    /**
     * 查询用户拥有的角色
     *
     * @param certificate 证书
     * @return
     */
    Set<String> getRoles(T certificate);

    /**
     * 查询用户可以访问的urls
     *
     * @param certificate 证书
     * @param roles       用户拥有的角色信息
     * @return
     */
    Set<String> getUrls(T certificate, Set<String> roles);

    /**
     * 表单登录成功
     *
     * @param handler  操作
     * @param request  请求
     * @param response 响应
     * @return 是否继续执行过滤器
     */
    default boolean formLoginSuccess(RequestHandler handler, ServletRequest request, ServletResponse response) {
        try {
            handler.redirectToSavedRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 表单登录失败
     *
     * @param handler  操作
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return 是否继续执行过滤器
     */
    default boolean formLoginFail(RequestHandler handler, ServletRequest request, ServletResponse response, Exception e) {
        try {
            handler.redirectToLogin(request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * ajax登录成功
     *
     * @param request  请求
     * @param response 响应
     * @return 是否继续执行过滤器
     */
    default boolean ajaxLoginSuccess(ServletRequest request, ServletResponse response) {
        return false;
    }

    /**
     * ajax登录失败
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return 是否继续执行过滤器
     */
    default boolean ajaxLoginFail(ServletRequest request, ServletResponse response, Exception e) {
        return false;
    }

    /**
     * Ajax访问被拒绝
     *
     * @param request  请求
     * @param response 响应
     * @return 是否继续执行过滤器
     * @throws Exception
     */
    default boolean onAjaxAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /**
     * 登录请求被拒绝
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    default boolean onLoginAccessDenied(String username, String password, ServletRequest request, ServletResponse response) throws Exception {
        return this.subjectLogin(username, password);
    }


    /**
     * 访问被拒绝
     * 如果是Ajax请求访问被拒绝则会执行 {@link #onAjaxAccessDenied(javax.servlet.ServletRequest, javax.servlet.ServletResponse) onAjaxAccessDenied(request, response)}
     *
     * @param handler
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    default boolean onAccessDenied(RequestHandler handler, ServletRequest request, ServletResponse response) throws Exception {
        handler.saveRequestAndRedirectToLogin(request, response);
        return false;
    }

    /**
     * 判断用户是否已经登录
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    default boolean isAuthenticated(ServletRequest request, ServletResponse response, Object mappedValue) {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * subject登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否登录成功
     */
    default boolean subjectLogin(String username, String password) throws Exception {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        return true;
    }

    class DefaultLoginAuthenticationModule implements LoginAuthenticationModule<Certificate> {

        @Override
        public Certificate login(String username, String password) throws AuthenticationException {
            return null;
        }

        @Override
        public Set<String> getRoles(Certificate certificate) {
            return new HashSet<>();
        }

        @Override
        public Set<String> getUrls(Certificate certificate, Set<String> roles) {
            return new HashSet<>();
        }

        @Override
        public boolean formLoginSuccess(RequestHandler handler, ServletRequest request, ServletResponse response) {
            System.out.println("表单登录成功");
            return false;
        }

        @Override
        public boolean formLoginFail(RequestHandler handler, ServletRequest request, ServletResponse response, Exception e) {
            System.err.println("表单登录失败");
            return false;
        }

        @Override
        public boolean ajaxLoginSuccess(ServletRequest request, ServletResponse response) {
            System.out.println("Ajax登录成功");
            return false;
        }

        @Override
        public boolean ajaxLoginFail(ServletRequest request, ServletResponse response, Exception e) {
            System.err.println("Ajax登录失败");
            return false;
        }

        @Override
        public boolean onAjaxAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
            System.err.println("Ajax请求被拒绝");
            return false;
        }

        @Override
        public boolean onAccessDenied(RequestHandler handler, ServletRequest request, ServletResponse response) throws Exception {
            System.err.println("请求被拒绝");
            return false;
        }

        @Override
        public boolean isAuthenticated(ServletRequest request, ServletResponse response, Object mappedValue) {
            System.err.println("无认证信息");
            return false;
        }
    }
}
