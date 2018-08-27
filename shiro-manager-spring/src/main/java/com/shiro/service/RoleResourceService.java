package com.shiro.service;

import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.bean.JurRes;
import com.shiro.bean.JurRole;
import com.shiro.bean.JurRoleRes;
import com.shiro.conf.Dict;
import com.shiro.entity.JurResGet;
import com.shiro.entity.JurRoleGet;
import com.shiro.entity.JurRoleResGet;
import com.shiro.model.JurResModel;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleResModel;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.avalon.beans.Time;
import pub.avalon.holygrail.response.beans.Status;
import pub.avalon.holygrail.response.utils.ExceptionUtil;
import pub.avalon.holygrail.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/23.
 */
@Component
public class RoleResourceService {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    public void grantResourcesToRoles(String[] resourceIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resTableName = TableUtils.getResTableName(request);
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (resourceIds == null || resourceIds.length == 0) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .leftJoin(roleTableName, JurRoleModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.roleId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().in(resourceIds))
                        .and(JurRoleModel.class, (condition1, table, mainTable1) -> condition1
                                .and(table.status().equalTo(Status.NORMAL.name()))
                                .or(mainTable.roleType().equalTo(Dict.RoleType.OTHER.name())))));
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        Map<String, JurResGet> resMap = this.jdbcEngine.queryForListInMap(JurResModel.primaryKeyAlias, JurResGet.class, MySqlEngine.main(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(resourceIds))));
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

        List<JurRoleRes> records = new ArrayList<>();
        JurRoleRes record;
        JurRole role;
        JurRes res;
        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();
        for (Map.Entry<String, JurRoleGet> roleEntry : roleMap.entrySet()) {
            role = roleEntry.getValue();
            for (Map.Entry<String, JurResGet> resEntry : resMap.entrySet()) {
                res = resEntry.getValue();

                record = new JurRoleRes();
                record.setCreateTime(timeString);
                record.setCreateTimeStamp(timeStamp);
                record.setStatus(Status.NORMAL.name());

                record.setRoleId(role.getId());
                record.setRole(role.getRole());
                record.setRoleName(role.getName());
                record.setRoleType(role.getType());

                record.setResId(res.getId());
                record.setResName(res.getName());
                record.setResUrl(res.getUrl());
                record.setResType(res.getType());

                records.add(record);
            }
        }

        int count = this.jdbcEngine.batchInsertRecords(records, roleResTableName, JurRoleResModel.class);
        if (count != records.size()) {
            ExceptionUtil.throwFailException("操作失败");
        }

    }

    public void unGrantResourcesToRoles(String[] resourceIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (resourceIds == null || resourceIds.length == 0) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .leftJoin(roleTableName, JurRoleModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.roleId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().in(resourceIds))
                        .and(JurRoleModel.class, (condition1, table, mainTable1) -> condition1
                                .and(table.status().equalTo(Status.NORMAL.name()))
                                .or(mainTable.roleType().equalTo(Dict.RoleType.OTHER.name())))));
    }

    public void grantResourceToRole(String resourceId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resTableName = TableUtils.getResTableName(request);
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (resourceId == null || resourceId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        if (roleId == null || roleId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }

        JurRoleResGet roleRes = this.jdbcEngine.queryOne(JurRoleResGet.class, MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().equalTo(resourceId)
                                .roleId().equalTo(roleId))));

        if (roleRes != null) {
            ExceptionUtil.throwFailException("当前角色已经拥有该资源,无法再次授予");
        }

        JurResGet res = this.jdbcEngine.queryByPrimaryKey(resourceId, JurResGet.class, MySqlEngine.column(resTableName, JurResModel.class));
        if (res == null) {
            ExceptionUtil.throwFailException("您要授予的资源不存在");
        }

        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
            role.setType(Dict.RoleType.OTHER.name());
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("指定的角色不存在");
        }

        roleRes = new JurRoleResGet();
        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();

        roleRes.setCreateTime(timeString);
        roleRes.setCreateTimeStamp(timeStamp);
        roleRes.setStatus(Status.NORMAL.name());

        roleRes.setRoleId(role.getId());
        roleRes.setRole(role.getRole());
        roleRes.setRoleName(role.getName());
        roleRes.setRoleType(role.getType());

        roleRes.setResId(res.getId());
        roleRes.setResName(res.getName());
        roleRes.setResUrl(res.getUrl());
        roleRes.setResType(res.getType());

        int count = this.jdbcEngine.insertRecordSelective(roleRes, roleResTableName, JurRoleResModel.class);
        if (count != 1) {
            ExceptionUtil.throwFailException("操作失败");
        }

    }

    public void unGrantResourceToRole(String resourceId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (resourceId == null || resourceId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        if (roleId == null || roleId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }

        int count = this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().equalTo(resourceId)
                                .roleId().equalTo(roleId))));
        if (count != 1) {
            ExceptionUtil.throwFailException("取消授权失败");
        }
    }

    public List<String> grantResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String resTableName = TableUtils.getResTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(nodeId)) {
            ExceptionUtil.throwFailException("未指定节点ID");
        }
        if (StringUtil.isEmpty(roleId)) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        JurResGet res = this.jdbcEngine.queryByPrimaryKey(nodeId, JurResGet.class, MySqlEngine.column(resTableName, JurResModel.class));
        if (res == null) {
            ExceptionUtil.throwFailException("您指定的节点不存在");
        }
        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("您指定的角色不存在");
        }
        List<JurResGet> resources = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().equalTo(nodeId)
                                .type().in(new String[]{Dict.ResourceType.URL.name(), Dict.ResourceType.PERMISSION.name()}))));
        if (resources.size() == 0) {
            ExceptionUtil.throwFailException("该节点下没有可授予的资源");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .innerJoin(resTableName, JurResModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.resId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId)))
                .where(JurResModel.class, (condition, table, mainTable) -> condition
                        .and(table.parentId().equalTo(nodeId))));
        List<JurRoleRes> records = new ArrayList<>();
        JurRoleRes record;
        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();
        List<String> ids = new ArrayList<>();
        for (JurResGet resource : resources) {

            ids.add(resource.getId());

            record = new JurRoleRes();

            record.setCreateTime(timeString);
            record.setCreateTimeStamp(timeStamp);
            record.setStatus(Status.NORMAL.name());

            record.setRoleId(role.getId());
            record.setRole(role.getRole());
            record.setRoleName(role.getName());
            record.setRoleType(role.getType());

            record.setResId(resource.getId());
            record.setResName(resource.getName());
            record.setResUrl(resource.getUrl());
            record.setResType(resource.getType());

            records.add(record);
        }

        int count = this.jdbcEngine.batchInsertRecords(records, roleResTableName, JurRoleResModel.class);
        if (count != records.size()) {
            ExceptionUtil.throwFailException("操作失败");
        }
        return ids;
    }

    public List<String> unGrantResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String resTableName = TableUtils.getResTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(nodeId)) {
            ExceptionUtil.throwFailException("未指定节点ID");
        }
        if (StringUtil.isEmpty(roleId)) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        JurResGet res = this.jdbcEngine.queryByPrimaryKey(nodeId, JurResGet.class, MySqlEngine.column(resTableName, JurResModel.class));
        if (res == null) {
            ExceptionUtil.throwFailException("您指定的节点不存在");
        }
        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("您指定的角色不存在");
        }
        List<JurResGet> resources = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().equalTo(nodeId))));
        if (resources.size() == 0) {
            ExceptionUtil.throwFailException("该节点下没有可取消授予的资源");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .innerJoin(resTableName, JurResModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.resId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId)))
                .where(JurResModel.class, (condition, table, mainTable) -> condition
                        .and(table.parentId().equalTo(nodeId))));
        List<String> ids = new ArrayList<>();
        for (JurResGet resource : resources) {
            ids.add(resource.getId());
        }
        return ids;
    }

    public List<String> grantAllChildResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String resTableName = TableUtils.getResTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(nodeId)) {
            ExceptionUtil.throwFailException("未指定节点ID");
        }
        if (StringUtil.isEmpty(roleId)) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        JurResGet res = this.jdbcEngine.queryByPrimaryKey(nodeId, JurResGet.class, MySqlEngine.column(resTableName, JurResModel.class));
        if (res == null) {
            ExceptionUtil.throwFailException("您指定的节点不存在");
        }
        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("您指定的角色不存在");
        }
        String likeText = "%" + nodeId + "%";
        List<JurResGet> resources = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentIds().like(likeText)
                                .type().in(new String[]{Dict.ResourceType.URL.name(), Dict.ResourceType.PERMISSION.name()}))));
        if (resources.size() == 0) {
            ExceptionUtil.throwFailException("该节点下没有可授予的资源");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .innerJoin(resTableName, JurResModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.resId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId)))
                .where(JurResModel.class, (condition, table, mainTable) -> condition
                        .and(table.parentIds().like(likeText))));
        List<JurRoleRes> records = new ArrayList<>();
        JurRoleRes record;
        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();
        List<String> ids = new ArrayList<>();
        for (JurResGet resource : resources) {

            ids.add(resource.getId());

            record = new JurRoleRes();

            record.setCreateTime(timeString);
            record.setCreateTimeStamp(timeStamp);
            record.setStatus(Status.NORMAL.name());

            record.setRoleId(role.getId());
            record.setRole(role.getRole());
            record.setRoleName(role.getName());
            record.setRoleType(role.getType());

            record.setResId(resource.getId());
            record.setResName(resource.getName());
            record.setResUrl(resource.getUrl());
            record.setResType(resource.getType());

            records.add(record);
        }

        int count = this.jdbcEngine.batchInsertRecords(records, roleResTableName, JurRoleResModel.class);
        if (count != records.size()) {
            ExceptionUtil.throwFailException("操作失败");
        }
        return ids;
    }

    public List<String> unGrantAllChildResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String resTableName = TableUtils.getResTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(nodeId)) {
            ExceptionUtil.throwFailException("未指定节点ID");
        }
        if (StringUtil.isEmpty(roleId)) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        JurResGet res = this.jdbcEngine.queryByPrimaryKey(nodeId, JurResGet.class, MySqlEngine.column(resTableName, JurResModel.class));
        if (res == null) {
            ExceptionUtil.throwFailException("您指定的节点不存在");
        }
        JurRoleGet role = this.jdbcEngine.queryByPrimaryKey(roleId, JurRoleGet.class, MySqlEngine.column(roleTableName, JurRoleModel.class));
/*        if (role == null) {
            role = JsonViewUtil.success(this.hRoleApi.getRole(roleId, request, response)).getRecord(JurRoleGet.class);
        }*/
        if (role == null) {
            ExceptionUtil.throwFailException("您指定的角色不存在");
        }
        String likeText = "%" + nodeId + "%";
        List<JurResGet> resources = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentIds().like(likeText)
                                .type().in(new String[]{Dict.ResourceType.URL.name(), Dict.ResourceType.PERMISSION.name()}))));
        if (resources.size() == 0) {
            ExceptionUtil.throwFailException("该节点下没有可授予的资源");
        }
        this.jdbcEngine.delete(MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .innerJoin(resTableName, JurResModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.resId())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId)))
                .where(JurResModel.class, (condition, table, mainTable) -> condition
                        .and(table.parentIds().like(likeText))));
        List<String> ids = new ArrayList<>();
        for (JurResGet resource : resources) {
            ids.add(resource.getId());
        }
        return ids;
    }
}
