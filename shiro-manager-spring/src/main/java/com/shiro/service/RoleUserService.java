package com.shiro.service;

import com.avalon.holygrail.enums.Status;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.utils.DateUtil;
import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.bean.JurRole;
import com.shiro.bean.JurRoleUser;
import com.shiro.conf.Dict;
import com.shiro.entity.JurRoleGet;
import com.shiro.entity.JurRoleUserGet;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleUserModel;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/25.
 */
@Component
public class RoleUserService {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    public void grantRolesToUsers(String[] userIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        if (userIds == null || userIds.length == 0) {
            ExceptionUtil.throwFailException("未指定用户ID");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .leftJoin(roleTableName, JurRoleModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.roleId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().in(userIds))
                        .and(JurRoleModel.class, (condition1, table, mainTable1) -> condition1
                                .and(table.status().equalTo(Status.NORMAL.name()))
                                .or(mainTable.roleType().equalTo(Dict.RoleType.OTHER.name())))));
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        Map<String, JurRoleGet> roleMap = this.jdbcEngine.queryForListInMap(JurRoleModel.primaryKeyAlias, JurRoleGet.class, MySqlEngine.main(roleTableName, JurRoleModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(roleIds))));
        /*获取第三方角色信息开始*/
/*        DataView dataView = this.hRoleApi.getRoleListByIds(roleIds, request, response);
        JsonView jsonView = JsonViewUtil.success(dataView);
        jsonView.getRows(JurRoleGet.class, jurRoleGet -> {
            jurRoleGet.setType(Dict.RoleType.OTHER.name());
            return jurRoleGet;
        }).forEach(r -> roleMap.put(r.getId(), r));*/
        /*获取第三方角色信息结束*/

        List<JurRoleUser> records = new ArrayList<>();
        JurRoleUser record;
        JurRole role;
        String timeString = DateUtil.getTimeString();
        long timeStamp = DateUtil.getTimeStamp();
        for (Map.Entry<String, JurRoleGet> roleEntry : roleMap.entrySet()) {
            role = roleEntry.getValue();
            for (String userId : userIds) {

                record = new JurRoleUser();
                record.setCreateTime(timeString);
                record.setCreateTimeStamp(timeStamp);
                record.setStatus(Status.NORMAL.name());

                record.setRoleId(role.getId());
                record.setRole(role.getRole());
                record.setRoleName(role.getName());
                record.setRoleType(role.getType());

                record.setUserId(userId);

                records.add(record);
            }
        }

        int count = this.jdbcEngine.batchInsertRecords(records, roleUserTableName, JurRoleUserModel.class);
        if (count != records.size()) {
            ExceptionUtil.throwFailException("操作失败");
        }

    }

    public void unGrantRolesToUsers(String[] userIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        if (userIds == null || userIds.length == 0) {
            ExceptionUtil.throwFailException("未指定用户ID");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .leftJoin(roleTableName, JurRoleModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.roleId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().in(userIds))
                        .and(JurRoleModel.class, (condition1, table, mainTable1) -> condition1
                                .and(table.status().equalTo(Status.NORMAL.name()))//删除正常状态角色相关数据
                                .or(mainTable.roleType().equalTo(Dict.RoleType.OTHER.name())))));//删除其它类型角色相关数据
    }

    public void grantRoleToUser(String userId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        if (userId == null || userId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定用户ID");
        }
        if (roleId == null || roleId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }

        JurRoleUserGet ru = this.jdbcEngine.queryOne(JurRoleUserGet.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().equalTo(userId)
                                .roleId().equalTo(roleId))));

        if (ru != null) {
            ExceptionUtil.throwFailException("当前用户已经拥有该角色,无法再次授予");
        }

        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("指定的角色不存在");
        }

        JurRoleUser roleUser = new JurRoleUser();
        String timeString = DateUtil.getTimeString();
        long timeStamp = DateUtil.getTimeStamp();

        roleUser.setCreateTime(timeString);
        roleUser.setCreateTimeStamp(timeStamp);
        roleUser.setStatus(Status.NORMAL.name());

        roleUser.setRoleId(role.getId());
        roleUser.setRole(role.getRole());
        roleUser.setRoleName(role.getName());
        roleUser.setRoleType(role.getType());

        roleUser.setUserId(userId);

        int count = this.jdbcEngine.insertRecordSelective(roleUser, roleUserTableName, JurRoleUserModel.class);
        if (count != 1) {
            ExceptionUtil.throwFailException("操作失败");
        }
    }

    public void unGrantRoleToUser(String userId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        if (userId == null || userId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定用户ID");
        }
        if (roleId == null || roleId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }

        int count = this.jdbcEngine.delete(MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().equalTo(userId)
                                .roleId().equalTo(roleId))));
        if (count != 1) {
            ExceptionUtil.throwFailException("取消授权失败");
        }
    }
}
