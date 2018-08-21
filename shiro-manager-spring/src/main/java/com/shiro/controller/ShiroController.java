package com.shiro.controller;

import com.avalon.holygrail.ss.util.DataViewUtil;
import com.avalon.holygrail.ss.util.ExceptionUtil;
import com.avalon.holygrail.ss.view.DataView;
import com.global.conf.MessageConfig;
import com.global.conf.ShiroConfig;
import com.shiro.api.ShiroApi;
import com.shiro.modules.SecurityUtilsModule;
import com.shiro.norm.Certificate;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 白超 on 2018/6/11.
 */
@RestController
@RequestMapping("${feign.shiro-api-service-path:/api-shiro}")
public class ShiroController implements ShiroApi {

    @Autowired
    SecurityUtilsModule securityUtilsModule;

    /**
     * 获取证书
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/get/currentCertificate")
    @Override
    public DataView getCurrentCertificate(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Certificate certificate = this.securityUtilsModule.getCurrentCertificate(request, response);

        if (certificate == null) {
            ExceptionUtil.throwNeedLoginException(MessageConfig.EXCEPTION_NEED_LOGIN_MESSAGE);
        }

        Map<String, Object> map = new HashMap<>();
        Session session = this.securityUtilsModule.getCurrentSession(request, response);
        map.put("sessionIdName", ShiroConfig.SESSION_ID_NAME);
        map.put("sessionIdValue", session.getId().toString());
        map.put("user", certificate);

        return DataViewUtil.getModelViewSuccess(map);
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/get/logout")
    @Override
    public DataView getLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.securityUtilsModule.logout(request, response);

        return DataViewUtil.getMessageViewSuccess("您已退出登录");
    }

}
