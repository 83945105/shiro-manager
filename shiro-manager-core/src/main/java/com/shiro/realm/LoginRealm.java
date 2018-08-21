package com.shiro.realm;

import com.shiro.modules.LoginAuthenticationModule;
import com.shiro.modules.Module;
import com.shiro.modules.ShiroModules;
import com.shiro.norm.Certificate;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Set;

/**
 * 用于用户登录授权的Realm
 */
public class LoginRealm extends AuthorizingRealm {

    private LoginAuthenticationModule<Certificate> loginAuthenticationModule = new LoginAuthenticationModule.DefaultLoginAuthenticationModule();

    private ShiroModules modules = new ShiroModules();

    public void setModules(ShiroModules modules) {
        this.modules = modules;
        Module module = this.modules.getModule(LoginAuthenticationModule.class);
        if (module != null) {
            this.loginAuthenticationModule = (LoginAuthenticationModule<Certificate>) module;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Certificate certificate = (Certificate) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = this.loginAuthenticationModule.getRoles(certificate);
        Set<String> urls = this.loginAuthenticationModule.getUrls(certificate, roles);
        info.setRoles(roles);
        info.setStringPermissions(urls);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        String password = new String((char[]) token.getCredentials());
        Certificate certificate = this.loginAuthenticationModule.login(username, password);
        if (certificate == null) {
            throw new IncorrectCredentialsException();
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(certificate,
                password, this.getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(certificate.getSalt()));
        return info;
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        Certificate certificate = (Certificate) principals.getPrimaryPrincipal();
        SimplePrincipalCollection spc = new SimplePrincipalCollection(
                certificate.getUsername(), this.getName());
        super.clearCachedAuthenticationInfo(spc);
    }

    public LoginAuthenticationModule<Certificate> getLoginAuthenticationModule() {
        return loginAuthenticationModule;
    }

    public void setLoginAuthenticationModule(LoginAuthenticationModule<Certificate> loginAuthenticationModule) {
        this.loginAuthenticationModule = loginAuthenticationModule;
    }
}
