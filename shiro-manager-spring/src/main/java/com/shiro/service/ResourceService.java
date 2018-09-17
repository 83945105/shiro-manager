package com.shiro.service;

import com.shiro.model.JurResModel;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleResModel;
import com.shiro.bean.JurRes;
import com.shiro.bean.JurRoleRes;
import com.shiro.conf.Dict;
import com.shiro.entity.JurResGet;
import com.shiro.entity.JurResPost;
import com.shiro.entity.JurResPut;
import com.shiro.entity.JurRoleGet;
import com.shiro.utils.TableUtils;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author 白超
 * @date 2018/6/13
 */
@Component
public class ResourceService {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    private List<JurRoleRes> buildRoleResList(List<JurRoleGet> roleList, JurRes res, Consumer<JurRoleRes> formatter) {
        List<JurRoleRes> records = new ArrayList<>();
        JurRoleRes roleRes;
        for (JurRoleGet role : roleList) {
            roleRes = new JurRoleRes();

            formatter.accept(roleRes);

            roleRes.setRoleId(role.getId());
            roleRes.setRole(role.getRole());
            roleRes.setRoleName(role.getName());
            roleRes.setRoleType(role.getType());

            roleRes.setResId(res.getId());
            roleRes.setResName(res.getName());
            roleRes.setResUrl(res.getUrl());
            roleRes.setResType(res.getType());

            records.add(roleRes);
        }
        return records;
    }

    public List<JurResGet> newNodeResource(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resTableName = TableUtils.getResTableName(request);
        if (StringUtil.isEmpty(record.getName())) {
            ExceptionUtil.throwFailException("请输入资源名称");
        }
        if (!Dict.ResourceType.NODE.equalsTo(record.getType())) {
            ExceptionUtil.throwFailException("资源类型请选择节点类型");
        }

        record.setUrl("");
        record.setPermission("");
        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();
        if (StringUtil.isEmpty(record.getStatus())) {
            record.setStatus(Status.NORMAL.name());
        }
        record.setCreateTime(timeString);
        record.setCreateTimeStamp(timeStamp);

        String[] newParentIds = record.getNewParentIds();

        //没有父节点
        if (newParentIds == null || newParentIds.length == 0) {
            record.setId(UUID.randomUUID().toString());

            Long index = this.jdbcEngine.queryColumnOne(1, Long.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                    .functionColumn(FunctionColumnType.MAX, JurResModel.Column::index));
            record.setIndex(index == null ? 0 : ++index);
            int count = this.jdbcEngine.insertJavaBeanSelective(record, MySqlDynamicEngine.insert(resTableName, JurResModel.class));
            if (count != 1) {
                ExceptionUtil.throwFailException("新增资源失败");
            }
            return this.jdbcEngine.queryForList(JurResGet.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                    .leftJoin(resTableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                            .and(joinTable.parentId().equalTo(mainTable.id())))
                    .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                    .column(table -> table)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.id().equalTo(record.getId())))
                    .group(JurResModel.Group::id));
        }

        // 有父节点
        Map<String, JurRes> resMap = this.jdbcEngine.queryInMap(JurResModel.primaryKeyAlias, JurRes.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(newParentIds))));
        JurRes parentRecord;
        List<JurResPost> records = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        Long index = this.jdbcEngine.queryColumnOne(1, Long.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .functionColumn(FunctionColumnType.MAX, JurResModel.Column::index));
        index = index == null ? 0 : ++index;
        for (String newParentId : newParentIds) {

            JurResPost newRes = new JurResPost();
            BeanUtils.copyProperties(record, newRes);

            String id = UUID.randomUUID().toString();
            ids.add(id);

            newRes.setId(id);
            newRes.setParentId(newParentId);
            parentRecord = resMap.get(newParentId);
            if (parentRecord == null) {
                ExceptionUtil.throwFailException("指定的父节点不存在");
            }
            String parentIds = parentRecord.getParentIds();
            if (parentIds == null || parentIds.trim().length() == 0) {
                parentIds = "/" + newParentId;
            } else {
                parentIds = parentIds + "/" + newParentId;
            }
            newRes.setParentIds(parentIds);
            newRes.setIndex(index++);
            records.add(newRes);
        }
        int count = this.jdbcEngine.batchInsertJavaBeans(records, MySqlDynamicEngine.insert(resTableName, JurResModel.class));
        if (count != records.size()) {
            ExceptionUtil.throwFailException("新增资源失败");
        }
        return this.jdbcEngine.queryForList(JurResGet.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .leftJoin(resTableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().inS(ids)))
                .group(JurResModel.Group::id));

    }

    public List<JurResGet> newResource(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (Dict.ResourceType.NODE.equalsTo(record.getType())) {
            return this.newNodeResource(record, request, response);
        }
        String resTableName = TableUtils.getResTableName(request);
        String roleTableName = TableUtils.getRoleTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(record.getName())) {
            ExceptionUtil.throwFailException("请输入资源名称");
        }
        if (StringUtil.isEmpty(record.getType())) {
            ExceptionUtil.throwFailException("请选择资源类型");
        }
        if (Dict.ResourceType.URL.equalsTo(record.getType()) && StringUtil.isEmpty(record.getUrl())) {
            ExceptionUtil.throwFailException("请输入资源请求地址");
        }
        if (Dict.ResourceType.PERMISSION.equalsTo(record.getType()) && StringUtil.isEmpty(record.getPermission())) {
            ExceptionUtil.throwFailException("请输入资源许可");
        }

        String timeString = Time.localDateTimeNow();
        long timeStamp = Time.timeStamp();
        if (StringUtil.isEmpty(record.getStatus())) {
            record.setStatus(Status.NORMAL.name());
        }
        record.setCreateTime(timeString);
        record.setCreateTimeStamp(timeStamp);

        String[] newParentIds = record.getNewParentIds();
        String[] roleIds = record.getRoleIds();
        List<JurRoleRes> newRoleResList;
        List<JurRoleGet> roleList = null;
        if (roleIds != null && roleIds.length > 0) {
            roleList = this.jdbcEngine.queryForList(JurRoleGet.class, MySqlDynamicEngine.query(roleTableName, JurRoleModel.class)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.id().in(roleIds))));
            /*获取第三方角色信息开始*/
