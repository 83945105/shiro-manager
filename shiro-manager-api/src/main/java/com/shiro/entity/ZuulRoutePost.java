package com.shiro.entity;

import com.shiro.bean.ZuulRoute;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/11
 */
public class ZuulRoutePost extends ZuulRoute {

    private String serviceIdSuffix;

    private String pathPrefix;

    private String pathSuffix;

    public String getServiceIdSuffix() {
        return serviceIdSuffix;
    }

    public void setServiceIdSuffix(String serviceIdSuffix) {
        this.serviceIdSuffix = serviceIdSuffix;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getPathSuffix() {
        return pathSuffix;
    }

    public void setPathSuffix(String pathSuffix) {
        this.pathSuffix = pathSuffix;
    }
}
