package com.shiro.service;

import com.avalon.holygrail.enums.Status;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.utils.DateUtil;
import com.avalon.holygrail.utils.StringUtil;
import com.dt.core.bean.FunctionColumnType;
import com.shiro.model.*;
import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.bean.ZuulRoute;
import com.shiro.entity.ZuulRoutePost;
import com.shiro.entity.ZuulRoutePut;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/11
 */
@Component
public class ModuleService {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    public boolean isServiceIdExist(String serviceId) {
        return this.jdbcEngine.queryCount(MySqlEngine.main(ZuulRouteModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.serviceId().equalTo(serviceId)))) > 0;
    }

    public boolean isModuleIdExist(String id) {
        return this.jdbcEngine.queryCount(MySqlEngine.main(ZuulRouteModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().equalTo(id)))) > 0;
    }

    public void newModule(ZuulRoutePost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtil.isEmpty(record.getId())) {
            ExceptionUtil.throwFailException("未设置模块ID");
        }
        if (isModuleIdExist(record.getId())) {
            ExceptionUtil.throwFailException("该模块ID已存在");
        }
        if (StringUtil.isEmpty(record.getServiceId())) {
            ExceptionUtil.throwFailException("请输入服务ID");
        }
        if (isServiceIdExist(record.getServiceId())) {
            ExceptionUtil.throwFailException("该服务ID已存在");
        }
        if (StringUtil.isEmpty(record.getServiceName())) {
            ExceptionUtil.throwFailException("请输入服务名称");
        }
        if (StringUtil.isEmpty(record.getPath())) {
            ExceptionUtil.throwFailException("请输入根路径");
        }
        if (StringUtil.isEmpty(record.getPathPrefix())) {
            ExceptionUtil.throwFailException("未指定根路径前缀");
        }
        if (StringUtil.isEmpty(record.getPathSuffix())) {
            ExceptionUtil.throwFailException("未指定根路径后缀");
        }
        if (StringUtil.isEmpty(record.getServiceIdSuffix())) {
            ExceptionUtil.throwFailException("未指定服务ID后缀");
        }
        if (StringUtil.isEmpty(record.getStatus())) {
            record.setStatus(Status.NORMAL.name());
        }

        Long index = this.jdbcEngine.queryOne(Long.class, MySqlEngine.main(ZuulRouteModel.class)
                .functionColumn(FunctionColumnType.MAX, ZuulRouteModel.Column::index));
        record.setIndex(index == null ? 0 : ++index);
        record.setCreateTime(DateUtil.getTimeString());
        record.setCreateTimeStamp(DateUtil.getTimeStamp());

        //创建表
        String resTableName = TableUtils.getResTableName(record.getId());
        String roleTableName = TableUtils.getRoleTableName(record.getId());
        String roleResTableName = TableUtils.getRoleResTableName(record.getId());
        String roleUserTableName = TableUtils.getRoleUserTableName(record.getId());

        if (this.jdbcEngine.isTableExist(resTableName)) {
            ExceptionUtil.throwFailException("资源表 [" + resTableName + "] 已存在, 无法创建");
        }
        if (this.jdbcEngine.isTableExist(roleTableName)) {
            ExceptionUtil.throwFailException("角色表 [" + roleTableName + "] 已存在, 无法创建");
        }
        if (this.jdbcEngine.isTableExist(roleResTableName)) {
            ExceptionUtil.throwFailException("角色资源表 [" + roleResTableName + "] 已存在, 无法创建");
        }
        if (this.jdbcEngine.isTableExist(roleUserTableName)) {
            ExceptionUtil.throwFailException("角色用户表 [" + roleUserTableName + "] 已存在, 无法创建");
        }

        this.jdbcEngine.copyTable(JurResModel.tableName, resTableName);
        this.jdbcEngine.copyTable(JurRoleModel.tableName, roleTableName);
        this.jdbcEngine.copyTable(JurRoleResModel.tableName, roleResTableName);
        this.jdbcEngine.copyTable(JurRoleUserModel.tableName, roleUserTableName);

        int count = this.jdbcEngine.insertRecordSelective(record, ZuulRouteModel.class);
        if (count != 1) {
            ExceptionUtil.throwFailException("新增模块失败");
        }
    }

    public void editModule(Object primaryKeyValue, ZuulRoutePut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtil.isEmpty(primaryKeyValue)) {
            ExceptionUtil.throwFailException("未指定主键值");
        }
        ZuulRoute route = this.jdbcEngine.queryByPrimaryKey(primaryKeyValue, ZuulRoute.class, MySqlEngine.column(ZuulRouteModel.class).column(ZuulRouteModel.Column::id));
        if (route == null) {
            ExceptionUtil.throwFailException("该模块记录不存在");
        }
        route = new ZuulRoute();
        route.setId((String) primaryKeyValue);
        if (!StringUtil.isEmpty(record.getServiceName())) {
            route.setServiceName(record.getServiceName());
        }
        if (!StringUtil.isEmpty(record.getStatus())) {
            route.setStatus(record.getStatus());
        }

        route.setUpdateTime(DateUtil.getTimeString());
        route.setUpdateTimeStamp(DateUtil.getTimeStamp());

        int count = this.jdbcEngine.updateRecordByPrimaryKeySelective(primaryKeyValue, route, ZuulRouteModel.class);
        if (count != 1) {
            ExceptionUtil.throwFailException("更新模块失败");
        }
    }

    public void deleteModule(Object primaryKeyValue) throws Exception {
        if (StringUtil.isEmpty(primaryKeyValue)) {
            ExceptionUtil.throwFailException("未指定主键值");
        }
        ZuulRoute route = this.jdbcEngine.queryByPrimaryKey(primaryKeyValue, ZuulRoute.class, MySqlEngine.column(ZuulRouteModel.class).column(ZuulRouteModel.Column::id));
        if (route == null) {
            ExceptionUtil.throwFailException("该模块记录不存在");
        }

        //逻辑删除表
        String resTableName = TableUtils.getResTableName((String) primaryKeyValue);
        String roleTableName = TableUtils.getRoleTableName((String) primaryKeyValue);
        String roleResTableName = TableUtils.getRoleResTableName((String) primaryKeyValue);
        String roleUserTableName = TableUtils.getRoleUserTableName((String) primaryKeyValue);

        String resTableNameBak = TableUtils.getRenameTableName(resTableName);
        String roleTableNameBak = TableUtils.getRenameTableName(roleTableName);
        String roleResTableNameBak = TableUtils.getRenameTableName(roleResTableName);
        String roleUserTableNameBak = TableUtils.getRenameTableName(roleUserTableName);

        this.jdbcEngine.renameTable(resTableName, resTableNameBak);
        this.jdbcEngine.renameTable(roleTableName, roleTableNameBak);
        this.jdbcEngine.renameTable(roleResTableName, roleResTableNameBak);
        this.jdbcEngine.renameTable(roleUserTableName, roleUserTableNameBak);

        int count = this.jdbcEngine.deleteByPrimaryKey(primaryKeyValue, ZuulRouteModel.class);
        if (count != 1) {
            ExceptionUtil.throwFailException("删除模块失败");
        }
    }

}
