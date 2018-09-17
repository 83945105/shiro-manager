package com.shiro.service;

import com.shiro.bean.JurRole;
import com.shiro.bean.JurRoleRes;
import com.shiro.bean.JurRoleUser;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleResModel;
import com.shiro.model.JurRoleUserModel;
import com.shiro.entity.JurRolePost;
import com.shiro.entity.JurRolePut;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.avalon.beans.EnumMethods;
import pub.avalon.beans.Time;
import pub.avalon.holygrail.response.beans.Status;
import pub.avalon.holygrail.response.utils.ExceptionUtil;
import pub.avalon.holygrail.utils.StringUtil;
import pub.avalon.sqlhelper.core.beans.FunctionColumnType;
import pub.avalon.sqlhelper.factory.MySqlDynamicEngine;
import pub.avalon.sqlhelper.spring.core.SpringJdbcEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/12
 */
@Component
public class RoleService {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    public boolean isRoleExist(String role, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getRoleTableName(request);
        return this.jdbcEngine.queryCount(MySqlDynamicEngine.query(tableName, JurRoleModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.role().equalTo(role)))) > 0;
    }

    public void newRole(JurRolePost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getRoleTableName(request);
        if (StringUtil.isEmpty(record.getName())) {
            ExceptionUtil.throwFailException("请输入角色名称");
        }
        if (StringUtil.isEmpty(record.getRole())) {
            ExceptionUtil.throwFailException("请输入角色标识符");
        }
        if (this.isRoleExist(record.getRole(), request, response)) {
            ExceptionUtil.throwFailException("角色标识符已存在");
        }
        if (StringUtil.isEmpty(record.getType())) {
            ExceptionUtil.throwFailException("请选择角色类型");
        }

        if (StringUtil.isEmpty(record.getStatus())) {
            record.setStatus(Status.NORMAL.name());
        }

        record.setId(record.getRole());
        record.setCreateTime(Time.localDateTimeNow());
        record.setCreateTimeStamp(Time.timeStamp());

        Long index = this.jdbcEngine.queryColumnOne(1, Long.class, MySqlDynamicEngine.query(tableName, JurRoleModel.class)
                .functionColumn(FunctionColumnType.MAX, JurRoleModel.Column::index));
        record.setIndex(index == null ? 0 : ++index);
        int count = this.jdbcEngine.insertJavaBeanSelective(record, MySqlDynamicEngine.insert(tableName, JurRoleModel.class));
        if (count != 1) {
            ExceptionUtil.throwFailException("新增角色失败");
        }
    }

    public void updateRoleStatus(String primaryKey, String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getRoleTableName(request);
        if (StringUtil.isEmpty(primaryKey)) {
            ExceptionUtil.throwFailException("请指定角色主键");
        }
        if (!EnumMethods.contains(status, Status.values())) {
            ExceptionUtil.throwFailException("状态不正确");
        }

        JurRolePut record = new JurRolePut();
        record.setId(primaryKey);
        record.setStatus(status);

        record.setUpdateTime(Time.localDateTimeNow());
        record.setUpdateTimeStamp(Time.timeStamp());

        int count = this.jdbcEngine.updateJavaBeanByPrimaryKeySelective(primaryKey, record, MySqlDynamicEngine.update(tableName, JurRoleModel.class));
        if (count != 1) {
            ExceptionUtil.throwFailException("更改角色状态失败");
        }
    }

    public void deleteResourceByIds(String[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleResTableName = TableUtils.getRoleResTableName(request);
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        String roleTableName = TableUtils.getRoleTableName(request);
        if (ids == null || ids.length == 0) {
            ExceptionUtil.throwFailException("没有可以删除的角色");
        }

        //删除角色资源数据
        this.jdbcEngine.delete(MySqlDynamicEngine.delete(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().in(ids))));

        //删除角色用户数据
        this.jdbcEngine.delete(MySqlDynamicEngine.delete(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().in(ids))));

        //删除角色
        this.jdbcEngine.delete(MySqlDynamicEngine.delete(roleTableName, JurRoleModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(ids))));
    }

    public void putRoleById(String id, JurRolePut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        if (id == null || id.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        int count = this.jdbcEngine.queryCount(MySqlDynamicEngine.query(roleTableName, JurRoleModel.class)
                .where((condition, mainTable) -> condition.and(mainTable.id().equalTo(id))));
        if (count != 1) {
            ExceptionUtil.throwFailException("没有可以修改的角色");
        }
        JurRoleRes roleRes = new JurRoleRes();
        JurRoleUser roleUser = new JurRoleUser();
        JurRole role = new JurRole();
        if (!StringUtil.isEmpty(record.getName())) {
            role.setName(record.getName());
            roleRes.setRoleName(record.getName());
            roleUser.setRoleName(record.getName());
        }
        if (!StringUtil.isEmpty(record.getType())) {
            role.setType(record.getType());
            roleRes.setRoleType(record.getType());
            roleUser.setRoleType(record.getType());
        }
        String dateString = Time.localDateTimeNow();
        Long dateTimeStamp = Time.timeStamp();
        role.setUpdateTime(dateString);
        role.setUpdateTimeStamp(dateTimeStamp);
        roleRes.setUpdateTime(dateString);
        roleRes.setUpdateTimeStamp(dateTimeStamp);
        roleUser.setUpdateTime(dateString);
        roleUser.setUpdateTimeStamp(dateTimeStamp);

        //更新相关资源
        this.jdbcEngine.updateJavaBeanSelective(roleRes, MySqlDynamicEngine.update(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(id))));

        this.jdbcEngine.updateJavaBeanSelective(roleUser, MySqlDynamicEngine.update(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(id))));

        //更新角色
        count = this.jdbcEngine.updateJavaBeanByPrimaryKeySelective(id, role, MySqlDynamicEngine.update(roleTableName, JurRoleModel.class));
        if (count != 1) {
            ExceptionUtil.throwFailException("更新角色失败");
        }
    }

}
