package com.shiro.controller;

import com.avalon.holygrail.ss.util.DataViewUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.api.RoleUserApi;
import com.shiro.entity.JurRoleUserGet;
import com.shiro.model.JurRoleUserModel;
import com.shiro.service.RoleUserService;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 白超 on 2018/7/25.
 */
@RestController
@RequestMapping("${feign.role-user-api-service-path:/api-shiro-role-user}")
public class RoleUserController implements RoleUserApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;
    @Autowired
    private RoleUserService roleUserService;

    @Override
    @RequestMapping(value = "/get/roleUserListByUserId/{userId}", method = RequestMethod.GET)
    public DataView getRoleUserListByUserId(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<JurRoleUserGet> list = this.jdbcEngine.queryForList(JurRoleUserGet.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().equalTo(userId))));
        /*获取第三方数据开始*/
/*        DataView dataView = this.hRoleUserApi.getRoleUserListByUserId(userId, request, response);
        JsonView jsonView = JsonViewUtil.success(dataView);
        List<JurRoleUserGet> list1 = jsonView.getRows(JurRoleUserGet.class, jurRoleUserGet -> {
            jurRoleUserGet.setType(Dict.RoleUserType.OTHER.name());
            return jurRoleUserGet;
        });
        list.addAll(list1);*/
        /*获取第三方数据结束*/
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/get/roleUserListByRoleId/{roleId}", method = RequestMethod.GET)
    public DataView getRoleUserListByRoleId(@PathVariable String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<JurRoleUserGet> list = this.jdbcEngine.queryForList(JurRoleUserGet.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId))));
        /*获取第三方数据开始*/
/*        DataView dataView = this.hRoleUserApi.getRoleUserListByRoleId(roleId, request, response);
        JsonView jsonView = JsonViewUtil.success(dataView);
        List<JurRoleUserGet> list1 = jsonView.getRows(JurRoleUserGet.class, jurRoleUserGet -> {
            jurRoleUserGet.setType(Dict.RoleUserType.OTHER.name());
            return jurRoleUserGet;
        });
        list.addAll(list1);*/
        /*获取第三方数据结束*/
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/post/grantRolesToUsers", method = RequestMethod.POST)
    public DataView postGrantRolesToUsers(String[] userIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleUserService.grantRolesToUsers(userIds, roleIds, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

    @Override
    @RequestMapping(value = "/post/unGrantRolesToUsers", method = RequestMethod.POST)
    public DataView postUnGrantRolesToUsers(String[] userIds, String[] roleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleUserService.unGrantRolesToUsers(userIds, roleIds, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

    @Override
    @RequestMapping(value = "/post/grantRoleToUser", method = RequestMethod.POST)
    public DataView postGrantRoleToUser(String userId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleUserService.grantRoleToUser(userId, roleId, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

    @Override
    @RequestMapping(value = "/post/unGrantRoleToUser", method = RequestMethod.POST)
    public DataView postUnGrantRoleToUser(String userId, String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleUserService.unGrantRoleToUser(userId, roleId, request, response);
        return DataViewUtil.getMessageViewSuccess("操作成功");
    }

}
