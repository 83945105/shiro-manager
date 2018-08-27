package com.shiro.controller;

import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.global.conf.YmlConfig;
import com.shiro.api.CommonApi;
import com.shiro.entity.JurRoleGet;
import com.shiro.model.JurRoleUserModel;
import com.shiro.model.ZuulRouteModel;
import com.shiro.utils.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pub.avalon.holygrail.response.utils.DataViewUtil;
import pub.avalon.holygrail.response.utils.ExceptionUtil;
import pub.avalon.holygrail.response.views.DataView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 白超 on 2018/8/1.
 */
@RestController
@RequestMapping("${feign.common-api-service-path:/api-shiro-common}")
public class CommonController implements CommonApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    @Override
    @RequestMapping(value = "/get/basePath", method = RequestMethod.GET)
    public DataView getBasePath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String basePath = request.getScheme() + "://" + request.getRemoteAddr() + ":" + request.getServerPort()
                + request.getContextPath() + "/";

        return DataViewUtil.getModelViewSuccess(basePath);
    }

    @Override
    @RequestMapping(value = "/get/staticBasePath", method = RequestMethod.GET)
    public DataView getStaticBasePath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String basePath = request.getScheme() + "://" + request.getRemoteAddr() + ":" + request.getServerPort()
                + request.getContextPath() + "/" + YmlConfig.STATIC_PATH_PREFIX + "/";

        return DataViewUtil.getModelViewSuccess(basePath);
    }

    @Override
    @RequestMapping(value = "/get/moduleBasePath", method = RequestMethod.GET)
    public DataView getModuleBasePath(String serviceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (serviceId == null || serviceId.trim().length() == 0) {
            ExceptionUtil.throwFailException("未指定模块服务ID");
        }
        String path = this.jdbcEngine.queryOne(String.class, MySqlEngine.main(ZuulRouteModel.class)
                .column(ZuulRouteModel.Column::path)
                .where((condition, mainTable) -> condition
                        .and(mainTable.serviceId().equalTo(serviceId))));
        if (path == null) {
            ExceptionUtil.throwFailException("模块不存在");
        }
        String basePath = request.getScheme() + "://" + request.getRemoteAddr() + ":" + request.getServerPort()
                + request.getContextPath() + "/" + path.replace("**", "") + "/";

        return DataViewUtil.getModelViewSuccess(basePath);
    }

    @Override
    @RequestMapping(value = "/get/userIdsByRoleId", method = RequestMethod.GET)
    public DataView getUserIdsByRoleId(String roleId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<String> userIds = this.jdbcEngine.queryForList(String.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.roleId().equalTo(roleId))));
        return DataViewUtil.getModelViewSuccess(userIds);
    }

    @Override
    @RequestMapping(value = "/get/userIdsByRoleRole", method = RequestMethod.GET)
    public DataView getUserIdsByRoleRole(String roleRole, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<String> userIds = this.jdbcEngine.queryForList(String.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.role().equalTo(roleRole))));
        return DataViewUtil.getModelViewSuccess(userIds);
    }

    @Override
    @RequestMapping(value = "/get/roleRolesByUserId", method = RequestMethod.GET)
    public DataView getRoleRolesByUserId(String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<String> roleRoles = this.jdbcEngine.queryForList(String.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().equalTo(userId))));
        return DataViewUtil.getModelViewSuccess(roleRoles);
    }

    @Override
    @RequestMapping(value = "/get/roleListByUserId", method = RequestMethod.GET)
    public DataView getRoleListByUserId(String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleUserTableName = TableUtils.getRoleUserTableName(request);
        List<JurRoleGet> roleList = this.jdbcEngine.queryForList(JurRoleGet.class, MySqlEngine.main(roleUserTableName, JurRoleUserModel.class)
                .where((condition, mainTable) -> condition
                        .and(mainTable.userId().equalTo(userId))));
        return DataViewUtil.getModelViewSuccess(roleList);
    }

}
