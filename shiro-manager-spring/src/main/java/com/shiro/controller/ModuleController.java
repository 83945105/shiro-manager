package com.shiro.controller;

import com.shiro.api.ModuleApi;
import com.shiro.bean.ZuulRoute;
import com.shiro.entity.ZuulRouteGet;
import com.shiro.entity.ZuulRoutePost;
import com.shiro.entity.ZuulRoutePut;
import com.shiro.model.ZuulRouteModel;
import com.shiro.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pub.avalon.holygrail.response.utils.DataViewUtil;
import pub.avalon.holygrail.response.utils.ExceptionUtil;
import pub.avalon.holygrail.response.views.DataView;
import pub.avalon.holygrail.utils.StringUtil;
import pub.avalon.sqlhelper.factory.MySqlDynamicEngine;
import pub.avalon.sqlhelper.spring.core.SpringJdbcEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 模块接口
 *
 * @author 白超
 * @version 1.0
 * @since 2018/7/11
 */
@RestController
@RequestMapping("${feign.module-api-service-path:/api-shiro-module}")
public class ModuleController implements ModuleApi {

    @Autowired
    private SpringJdbcEngine jdbcEngine;
    @Autowired
    private ModuleService moduleService;

    @Override
    @RequestMapping(value = "/get/module/{id}", method = RequestMethod.GET)
    public DataView getModule(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ZuulRoute zuulRoute = this.jdbcEngine.queryByPrimaryKey(id, ZuulRoute.class, MySqlDynamicEngine.query(ZuulRouteModel.class));
        if (zuulRoute == null) {
            ExceptionUtil.throwFailException("当前模块不存在");
        }
        return DataViewUtil.getModelViewSuccess(zuulRoute);
    }

    @Override
    @RequestMapping(value = "/post/validateServiceId", method = RequestMethod.POST)
    public DataView postValidateServiceId(String serviceId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtil.isEmpty(serviceId)) {
            return DataViewUtil.getModelViewFail("请输入服务ID");
        }
        if ("shiro-service".equalsIgnoreCase(serviceId)) {
            return DataViewUtil.getModelViewFail("该服务ID不可用");
        }
        if (this.moduleService.isServiceIdExist(serviceId)) {
            return DataViewUtil.getModelViewFail("该服务ID已存在");
        }
        return DataViewUtil.getModelViewSuccess("该服务ID可以使用");
    }

    @Override
    @RequestMapping(value = "/get/moduleList", method = RequestMethod.GET)
    public DataView getModuleList(ZuulRouteGet record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ZuulRoute> list = this.jdbcEngine.queryForList(ZuulRoute.class, MySqlDynamicEngine.query(ZuulRouteModel.class)
                .sort(table -> table.createTimeStamp().asc()));
        return DataViewUtil.getModelViewSuccess(list);
    }

    @Override
    @RequestMapping(value = "/post/module", method = RequestMethod.POST)
    public DataView postModule(ZuulRoutePost record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.moduleService.newModule(record, request, response);
        return DataViewUtil.getMessageViewSuccess("新建模块成功");
    }

    @Override
    @RequestMapping(value = "/put/module/{id}", method = RequestMethod.PUT)
    public DataView putModule(@PathVariable String id, ZuulRoutePut record, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.moduleService.editModule(id, record, request, response);
        return DataViewUtil.getMessageViewSuccess("更新模块成功");
    }

    @Override
    @RequestMapping(value = "/delete/module/{id}")
    public DataView deleteModule(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.moduleService.deleteModule(id);
        return DataViewUtil.getMessageViewSuccess("删除模块成功");
    }

}
