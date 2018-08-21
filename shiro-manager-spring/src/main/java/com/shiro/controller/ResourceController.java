package com.shiro.controller;

import com.avalon.holygrail.ss.util.DataViewUtil;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.dt.jdbc.bean.PageResultForBean;
import com.shiro.entity.JurResPut;
import com.shiro.model.JurResModel;
import com.dt.core.bean.FunctionColumnType;
import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.api.ResourceApi;
import com.shiro.entity.JurResGet;
import com.shiro.entity.JurResPost;
import com.shiro.service.ResourceService;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源控制器
 * Created by 白超 on 2018/6/13.
 */
@RestController
@RequestMapping("${feign.resource-api-service-path:/api-shiro-resource}")
public class ResourceController implements ResourceApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;
    @Autowired
    private ResourceService resourceService;

    @Override
    @RequestMapping(value = "/post/newResource", method = RequestMethod.POST)
    public DataView postNewResource(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<JurResGet> records = this.resourceService.newResource(record, request, response);
        return DataViewUtil.getModelViewSuccess(records);
    }

    @Override
    @RequestMapping(value = "/put/resourceStatus/{id}", method = RequestMethod.PUT)
    public DataView putResourceStatus(@PathVariable("id") String id, String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.resourceService.updateResourceStatus(id, status, request, response);
        return DataViewUtil.getMessageViewSuccess("更新资源状态成功");
    }

    @Override
    @RequestMapping(value = "/get/resourcePageList", method = RequestMethod.GET)
    public DataView getResourcePageList(JurResPost record, Integer currentPage, Integer pageSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        currentPage = currentPage == null ? 1 : currentPage;
        pageSize = pageSize == null ? 10 : pageSize;
        String[] parentIds = record.getNewParentIds();
        String likeText = (record.getSearchText() == null || "".equals(record.getSearchText())) ? null : "%" + record.getSearchText().trim() + "%";

        PageResultForBean<JurResGet> pageResult = this.jdbcEngine.pageQueryForList(JurResGet.class, currentPage, pageSize, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .leftJoin(tableName, JurResModel.class, "JurResParent", (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.parentId())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(JurResModel.class, "JurResParent", table -> table.name("parentResourceName"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().in(parentIds))
                        .and(mainTable.name().like(likeText))
                        .and(mainTable.type().in(record.getSearchTypes())))
                .group(JurResModel.Group::id)
                .sort(JurResModel.class, "JurResLeft", table -> table.index().asc())
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(
                pageResult.getResult(),
                pageResult.getPagination().getTotal(),
                pageResult.getPagination().getCurrentPage(),
                pageResult.getPagination().getPageSize());
    }

    @Override
    @RequestMapping(value = "/get/resourceList", method = RequestMethod.GET)
    public DataView getResourceList(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        List<JurResGet> list = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.type().in(record.getSearchTypes())))
                .group(JurResModel.Group::id)
                .sort(JurResModel.class, "JurResLeft", table -> table.index().asc())
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/get/rootResourcePageList", method = RequestMethod.GET)
    public DataView getRootResourcePageList(JurResPost record, Integer currentPage, Integer pageSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        currentPage = currentPage == null ? 1 : currentPage;
        pageSize = pageSize == null ? 10 : pageSize;
        String likeText = (record.getSearchText() == null || "".equals(record.getSearchText())) ? null : "%" + record.getSearchText().trim() + "%";
        PageResultForBean<JurResGet> pageResult = this.jdbcEngine.pageQueryForList(JurResGet.class, currentPage, pageSize, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.name().like(likeText))
                        .and(mainTable.type().in(record.getSearchTypes())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().isNull())
                        .or(mainTable.parentId().equalTo("")))
                .group(JurResModel.Group::id)
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(
                pageResult.getResult(),
                pageResult.getPagination().getTotal(),
                pageResult.getPagination().getCurrentPage(),
                pageResult.getPagination().getPageSize());
    }

    @Override
    @RequestMapping(value = "/get/rootResourceList", method = RequestMethod.GET)
    public DataView getRootResourceList(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        String likeText = (record.getSearchText() == null || "".equals(record.getSearchText())) ? null : "%" + record.getSearchText().trim() + "%";
        List<JurResGet> list = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.name().like(likeText))
                        .and(mainTable.type().in(record.getSearchTypes())))
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().isNull())
                        .or(mainTable.parentId().equalTo("")))
                .group(JurResModel.Group::id)
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/get/childResourceList", method = RequestMethod.GET)
    public DataView getChildResourceList(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        String parentId = record.getParentId();
        if (parentId == null || "".equals(parentId.trim())) {
            ExceptionUtil.throwFailException("未指定父节点ID");
        }
        List<JurResGet> list = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResLeft", (on, joinTable, mainTable) -> on
                        .and(joinTable.parentId().equalTo(mainTable.id())))
                .functionColumn(JurResModel.class, "JurResLeft", FunctionColumnType.COUNT, table -> table.id("childCount"))
                .column(table -> table)
                .where((condition, mainTable) -> condition
                        .and(mainTable.parentId().equalTo(parentId))
                        .and(mainTable.type().in(record.getSearchTypes())))
                .group(JurResModel.Group::id)
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/delete/resourceList", method = RequestMethod.DELETE)
    public DataView deleteResourceList(String[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.resourceService.deleteResourceByIds(ids, request, response);
        return DataViewUtil.getMessageViewSuccess("删除资源成功");
    }

    @Override
    @RequestMapping(value = "/put/resource/{id}", method = RequestMethod.PUT)
    public DataView putResource(@PathVariable String id, JurResPut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.resourceService.putResourceById(id, record, request, response);
        return DataViewUtil.getMessageViewSuccess("更新资源成功");
    }

    @Override
    @RequestMapping(value = "/get/locationResourceList", method = RequestMethod.GET)
    public DataView getLocationResourceList(String[] rootIds, JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getResTableName(request);
        String likeText = (record.getSearchText() == null || "".equals(record.getSearchText())) ? null : "%" + record.getSearchText().trim() + "%";
        List<JurResGet> list = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(tableName, JurResModel.class)
                .leftJoin(tableName, JurResModel.class, "JurResParent", (on, joinTable, mainTable) -> on
                        .and(joinTable.id().equalTo(mainTable.parentId())))
                .column(JurResModel.class, "JurResParent", table -> table.name("parentResourceName"))
                .column(table -> table.id().name().parentIds())
                .where((condition, mainTable) -> condition
                        .and(mainTable.name().like(likeText))
                        .and(mainTable.type().in(record.getSearchTypes())))
                .group(JurResModel.Group::id)
                .sort(table -> table.index().asc()));
        //过滤掉不包含根节点ID的资源
        List<JurResGet> records = new ArrayList<>();
        for (JurResGet res : list) {
            for (String id : rootIds) {
                if (res.getParentIds().contains(id)) {
                    records.add(res);
                    break;
                }
            }
        }
        return DataViewUtil.getModelViewSuccess(records);
    }

}
