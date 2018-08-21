package com.shiro.api;

import com.avalon.holygrail.ss.view.DataView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/24.
 */
@FeignClient(name = "${feign.user-api-service-name:shiro-service}", path = "${feign.user-api-service-path:/api-shiro-user}")
public interface UserApi {

    @RequestMapping(value = "/get/userColumnList", method = RequestMethod.GET)
    DataView getUserColumnList(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/get/userPageList", method = RequestMethod.GET)
    DataView getUserPageList(@RequestParam("searchText") String searchText, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/userDisabled/{id}", method = RequestMethod.PUT)
    DataView putUserDisabled(@PathVariable("id") String id, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

    @RequestMapping(value = "/put/userEnabled/{id}", method = RequestMethod.PUT)
    DataView putUserEnabled(@PathVariable("id") String id, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response) throws Exception;

}
