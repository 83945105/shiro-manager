package com.shiro.controller;

import com.avalon.holygrail.enums.Status;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.shiro.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/7/24.
 */
@RestController
@RequestMapping("/api-shiro-user")
public class UserController {

    @Autowired
    private UserApi userApi;

    @RequestMapping(value = "/get/userColumnList", method = RequestMethod.GET)
    public DataView getUserColumnList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.userApi.getUserColumnList(request, response);
    }

    @RequestMapping(value = "/get/userPageList", method = RequestMethod.GET)
    public DataView getUserPageList(String searchText, Integer currentPage, Integer pageSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.userApi.getUserPageList(searchText, currentPage, pageSize, request, response);
    }

    @RequestMapping(value = "/put/userStatus/{id}", method = RequestMethod.PUT)
    public DataView putUserStatus(@PathVariable String id, String status, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (Status.NORMAL.name().equals(status)) {
            return this.userApi.putUserEnabled(id, request, response);
        } else if (Status.DISABLED.name().equals(status)) {
            return this.userApi.putUserDisabled(id, request, response);
        } else {
            ExceptionUtil.throwFailException("状态类型不正确");
        }
        return null;
    }

}
