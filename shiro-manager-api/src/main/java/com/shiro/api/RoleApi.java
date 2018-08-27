package com.shiro.api;

import com.shiro.entity.JurResPost;
import com.shiro.entity.JurRolePost;
import com.shiro.entity.JurRolePut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pub.avalon.holygrail.response.views.DataView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/12.
 */
@FeignClient(name = "${feign.role-api-service-name:shiro-service}", path = "${feign.role-api-service-path:/api-shiro-role}")
public interface RoleApi {

    @RequestMapping(value = "/post/validateRole", method = RequestMethod.POST)
    DataView postValidateRole(@RequestParam("role") String role, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/newRole", method = RequestMethod.POST)
    DataView postNewRole(@RequestParam("record") JurRolePost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/roleStatus/{id}", method = RequestMethod.PUT)
    DataView putRoleStatus(@PathVariable("id") String id, String status, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/rolePageList", method = RequestMethod.GET)
    DataView getRolePageList(@RequestParam("record") JurRolePost record, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/roleList", method = RequestMethod.GET)
    DataView getRoleList(@RequestParam("record") JurRolePost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/rolesForResource", method = RequestMethod.GET)
    DataView getRolesForResource(@RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/delete/roleList", method = RequestMethod.DELETE)
    DataView deleteRoleList(@RequestParam("ids") String[] ids, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/role/{id}", method = RequestMethod.PUT)
    DataView putRole(@PathVariable("id") String id, @RequestParam("record") JurRolePut record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;


}
