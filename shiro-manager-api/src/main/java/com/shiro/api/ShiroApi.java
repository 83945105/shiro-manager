package com.shiro.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pub.avalon.holygrail.response.views.DataView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/6/11.
 */
@FeignClient(name = "${feign.shiro-api-service-name:shiro-service}", path = "${feign.shiro-api-service-path:/api-shiro}")
public interface ShiroApi {

    @RequestMapping(value = "/get/currentCertificate", method = RequestMethod.GET)
    DataView getCurrentCertificate(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/logout", method = RequestMethod.GET)
    DataView getLogout(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;


}
