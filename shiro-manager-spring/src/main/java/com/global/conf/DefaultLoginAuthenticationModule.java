package com.global.conf;

import com.shiro.exception.UnimplementedInterfaceException;
import com.shiro.filter.RequestHandler;
import com.shiro.model.JurRoleResModel;
import com.shiro.model.JurRoleUserModel;
import com.shiro.modules.LoginAuthenticationModule;
import com.shiro.norm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import pub.avalon.holygrail.response.beans.ResultInfo;
import pub.avalon.holygrail.response.utils.ResultUtil;
import pub.avalon.holygrail.response.views.ExceptionView;
import pub.avalon.holygrail.response.views.ModelView;
import pub.avalon.holygrail.utils.StringUtil;
import pub.avalon.sqlhelper.factory.MySqlDynamicEngine;
import pub.avalon.sqlhelper.spring.core.SpringJdbcEngine;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author 白超
 * @date 2018/6/7
 */
@Component
public class DefaultLoginAuthenticationModule implements LoginAuthenticationModule<ShiroUser> {

    private HttpMessageConverter<Object> jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    private static final String DEV_USERNAME = "developer";
    private static final String DEV_PASSWORD = "1024";

    @Override
    public ShiroUser login(String username, String password) throws AuthenticationException {
        if (DEV_USERNAME.equals(username) && DEV_PASSWORD.equals(password)) {
            return new ShiroUser() {
                @Override
                public String getId() {
                    return "1024";
                }

                @Override
                public Object getSalt() {
                    return this.getId();
                }

                @Override
                public Object getUsername() {
                    return "开发者";
                }
            };
        }
        return null;
    }