/*            DataView dataView = this.hRoleApi.getRoleListByIds(roleIds, request, response);
            JsonView jsonView = JsonViewUtil.success(dataView);
            roleList.addAll(jsonView.getRows(JurRoleGet.class, jurRoleGet -> {
                jurRoleGet.setType(Dict.RoleType.OTHER.name());
                return jurRoleGet;
            }))*/
            ;
            /*获取第三方角色信息结束*/
        }

        //没有父节点
        if (newParentIds == null || newParentIds.length == 0) {
            record.setId(UUID.randomUUID().toString());

            //新增角色资源数据
            if (roleList != null && roleList.size() > 0) {
                newRoleResList = this.buildRoleResList(roleList, record, roleRes -> {
                    roleRes.setCreateTime(timeString);
                    roleRes.setCreateTimeStamp(timeStamp);
                    roleRes.setStatus(Status.NORMAL.name());
                });

                if (newRoleResList != null && newRoleResList.size() > 0) {
                    int count = this.jdbcEngine.batchInsertJavaBeans(newRoleResList, MySqlDynamicEngine.insert(roleResTableName, JurRoleResModel.class));
                    if (count != newRoleResList.size()) {
                        ExceptionUtil.throwFailException("创建角色资源关系失败");
                    }
                }
            }

            Long index = this.jdbcEngine.queryColumnOne(1, Long.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                    .functionColumn(FunctionColumnType.MAX, JurResModel.Column::index));
            record.setIndex(index == null ? 0 : ++index);
            int count = this.jdbcEngine.insertJavaBeanSelective(record, MySqlDynamicEngine.insert(resTableName, JurResModel.class));
            if (count != 1) {
                ExceptionUtil.throwFailException("新增资源失败");
            }
            return this.jdbcEngine.queryForList(JurResGet.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                    .leftJoin(resTableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                            .and(joinTable.parentId().equalTo(mainTable.id())))
                    .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                    .column(table -> table)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.id().equalTo(record.getId())))
                    .group(JurResModel.Group::id));
        }


        // 有父节点
        Map<String, JurRes> resMap = this.jdbcEngine.queryInMap(JurResModel.primaryKeyAlias, JurRes.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(newParentIds))));
        JurRes parentRecord;
        List<JurResPost> records = new ArrayList<>();
        newRoleResList = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        Long index = this.jdbcEngine.queryColumnOne(1, Long.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .functionColumn(FunctionColumnType.MAX, JurResModel.Column::index));
        index = index == null ? 0 : ++index;
        for (String newParentId : newParentIds) {

            JurResPost newRes = new JurResPost();
            BeanUtils.copyProperties(record, newRes);

            String id = UUID.randomUUID().toString();
            ids.add(id);

            newRes.setId(id);
            newRes.setParentId(newParentId);
            parentRecord = resMap.get(newParentId);
            if (parentRecord == null) {
                ExceptionUtil.throwFailException("指定的父节点不存在");
            }
            String parentIds = parentRecord.getParentIds();
            if (parentIds == null || parentIds.trim().length() == 0) {
                parentIds = "/" + newParentId;
            } else {
                parentIds = parentIds + "/" + newParentId;
            }
            newRes.setParentIds(parentIds);
            newRes.setIndex(index++);
            records.add(newRes);

            //新增角色资源数据
            if (roleList != null && roleList.size() > 0) {
                newRoleResList.addAll(this.buildRoleResList(roleList, newRes, roleRes -> {
                    roleRes.setCreateTime(timeString);
                    roleRes.setCreateTimeStamp(timeStamp);
                    roleRes.setStatus(Status.NORMAL.name());
                }));
            }
        }
        if (newRoleResList.size() > 0) {
            int count = this.jdbcEngine.batchInsertJavaBeans(newRoleResList, MySqlDynamicEngine.insert(roleResTableName, JurRoleResModel.class));
            if (count != newRoleResList.size()) {
                ExceptionUtil.throwFailException("创建角色资源关系失败");
            }
        }
        int count = this.jdbcEngine.batchInsertJavaBeans(records, MySqlDynamicEngine.insert(resTableName, JurResModel.class));
        if (count != records.size()) {
            ExceptionUtil.throwFailException("新增资源失败");
        }
        return this.jdbcEngine.queryForList(JurResGet.class, MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .leftJoin(resTableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().inS(ids)))
                .group(JurResModel.Group::id));
    }

    public void updateResourceStatus(String primaryKey, String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        if (StringUtil.isEmpty(primaryKey)) {
            ExceptionUtil.throwErrorException("请指定资源主键");
        }
        if (!EnumMethods.contains(status, Status.values())) {
            ExceptionUtil.throwErrorException("状态不正确");
        }

        JurResPut record = new JurResPut();
        record.setId(primaryKey);
        record.setStatus(status);

        record.setUpdateTime(Time.localDateTimeNow());
        record.setUpdateTimeStamp(Time.timeStamp());

        int count = this.jdbcEngine.updateJavaBeanByPrimaryKeySelective(primaryKey, record, MySqlDynamicEngine.update(tableName, JurResModel.class));
        if (count != 1) {
            ExceptionUtil.throwFailException("更改资源状态失败");
        }
    }

    public void deleteResourceByIds(String[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleResTableName = TableUtils.getRoleResTableName(request);
        String resTableName = TableUtils.getResTableName(request);
        if (ids == null || ids.length == 0) {
            ExceptionUtil.throwFailException("没有可以删除的资源");
        }

        for (String id : ids) {
            String likeString = "%" + id + "%";
            //删除所有相关角色资源关系数据
            this.jdbcEngine.delete(MySqlDynamicEngine.delete(roleResTableName, JurRoleResModel.class)
                    .innerJoin(resTableName, JurResModel.class, (on, joinTable, mainTable) -> on
                            .and(joinTable.id().equalTo(mainTable.resId())))
                    .where(JurResModel.class, (condition, table, mainTable) -> condition
                            .and(table.parentIds().like(likeString))));
            //删除所有相关资源
            this.jdbcEngine.delete(MySqlDynamicEngine.delete(resTableName, JurResModel.class)
                    .where((condition, mainTable) -> condition
                            .and(mainTable.parentIds().like(likeString))));
        }

        //删除关系数据
        this.jdbcEngine.delete(MySqlDynamicEngine.delete(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().in(ids))));

        //删除所有资源
        this.jdbcEngine.delete(MySqlDynamicEngine.delete(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.id().in(ids))));

    }

    public void putResourceById(String id, JurResPut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resTableName = TableUtils.getResTableName(request);
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (id == null || id.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        int count = this.jdbcEngine.queryCount(MySqlDynamicEngine.query(resTableName, JurResModel.class)
                .where((condition, mainTable) -> condition.and(mainTable.id().equalTo(id))));
        if (count != 1) {
            ExceptionUtil.throwFailException("没有可以修改的资源");
        }
        JurRoleRes roleRes = new JurRoleRes();
        JurRes res = new JurRes();
        if (!StringUtil.isEmpty(record.getName())) {
            res.setName(record.getName());
            roleRes.setResName(record.getName());
        }
        if (!StringUtil.isEmpty(record.getType())) {
            res.setType(record.getType());
            roleRes.setResType(record.getType());
        }
        if (!StringUtil.isEmpty(record.getUrl())) {
            res.setUrl(record.getUrl());
            roleRes.setResUrl(record.getUrl());
        }
        if (!StringUtil.isEmpty(record.getPermission())) {
            res.setPermission(record.getPermission());
        }
        String dateString = Time.localDateTimeNow();
        Long dateTimeStamp = Time.timeStamp();
        res.setUpdateTime(dateString);
        res.setUpdateTimeStamp(dateTimeStamp);
        roleRes.setUpdateTime(dateString);
        roleRes.setUpdateTimeStamp(dateTimeStamp);

        //更新相关资源
        this.jdbcEngine.updateJavaBeanSelective(roleRes, MySqlDynamicEngine.update(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().equalTo(id))));

        //更新资源
        count = this.jdbcEngine.updateJavaBeanByPrimaryKeySelective(id, res, MySqlDynamicEngine.update(resTableName, JurResModel.class));
        if (count != 1) {
            ExceptionUtil.throwFailException("更新资源失败");
        }
    }
}
