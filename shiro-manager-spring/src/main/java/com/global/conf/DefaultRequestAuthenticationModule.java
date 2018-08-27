package com.global.conf;

import com.shiro.modules.RequestAuthenticationModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import pub.avalon.holygrail.response.utils.ResultUtil;
import pub.avalon.holygrail.response.views.ExceptionView;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 白超 on 2018/6/8.
 */
@Component
public class DefaultRequestAuthenticationModule implements RequestAuthenticationModule {

    /**
     * JSON转换器
     */
    private HttpMessageConverter<Object> jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();

    @Value("${shiro.filter.unauthorizedUrl:/view/noAuthority}")
    String unauthorizedUrl;

    @Override
    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(res);
        if (isAjax(req)) {
            this.jackson2HttpMessageConverter.write(new ExceptionView(ResultUtil.createNoAuthority(MessageConfig.EXCEPTION_NO_AUTHENTICATION_MESSAGE), unauthorizedUrl), MediaType.APPLICATION_JSON, outputMessage);
        } else {
            req.getRequestDispatcher(unauthorizedUrl).forward(request, response);
        }
        return false;
    }
}
