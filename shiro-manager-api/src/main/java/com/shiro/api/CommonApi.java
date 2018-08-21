package com.shiro.api;

import com.avalon.holygrail.ss.view.DataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/8/1.
 */
@FeignClient(name = "${feign.common-api-service-name:shiro-service}", path = "${feign.common-api-service-path:/api-shiro-common}")
public interface CommonApi {

    @RequestMapping(value = "/get/basePath", method = RequestMethod.GET)
    DataView getBasePath(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/staticBasePath", method = RequestMethod.GET)
    DataView getStaticBasePath(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/moduleBasePath", method = RequestMethod.GET)
    DataView getModuleBasePath(@RequestParam("serviceId") String serviceId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/userIdsByRoleId", method = RequestMethod.GET)
    DataView getUserIdsByRoleId(@RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/userIdsByRoleRole", method = RequestMethod.GET)
    DataView getUserIdsByRoleRole(@RequestParam("roleRole") String roleRole, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleRolesByUserId", method = RequestMethod.GET)
    DataView getRoleRolesByUserId(@RequestParam("userId") String userId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleListByUserId", method = RequestMethod.GET)
    DataView getRoleListByUserId(@RequestParam("userId") String userId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;


}
