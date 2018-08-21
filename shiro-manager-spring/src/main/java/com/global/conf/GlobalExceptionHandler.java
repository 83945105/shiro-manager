package com.global.conf;

import com.avalon.holygrail.ss.exception.NeedLoginException;
import com.avalon.holygrail.ss.exception.NoAuthorityException;
import com.avalon.holygrail.ss.exception.NotFoundException;
import com.avalon.holygrail.ss.exception.ResultException;
import com.avalon.holygrail.ss.norm.ResultInfo;
import com.avalon.holygrail.ss.util.DataViewUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.avalon.holygrail.ss.view.ExceptionView;
import com.shiro.exception.ThirdPartyLoginException;
import feign.FeignException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * Created by 白超 on 2018-1-20.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //404异常
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    DataView notFound(HttpServletRequest request, NotFoundException ex) throws Exception {
        ExceptionView view = new ExceptionView(ex.getResultInfo());
        return view;
    }

    //需要登录
    @ExceptionHandler(NeedLoginException.class)
    @ResponseBody
    DataView needLogin(HttpServletRequest request, NeedLoginException ex) throws Exception {
        ExceptionView view = new ExceptionView(ex.getResultInfo());
        return view;
    }

    //无权
    @ExceptionHandler(NoAuthorityException.class)
    @ResponseBody
    DataView noAuthority(HttpServletRequest request, NoAuthorityException ex) throws Exception {
        ExceptionView view = new ExceptionView(ex.getResultInfo());
        return view;
    }

    //角色授权异常
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    DataView checkRole(HttpServletRequest request, UnauthenticatedException ex) throws Exception {
        return DataViewUtil.getMessageViewFail(MessageConfig.EXCEPTION_UNAUTHENTICATED_MESSAGE);
    }

    //角色检验异常
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    DataView checkRole(HttpServletRequest request, UnauthorizedException ex) throws Exception {
        return DataViewUtil.getMessageViewFail(MessageConfig.EXCEPTION_UNAUTHORIZED_MESSAGE);
    }

    //第三方登录异常
    @ExceptionHandler(ThirdPartyLoginException.class)
    @ResponseBody
    DataView thirdPartyLogin(HttpServletRequest request, ThirdPartyLoginException ex) throws Exception {
        return DataViewUtil.getMessageViewFail(MessageConfig.EXCEPTION_THIRD_PARTY_LOGIN_MESSAGE);
    }

    //Feign异常
    @ExceptionHandler(FeignException.class)
    @ResponseBody
    DataView feign(HttpServletRequest request, FeignException ex) throws Exception {
        ex.printStackTrace();
        return DataViewUtil.getMessageViewFail(MessageConfig.EXCEPTION_FEIGN_MESSAGE);
    }

    @ExceptionHandler(ResultException.class)
    @ResponseBody
    DataView result(HttpServletRequest request, ResultException ex) throws Exception {
        ResultInfo resultInfo = ex.getResultInfo();
        if(resultInfo.isError()) {
            ex.printStackTrace();
        }
        return new ExceptionView(resultInfo);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    DataView exceptionHandler(HttpServletRequest request, Exception ex) throws Exception {
        ex.printStackTrace();
        return DataViewUtil.getMessageViewError(MessageConfig.EXCEPTION_DEFAULT_MESSAGE);
    }

}
