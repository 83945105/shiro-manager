package com.shiro.controller;

import com.avalon.holygrail.ss.util.DataViewUtil;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.avalon.holygrail.utils.StringUtil;
import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.api.RoleResourceApi;
import com.shiro.entity.JurRoleResGet;
import com.shiro.model.JurRoleResModel;
import com.shiro.service.RoleResourceService;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 白超 on 2018/7/23.
 */
@RestController
@RequestMapping("${feign.role-resource-api-service-path:/api-shiro-role-resource}")
public class RoleResourceController implements RoleResourceApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;
    @Autowired
    private RoleResourceService roleResourceService;

    @Override
    @RequestMapping(value = "/post/grantResourcesToRoles", method = RequestMethod.POST)
    public DataView postGrantResourcesToRoles(String[] resourceIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleResourceService.grantResourcesToRoles(resourceIds, roleIds, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

    @Override
    @RequestMapping(value = "/post/unGrantResourcesToRoles", method = RequestMethod.POST)
    public DataView postUnGrantResourcesToRoles(String[] resourceIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleResourceService.unGrantResourcesToRoles(resourceIds, roleIds, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

    @Override
    @RequestMapping(value = "/get/roleResourceListByRoleId", method = RequestMethod.GET)
    public DataView getRoleResourceListByRoleId(String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(roleId)) {
            ExceptionUtil.throwFailException("未指定角色ID");
        }
        List<JurRoleResGet> list = this.jdbcEngine.queryForList(JurRoleResGet.class, MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId))));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/get/roleResourceListByResourceId", method = RequestMethod.GET)
    public DataView getRoleResourceListByResourceId(String resourceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleResTableName = TableUtils.getRoleResTableName(request);
        if (StringUtil.isEmpty(resourceId)) {
            ExceptionUtil.throwFailException("未指定资源ID");
        }
        List<JurRoleResGet> list = this.jdbcEngine.queryForList(JurRoleResGet.class, MySqlEngine.main(roleResTableName, JurRoleResModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.resId().equalTo(resourceId))));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/post/grantResourceToRole", method = RequestMethod.POST)
    public DataView postGrantResourceToRole(String resourceId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleResourceService.grantResourceToRole(resourceId, roleId, request, response);
        return DataViewUtil.getMessageViewSuccess("授权成功");
    }

    @Override
    @RequestMapping(value = "/post/unGrantResourceToRole", method = RequestMethod.POST)
    public DataView postUnGrantResourceToRole(String resourceId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleResourceService.unGrantResourceToRole(resourceId, roleId, request, response);
        return DataViewUtil.getMessageViewSuccess("取消授权成功");
    }

    @Override
    @RequestMapping(value = "/post/grantResourcesInNodeToRole", method = RequestMethod.POST)
    public DataView postGrantResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> ids = this.roleResourceService.grantResourcesInNodeToRole(nodeId, roleId, request, response);
        return DataViewUtil.getModelViewSuccess(ids);
    }

    @Override
    @RequestMapping(value = "/post/unGrantResourcesInNodeToRole", method = RequestMethod.POST)
    public DataView postUnGrantResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> ids = this.roleResourceService.unGrantResourcesInNodeToRole(nodeId, roleId, request, response);
        return DataViewUtil.getModelViewSuccess(ids);
    }

    @Override
    @RequestMapping(value = "/post/grantAllChildResourcesInNodeToRole", method = RequestMethod.POST)
    public DataView postGrantAllChildResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> ids = this.roleResourceService.grantAllChildResourcesInNodeToRole(nodeId, roleId, request, response);
        return DataViewUtil.getModelViewSuccess(ids);
    }

    @Override
    @RequestMapping(value = "/post/unGrantAllChildResourcesInNodeToRole", method = RequestMethod.POST)
    public DataView postUnGrantAllChildResourcesInNodeToRole(String nodeId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> ids = this.roleResourceService.unGrantAllChildResourcesInNodeToRole(nodeId, roleId, request, response);
        return DataViewUtil.getModelViewSuccess(ids);
    }

}
