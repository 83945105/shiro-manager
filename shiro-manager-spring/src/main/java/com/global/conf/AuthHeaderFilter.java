package com.global.conf;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.shiro.modules.SecurityUtilsModule;
import com.shiro.norm.Certificate;
import com.shiro.utils.ShiroUtils;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用户过滤器
 * Created by 白超 on 2018/6/14.
 */
@Component
public class AuthHeaderFilter extends ZuulFilter {

    @Autowired
    SecurityUtilsModule securityUtilsModule;
    @Autowired
    AuthorizingRealm realm;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        Certificate certificate = null;
        try {
            certificate = this.securityUtilsModule.getCurrentCertificate(requestContext.getRequest(), requestContext.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (certificate != null) {
            requestContext.addZuulRequestHeader(ShiroUtils.CERTIFICATE_HEADER_ID, JSONObject.toJSONString(certificate, SerializerFeature.BrowserCompatible));
        }
        Collection<String> roles = this.securityUtilsModule.getCurrentRoles(realm);
        if (roles.size() > 0) {
            requestContext.addZuulRequestHeader(ShiroUtils.ROLES_HEADER_ID, JSONObject.toJSONString(roles, SerializerFeature.BrowserCompatible));
        }
        return null;
    }
}
