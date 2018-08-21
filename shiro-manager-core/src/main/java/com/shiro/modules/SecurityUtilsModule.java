package com.shiro.modules;

import com.shiro.norm.Certificate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;

/**
 * 安全相关模块
 * Created by 白超 on 2018/6/11.
 */
public interface SecurityUtilsModule extends Module {

    /**
     * 获取当前Session
     *
     * @param request
     * @param response
     * @return
     */
    Session getCurrentSession(HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据sessionId获取Session
     *
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    Session getSession(String sessionId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取当前证书
     *
     * @param request
     * @param response
     * @return
     */
    Certificate getCurrentCertificate(HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据sessionId获取证书
     *
     * @param sessionId
     * @param request
     * @param response
     * @return
     */
    Certificate getCertificate(String sessionId, HttpServletRequest request, HttpServletResponse response);

    Collection<String> getCurrentRoles(AuthorizingRealm realm);

    void logout(HttpServletRequest request, HttpServletResponse response);

    class DefaultSecurityUtilsModule implements SecurityUtilsModule {

        @Override
        public Session getCurrentSession(HttpServletRequest request, HttpServletResponse response) {
            Session session = SecurityUtils.getSubject().getSession();
            return session;
        }

        @Override
        public Session getSession(String sessionId, HttpServletRequest request, HttpServletResponse response) {
            SessionKey key = new WebSessionKey(sessionId, request, response);
            Session session = SecurityUtils.getSecurityManager().getSession(key);
            return session;
        }

        @Override
        public Certificate getCurrentCertificate(HttpServletRequest request, HttpServletResponse response) {
            Certificate certificate = (Certificate) SecurityUtils.getSubject().getPrincipal();
            return certificate;
        }

        @Override
        public Certificate getCertificate(String sessionId, HttpServletRequest request, HttpServletResponse response) {
            Session session = this.getSession(sessionId, request, response);
            if (session == null) {
                return null;
            }
            SimplePrincipalCollection coll = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (coll == null) {
                return null;
            }
            return (Certificate) coll.getPrimaryPrincipal();
        }

        @Override
        public Collection<String> getCurrentRoles(AuthorizingRealm realm) {
            if (realm == null) {
                return new HashSet<>();
            }
            AuthorizationInfo authorizationInfo = realm.getAuthorizationCache().get(SecurityUtils.getSubject().getPrincipals());
            if (authorizationInfo == null) {
                return new HashSet<>();
            }
            return authorizationInfo.getRoles();
        }

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response) {
            SecurityUtils.getSubject().logout();
        }
    }
}
