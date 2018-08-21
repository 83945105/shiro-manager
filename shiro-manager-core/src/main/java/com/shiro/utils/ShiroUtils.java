package com.shiro.utils;

import com.alibaba.fastjson.JSONObject;
import com.shiro.norm.Certificate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by 白超 on 2018/6/14.
 */
public class ShiroUtils {

    /**
     * 证书请求头ID
     */
    public static final String CERTIFICATE_HEADER_ID = "X-AUTH-ID";

    /**
     * 角色集合请求头ID
     */
    public static final String ROLES_HEADER_ID = "X-ROLES-ID";

    /**
     * 获取证书(用户)
     *
     * @param request
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Certificate> T getCertificate(HttpServletRequest request, Class<T> clazz) {
        String c = request.getHeader(CERTIFICATE_HEADER_ID);
        if (c != null) {
            return JSONObject.parseObject(c, clazz);
        }
        return null;
    }

    /**
     * 获取角色
     *
     * @param request
     * @return
     */
    public static String[] getRoles(HttpServletRequest request) {
        String rs = request.getHeader(ROLES_HEADER_ID);
        if (rs != null) {
            return JSONObject.parseObject(rs, String[].class);
        }
        return new String[]{};
    }
}
