package com.shiro.utils;

import com.shiro.model.JurResModel;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleResModel;
import com.shiro.model.JurRoleUserModel;
import pub.avalon.beans.Time;
import pub.avalon.holygrail.response.utils.ExceptionUtil;
import pub.avalon.holygrail.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/11
 */
public class TableUtils {

    public static final String RES_TABLE_NAME = JurResModel.tableName;

    public static final String RES_TABLE_NAME_REGEX = "^(.*?)" + RES_TABLE_NAME + "$";

    public static final String ROLE_TABLE_NAME = JurRoleModel.tableName;

    public static final String ROLE_TABLE_NAME_REGEX = "^(.*?)" + ROLE_TABLE_NAME + "$";

    public static final String ROLE_RES_TABLE_NAME = JurRoleResModel.tableName;

    public static final String ROLE_RES_TABLE_NAME_REGEX = "^(.*?)" + ROLE_RES_TABLE_NAME + "$";

    public static final String ROLE_USER_TABLE_NAME = JurRoleUserModel.tableName;

    public static final String ROLE_USER_TABLE_NAME_REGEX = "^(.*?)" + ROLE_USER_TABLE_NAME + "$";

    public static final String ROOT_RES_TABLE_NAME = "root_" + RES_TABLE_NAME;

    public static final String ROOT_ROLE_TABLE_NAME = "root_" + ROLE_TABLE_NAME;

    public static final String ROOT_ROLE_RES_TABLE_NAME = "root_" + ROLE_RES_TABLE_NAME;

    public static final String ROOT_ROLE_USER_TABLE_NAME = "root_" + ROLE_USER_TABLE_NAME;

    public static String getResTableName(String moduleId) {
        return moduleId + "_" + RES_TABLE_NAME;
    }

    public static String getRoleTableName(String moduleId) {
        return moduleId + "_" + ROLE_TABLE_NAME;
    }

    public static String getRoleResTableName(String moduleId) {
        return moduleId + "_" + ROLE_RES_TABLE_NAME;
    }

    public static String getRoleUserTableName(String moduleId) {
        return moduleId + "_" + ROLE_USER_TABLE_NAME;
    }

    public static String getRenameTableName(String tableName) {
        return tableName + "_bak_" + Time.localDateNow().replaceAll("-", "");
    }

    public static String getResTableName(HttpServletRequest request) throws Exception {
        String moduleId = request.getParameter("moduleId");
        if (StringUtil.isEmpty(moduleId)) {
            ExceptionUtil.throwFailException("未设置模块ID");
        }
        return TableUtils.getResTableName(moduleId);
    }

    public static String getRoleTableName(HttpServletRequest request) throws Exception {
        String moduleId = request.getParameter("moduleId");
        if (StringUtil.isEmpty(moduleId)) {
            ExceptionUtil.throwFailException("未设置模块ID");
        }
        return TableUtils.getRoleTableName(moduleId);
    }

    public static String getRoleResTableName(HttpServletRequest request) throws Exception {
        String moduleId = request.getParameter("moduleId");
        if (StringUtil.isEmpty(moduleId)) {
            ExceptionUtil.throwFailException("未设置模块ID");
        }
        return TableUtils.getRoleResTableName(moduleId);
    }

    public static String getRoleUserTableName(HttpServletRequest request) throws Exception {
        String moduleId = request.getParameter("moduleId");
        if (StringUtil.isEmpty(moduleId)) {
            ExceptionUtil.throwFailException("未设置模块ID");
        }
        return TableUtils.getRoleUserTableName(moduleId);
    }

}
