package com.shiro.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pub.avalon.holygrail.response.views.DataView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/25.
 */
@FeignClient(name = "${feign.role-user-api-service-name:shiro-service}", path = "${feign.role-user-api-service-path:/api-shiro-role-user}")
public interface RoleUserApi {

    @RequestMapping(value = "/get/roleUserListByUserId/{userId}", method = RequestMethod.GET)
    DataView getRoleUserListByUserId(@PathVariable("userId") String userId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleUserListByRoleId/{roleId}", method = RequestMethod.GET)
    DataView getRoleUserListByRoleId(@PathVariable("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/grantRolesToUsers", method = RequestMethod.POST)
    DataView postGrantRolesToUsers(@RequestParam("userIds") String[] userIds, @RequestParam("roleIds") String[] roleIds, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantRolesToUsers", method = RequestMethod.POST)
    DataView postUnGrantRolesToUsers(@RequestParam("userIds") String[] userIds, @RequestParam("roleIds") String[] roleIds, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/grantRoleToUser", method = RequestMethod.POST)
    DataView postGrantRoleToUser(@RequestParam("userId") String userId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/unGrantRoleToUser", method = RequestMethod.POST)
    DataView postUnGrantRoleToUser(@RequestParam("userId") String userId, @RequestParam("roleId") String roleId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

}
