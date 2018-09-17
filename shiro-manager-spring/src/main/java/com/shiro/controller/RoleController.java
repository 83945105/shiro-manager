package com.shiro.controller;

import com.shiro.entity.JurRolePut;
import com.shiro.model.JurRoleModel;
import com.shiro.model.JurRoleResModel;
import com.shiro.api.RoleApi;
import com.shiro.entity.JurResPost;
import com.shiro.entity.JurRoleGet;
import com.shiro.entity.JurRolePost;
import com.shiro.service.RoleService;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pub.avalon.holygrail.response.utils.DataViewUtil;
import pub.avalon.holygrail.response.views.DataView;
import pub.avalon.holygrail.utils.StringUtil;
import pub.avalon.sqlhelper.factory.MySqlDynamicEngine;
import pub.avalon.sqlhelper.spring.beans.PageResultForBean;
import pub.avalon.sqlhelper.spring.core.SpringJdbcEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 白超
 * @version 1.0
 * @see
 * @since 2018/7/12
 */
@RestController
@RequestMapping("${feign.role-api-service-path:/api-shiro-role}")
public class RoleController implements RoleApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;
    @Autowired
    private RoleService roleService;

    @Override
    @RequestMapping(value = "/post/validateRole", method = RequestMethod.POST)
    public DataView postValidateRole(String role, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtil.isEmpty(role)) {
            return DataViewUtil.getModelViewFail("请输入角色标识符");
        }
        if (this.roleService.isRoleExist(role, request, response)) {
            return DataViewUtil.getModelViewFail("该角色标识符已存在");
        }
        return DataViewUtil.getModelViewSuccess("该角色标识符可以使用");
    }

    @Override
    @RequestMapping(value = "/post/newRole", method = RequestMethod.POST)
    public DataView postNewRole(JurRolePost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleService.newRole(record, request, response);
        return DataViewUtil.getMessageViewSuccess("新增角色成功");
    }

    @Override
    @RequestMapping(value = "/put/roleStatus/{id}", method = RequestMethod.PUT)
    public DataView putRoleStatus(@PathVariable String id, String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleService.updateRoleStatus(id, status, request, response);
        return DataViewUtil.getMessageViewSuccess("更新角色状态成功");
    }

    @Override
    @RequestMapping(value = "/get/rolePageList", method = RequestMethod.GET)
    public DataView getRolePageList(JurRolePost record, Integer currentPage, Integer pageSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getRoleTableName(request);
        currentPage = currentPage == null ? 1 : currentPage;
        pageSize = pageSize == null ? 10 : pageSize;
        PageResultForBean<JurRoleGet> pageResult = this.jdbcEngine.pageQueryForList(JurRoleGet.class, currentPage, pageSize, MySqlDynamicEngine.query(tableName, JurRoleModel.class)
                .sort(table -> table.index().asc()));
        return DataViewUtil.getModelViewSuccess(
                pageResult.getResult(), pageResult.getLimit());
    }

    @Override
    @RequestMapping(value = "/get/roleList", method = RequestMethod.GET)
    public DataView getRoleList(JurRolePost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String tableName = TableUtils.getRoleTableName(request);
        String likeText = (record.getSearchText() == null || "".equals(record.getSearchText())) ? null : "%" + record.getSearchText().trim() + "%";
        List<JurRoleGet> list = this.jdbcEngine.queryForList(JurRoleGet.class, MySqlDynamicEngine.query(tableName, JurRoleModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.name().like(likeText))
                        .or(mainTable.role().like(likeText)))
                .sort(table -> table.index().asc()));
        /*获取第三方角色开始*/
/*        try {
            DataView dataView = this.hRoleApi.getRoleList(request, response);
            JsonView jsonView = JsonViewUtil.success(dataView);
            List<JurRoleGet> list2 = jsonView.getRows(JurRoleGet.class, jurRoleGet -> {
                jurRoleGet.setType(Dict.RoleType.OTHER.name());
                jurRoleGet.setCanEdit(false);
                jurRoleGet.setCanChangeStatus(false);
                jurRoleGet.setStatus(Status.NORMAL.name());
                return jurRoleGet;
            });
            list.addAll(list2);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*获取第三方角色结束*/
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/get/rolesForResource", method = RequestMethod.GET)
    public DataView getRolesForResource(JurResPost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleTableName = TableUtils.getRoleTableName(request);
        String resTableName = TableUtils.getRoleResTableName(request);
        List<JurRoleGet> list = this.jdbcEngine.queryForList(JurRoleGet.class, MySqlDynamicEngine.query(roleTableName, JurRoleModel.class)
                .innerJoin(resTableName, JurRoleResModel.class, (on, joinTable, mainTable) -> on
                        .and(joinTable.roleId().equalTo(mainTable.id()))
                        .and(joinTable.resId().equalTo(record.getId()))));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/delete/roleList", method = RequestMethod.DELETE)
    public DataView deleteRoleList(String[] ids, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleService.deleteResourceByIds(ids, request, response);
        return DataViewUtil.getMessageViewSuccess("删除角色成功");
    }

    @Override
    @RequestMapping(value = "/put/role/{id}", method = RequestMethod.PUT)
    public DataView putRole(@PathVariable String id, JurRolePut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.roleService.putRoleById(id, record, request, response);
        return DataViewUtil.getMessageViewSuccess("更新角色成功");
    }
}
