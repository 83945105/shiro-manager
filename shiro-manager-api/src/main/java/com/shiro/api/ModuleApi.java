package com.shiro.api;

import com.shiro.entity.ZuulRouteGet;
import com.shiro.entity.ZuulRoutePost;
import com.shiro.entity.ZuulRoutePut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pub.avalon.holygrail.response.views.DataView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/11.
 */
@FeignClient(name = "${feign.module-api-service-name:shiro-service}", path = "${feign.module-api-service-path:/api-shiro-module}")
public interface ModuleApi {

    @RequestMapping(value = "/get/module/{id}", method = RequestMethod.GET)
    DataView getModule(@PathVariable("id") String id, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/validateServiceId", method = RequestMethod.POST)
    DataView postValidateServiceId(@RequestParam("serviceId") String serviceId, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/moduleList", method = RequestMethod.GET)
    DataView getModuleList(@RequestParam("record") ZuulRouteGet record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/post/module", method = RequestMethod.POST)
    DataView postModule(@RequestParam("record") ZuulRoutePost record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/module/{id}", method = RequestMethod.PUT)
    DataView putModule(@PathVariable("id") String id, @RequestParam("record") ZuulRoutePut record, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/delete/module/{id}")
    DataView deleteModule(@PathVariable("id") String id, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

}
