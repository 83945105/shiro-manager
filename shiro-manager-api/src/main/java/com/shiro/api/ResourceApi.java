package com.shiro.api;

import com.avalon.holygrail.ss.view.DataView;
import com.shiro.entity.JurResPost;
import com.shiro.entity.JurResPut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 资源接口
 * Created by 白超 on 2018/6/13.
 */
@FeignClient(name = "${feign.resource-api-service-name:shiro-service}", path = "${feign.resource-api-service-path:/api-shiro-resource}")
public interface ResourceApi {

    @RequestMapping(value = "/post/newResource", method = RequestMethod.POST)
    DataView postNewResource(@RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/resourceStatus/{id}", method = RequestMethod.PUT)
    DataView putResourceStatus(@PathVariable("id") String id, String status, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/resourcePageList", method = RequestMethod.GET)
    DataView getResourcePageList(@RequestParam("record") JurResPost record, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/resourceList", method = RequestMethod.GET)
    DataView getResourceList(@RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/rootResourcePageList", method = RequestMethod.GET)
    DataView getRootResourcePageList(@RequestParam("record") JurResPost record, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/rootResourceList", method = RequestMethod.GET)
    DataView getRootResourceList(@RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/childResourceList", method = RequestMethod.GET)
    DataView getChildResourceList(@RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/delete/resourceList", method = RequestMethod.DELETE)
    DataView deleteResourceList(@RequestParam("ids") String[] ids, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/resource/{id}", method = RequestMethod.PUT)
    DataView putResource(@PathVariable("id") String id, @RequestParam("record") JurResPut record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/locationResourceList", method = RequestMethod.GET)
    DataView getLocationResourceList(@RequestParam("rootIds") String[] rootIds, @RequestParam("record") JurResPost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;
}
