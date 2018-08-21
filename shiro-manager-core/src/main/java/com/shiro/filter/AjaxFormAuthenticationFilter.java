package com.shiro.filter;

import com.shiro.modules.LoginAuthenticationModule;
import com.shiro.modules.Module;
import com.shiro.modules.ShiroModules;
import com.shiro.norm.Certificate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>Title: 自定义Form过滤器,用于实现表单和AJAX登录</p>
 * <p>Description: </p>
 *
 * @author 白超
 * @date 2017年6月16日 上午8:55:17
 * @版本 V 1.0
 */
public class AjaxFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(AjaxFormAuthenticationFilter.class);

    private LoginAuthenticationModule loginAuthenticationModule = new LoginAuthenticationModule.DefaultLoginAuthenticationModule();

    private ShiroModules modules = new ShiroModules();

    private RequestHandler requestHandler = new DefaultRequestHandler();

    private boolean crossDomain;

    public void setModules(ShiroModules modules) {
        this.modules = modules;
        Module module = this.modules.getModule(LoginAuthenticationModule.class);
        if (module != null) {
            this.loginAuthenticationModule = (LoginAuthenticationModule<Certificate>) module;
        }
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        /**
         * 先判断是否是options请求,然后根据是否允许跨域配置决定是否放行
         * 如果用户请求URL不是该表单验证器配置的loginUrl,则使用原始{@link FormAuthenticationFilter} 的onPreHandle方法<br/>
         * 该方法会调用isAccessAllowed(本类已重写),如果返回false则调用onAccessDenied(本类已重写),若还是返回false则禁止用户访问<br/>
         *
         * 如果用户请求URL是该表单验证器配置的loginUrl,则继续判断是否是POST请求<br/>
         * 如果不是POST请求,则放行,在这里,GET请求的loginUrl用于进入登录页面,该页面的GET请求需要根据项目单独编写<br/>
         *
         * 如果是POST请求,则表示该请求用于本地登录校验,首先判断该POST请求是Form表单请求还是AJAX请求<br/>
         * 如果是Form请求则执行formToLogin<br/>
         * 如果是Ajax请求则执行ajaxTologin<br/>
         */
        if ("OPTIONS".equalsIgnoreCase(req.getMethod()) && this.crossDomain) {
            return true;
        }
        if (!isLoginRequest(request, response)) {
            return super.onPreHandle(request, response, mappedValue);
        }
        if (!"POST".equalsIgnoreCase(req.getMethod())) {
            return true;
        }
        if (this.loginAuthenticationModule.isAjax(req)) {
            return ajax2login(req, resp);
        }
        return form2Login(req, resp);
    }

    //form表单登录
    private boolean form2Login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(this.getUsernameParam());
        String password = request.getParameter(this.getPasswordParam());

        try {
            this.loginAuthenticationModule.subjectLogin(username, password);
        } catch (Exception e) {
            return this.loginAuthenticationModule.formLoginFail(requestHandler, request, response, e);
        }

        SecurityUtils.getSubject().hasRole("admin");
        return this.loginAuthenticationModule.formLoginSuccess(requestHandler, request, response);
    }

    //ajax登录
    private boolean ajax2login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = request.getParameter(this.getUsernameParam());
        String password = request.getParameter(this.getPasswordParam());

        try {
            this.loginAuthenticationModule.subjectLogin(username, password);
        } catch (Exception e) {
            return this.loginAuthenticationModule.ajaxLoginFail(request, response, e);
        }

        SecurityUtils.getSubject().hasRole("admin");
        return this.loginAuthenticationModule.ajaxLoginSuccess(request, response);
    }

    /**
     * 以用户是否登录为条件判定是否允许访问
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return this.loginAuthenticationModule.isAuthenticated(request, response, mappedValue);
    }

    /**
     * 当访问被拒绝时
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {

            if (log.isTraceEnabled()) {
                log.trace("检测到登录请求...尝试执行登录");
            }

            String username = request.getParameter(this.getUsernameParam());
            String password = request.getParameter(this.getPasswordParam());
            boolean isLoginSubmission = isLoginSubmission(request, response);

            if (isLoginSubmission) {
                if (log.isTraceEnabled()) {
                    log.trace("检测到Post登录请求...尝试执行登录");
                }
                return this.loginAuthenticationModule.onLoginAccessDenied(username, password, request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("未检测到Post登录请求...");
                }
                return true;
            }
        } else {
            if (this.loginAuthenticationModule.isAjax((HttpServletRequest) request)) {
                return this.loginAuthenticationModule.onAjaxAccessDenied(request, response);
            }
            return this.loginAuthenticationModule.onAccessDenied(requestHandler, request, response);
        }
    }

    public LoginAuthenticationModule getLoginAuthenticationModule() {
        return loginAuthenticationModule;
    }

    public void setLoginAuthenticationModule(LoginAuthenticationModule loginAuthenticationModule) {
        this.loginAuthenticationModule = loginAuthenticationModule;
    }

    public boolean isCrossDomain() {
        return crossDomain;
    }

    public void setCrossDomain(boolean crossDomain) {
        this.crossDomain = crossDomain;
    }

    private final class DefaultRequestHandler implements RequestHandler {

        @Override
        public void redirectToLogin(ServletRequest request, ServletResponse response) throws Exception {
            AjaxFormAuthenticationFilter.this.redirectToLogin(request, response);
        }

        @Override
        public void redirectToSavedRequest(ServletRequest request, ServletResponse response) throws Exception {
            WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
        }

        @Override
        public void redirectTo(String url, Map queryParams, boolean contextRelative, ServletRequest request, ServletResponse response) throws Exception {
            WebUtils.issueRedirect(request, response, url, queryParams, contextRelative);
        }

        @Override
        public void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws Exception {
            AjaxFormAuthenticationFilter.this.saveRequestAndRedirectToLogin(request, response);
        }
    }

}
