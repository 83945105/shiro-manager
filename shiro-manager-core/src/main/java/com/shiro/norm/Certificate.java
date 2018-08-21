package com.shiro.norm;

import java.io.Serializable;

/**
 * 证书
 * Created by 白超 on 2018/1/29.
 */
public interface Certificate extends Serializable {

    /**
     * 获得盐
     */
    Object getSalt();

    /**
     * 获取用户名,该字段用于缓存Key,如果要清除用户缓存该字段必须指定正确
     *
     * @return
     */
    Object getUsername();
}
