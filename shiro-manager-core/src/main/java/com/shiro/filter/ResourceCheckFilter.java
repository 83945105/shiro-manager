package com.shiro.filter;

import com.shiro.modules.Module;
import com.shiro.modules.RequestAuthenticationModule;
import com.shiro.modules.ShiroModules;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 资源请求校验器
 */
public class ResourceCheckFilter extends AccessControlFilter {

    private RequestAuthenticationModule requestAuthenticationModule = new RequestAuthenticationModule.DefaultRequestAuthenticationModule();

    private ShiroModules modules = new ShiroModules();

    public void setModules(ShiroModules modules) {
        this.modules = modules;
        Module module = this.modules.getModule(RequestAuthenticationModule.class);
        if (module != null) {
            this.requestAuthenticationModule = (RequestAuthenticationModule) module;
        }
    }

    //重写该方法使用自定义权限认证,也就是走UrlPermission来验证是否有访问权限
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String url = getPathWithinApplication(request);
        return subject.isPermitted(url);
    }

    //进入此方法说明报错
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return this.requestAuthenticationModule.onAccessDenied(request, response);
    }

    public RequestAuthenticationModule getRequestAuthenticationModule() {
        return requestAuthenticationModule;
    }

    public void setRequestAuthenticationModule(RequestAuthenticationModule requestAuthenticationModule) {
        this.requestAuthenticationModule = requestAuthenticationModule;
    }
}
