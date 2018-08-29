package com.global.controller;

import com.global.conf.MessageConfig;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.avalon.holygrail.response.utils.ResultUtil;
import pub.avalon.holygrail.response.views.DataView;
import pub.avalon.holygrail.response.views.ExceptionView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 错误处理
 *
 * @author 白超
 * @date 2018-3-15
 */
@RestController
public class GlobalErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public DataView error(Model model, HttpServletRequest request, HttpServletResponse response) {
        return new ExceptionView(ResultUtil.createNotFound(MessageConfig.EXCEPTION_NOT_FOUND_MESSAGE));
    }

    @Override
    public String getErrorPath() {
        return "error/404";
    }
}
