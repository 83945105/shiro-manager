package com.global.controller;

import com.avalon.holygrail.ss.util.ResultUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.avalon.holygrail.ss.view.ExceptionView;
import com.global.conf.MessageConfig;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 错误处理
 * Created by 白超 on 2018-3-15.
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