    @Override
    public Set<String> getRoles(ShiroUser certificate) {
        Set<String> roles = new HashSet<>();
        for (String roleUserTableName : ShiroConfig.ROLE_USER_TABLE_NAMES) {
            List<String> list = this.jdbcEngine.queryForList(String.class, MySqlDynamicEngine.query(roleUserTableName, JurRoleUserModel.class)
                    .column(JurRoleUserModel.Column::role)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.userId().equalTo(certificate.getId()))));
            roles.addAll(list);
        }
        return roles;
    }

    @Override
    public Set<String> getUrls(ShiroUser certificate, Set<String> roles) {
        Set<String> urls = new HashSet<>();
        for (String roleResTableName : ShiroConfig.ROLE_RES_TABLE_NAMES) {
            List<String> list = this.jdbcEngine.queryForList(String.class, MySqlDynamicEngine.query(roleResTableName, JurRoleResModel.class)
                    .column(JurRoleResModel.Column::resUrl)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.role().inS(roles))));
            urls.addAll(list);
        }
        return urls;
    }

    @Override
    public boolean formLoginSuccess(RequestHandler handler, ServletRequest request, ServletResponse response) {
        Session session = SecurityUtils.getSubject().getSession();
        if (session == null) {
            return false;
        }
        Serializable sessionId = session.getId();
        request.setAttribute(ShiroConfig.SESSION_ID_NAME, sessionId.toString());
        Cookie cookie = new Cookie(ShiroConfig.SESSION_ID_NAME, sessionId.toString());
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        ((HttpServletResponse) response).addCookie(cookie);

        try {
            handler.redirectToSavedRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean formLoginFail(RequestHandler handler, ServletRequest request, ServletResponse response, Exception e) {
        try {
            handler.redirectToLogin(request, response);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean ajaxLoginSuccess(ServletRequest request, ServletResponse response) {
        Session session = SecurityUtils.getSubject().getSession();
        if (session == null) {
            return false;
        }

        HttpServletResponse resp = ((HttpServletResponse) response);

        Serializable sessionId = session.getId();
        request.setAttribute(ShiroConfig.SESSION_ID_NAME, sessionId.toString());
        Cookie cookie = new Cookie(ShiroConfig.SESSION_ID_NAME, sessionId.toString());
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        resp.addCookie(cookie);

        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        ModelView modelView = new ModelView(ResultUtil.createSuccess("登录成功"));
        Map<String, Object> map = new HashMap<>(3);
        map.put("sessionIdName", ShiroConfig.SESSION_ID_NAME);
        map.put("sessionIdValue", sessionId.toString());

        map.put("user", user);
        modelView.setRecords(map);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse((HttpServletResponse) response);

        try {
            jackson2HttpMessageConverter.write(modelView, MediaType.APPLICATION_JSON, outputMessage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean ajaxLoginFail(ServletRequest request, ServletResponse response, Exception e) {
        ResultInfo resultInfo;
        if (e instanceof UnknownAccountException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_UNKNOWN_ACCOUNT_MESSAGE);
        } else if (e instanceof IncorrectCredentialsException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_INCORRECT_CREDENTIALS_MESSAGE);
        } else if (e instanceof UnsupportedTokenException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_UNSUPPORTED_TOKEN_MESSAGE);
        } else if (e instanceof LockedAccountException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_LOCKED_ACCOUNT_MESSAGE);
        } else if (e instanceof DisabledAccountException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_DISABLED_ACCOUNT_MESSAGE);
        } else if (e instanceof ExcessiveAttemptsException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_EXCESSIVE_ATTEMPTS_MESSAGE);
        } else if (e instanceof ConcurrentAccessException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_CONCURRENT_ACCESS_MESSAGE);
        } else if (e instanceof AccountException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_ACCOUNT_MESSAGE);
        } else if (e instanceof ExpiredCredentialsException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_EXPIRED_CREDENTIALS_MESSAGE);
        } else if (e instanceof CredentialsException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_CREDENTIALS_MESSAGE);
        } else if (e instanceof UnimplementedInterfaceException) {
            resultInfo = ResultUtil.createError(e.getMessage());
        } else if (e instanceof AuthenticationException) {
            resultInfo = ResultUtil.createFail(MessageConfig.EXCEPTION_AUTHENTICATION_MESSAGE);
        } else {
            e.printStackTrace();
            resultInfo = ResultUtil.createError(MessageConfig.EXCEPTION_DEFAULT_MESSAGE);
        }

        HttpOutputMessage outputMessage = new ServletServerHttpResponse((HttpServletResponse) response);
        try {
            jackson2HttpMessageConverter.write(new ExceptionView(resultInfo), MediaType.APPLICATION_JSON, outputMessage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onAjaxAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = null;
        String uri = req.getRequestURI();
        for (Map.Entry<String, DynamicRouteLocator.ZuulRoute> entry : DynamicRouteLocator.DB_ROUTE_MAP.entrySet()) {
            if (pathMatcher.match(entry.getKey(), uri)) {
                url = entry.getValue().getLoginUrl();
                break;
            }
        }
        if (StringUtil.isEmpty(url)) {
            url = ShiroConfig.LOGIN_PAGE_URL;
        }
        HttpOutputMessage outputMessage = new ServletServerHttpResponse((HttpServletResponse) response);
        Map<String, String> result = new HashMap<>(2);
        result.put("route_login_url", url);
        result.put("login_url", ShiroConfig.LOGIN_PAGE_URL);
        jackson2HttpMessageConverter.write(new ExceptionView(ResultUtil.createNeedLogin(MessageConfig.EXCEPTION_NEED_LOGIN_MESSAGE), result), MediaType.APPLICATION_JSON, outputMessage);
        return false;
    }

    private static AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean onAccessDenied(RequestHandler handler, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = null;
        String uri = req.getRequestURI();
        for (Map.Entry<String, DynamicRouteLocator.ZuulRoute> entry : DynamicRouteLocator.DB_ROUTE_MAP.entrySet()) {
            if (pathMatcher.match(entry.getKey(), uri)) {
                url = entry.getValue().getLoginUrl();
                break;
            }
        }
        if (StringUtil.isEmpty(url)) {
            url = ShiroConfig.LOGIN_PAGE_URL;
        }
        handler.redirectTo(url, null, true, request, response);
        return false;
    }
}
