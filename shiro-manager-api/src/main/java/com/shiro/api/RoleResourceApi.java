package com.shiro.api;

import com.avalon.holygrail.ss.view.DataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/23.
 */
@FeignClient(name = "${feign.role-resource-api-service-name:shiro-service}", path = "${feign.role-resource-api-service-path:/api-shiro-role-resource}")
public interface RoleResourceApi {

    @RequestMapping(value = "/post/grantResourcesToRoles", method = RequestMethod.POST)
    DataView postGrantResourcesToRoles(@RequestParam("resourceIds") String[] resourceIds, @RequestParam("roleIds") String[] roleIds, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantResourcesToRoles", method = RequestMethod.POST)
    DataView postUnGrantResourcesToRoles(@RequestParam("resourceIds") String[] resourceIds, @RequestParam("roleIds") String[] roleIds, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleResourceListByRoleId", method = RequestMethod.GET)
    DataView getRoleResourceListByRoleId(@RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleResourceListByResourceId", method = RequestMethod.GET)
    DataView getRoleResourceListByResourceId(@RequestParam("resourceId") String resourceId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/grantResourceToRole", method = RequestMethod.POST)
    DataView postGrantResourceToRole(@RequestParam("resourceId") String resourceId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantResourceToRole", method = RequestMethod.POST)
    DataView postUnGrantResourceToRole(@RequestParam("resourceId") String resourceId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/grantResourcesInNodeToRole", method = RequestMethod.POST)
    DataView postGrantResourcesInNodeToRole(@RequestParam("nodeId") String nodeId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantResourcesInNodeToRole", method = RequestMethod.POST)
    DataView postUnGrantResourcesInNodeToRole(@RequestParam("nodeId") String nodeId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/grantAllChildResourcesInNodeToRole", method = RequestMethod.POST)
    DataView postGrantAllChildResourcesInNodeToRole(@RequestParam("nodeId") String nodeId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantAllChildResourcesInNodeToRole", method = RequestMethod.POST)
    DataView postUnGrantAllChildResourcesInNodeToRole(@RequestParam("nodeId") String nodeId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

}
