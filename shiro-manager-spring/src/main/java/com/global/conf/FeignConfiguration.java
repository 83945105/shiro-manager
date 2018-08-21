package com.global.conf;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by 白超 on 2018/8/2.
 */
//@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
            Enumeration<String> bodyNames = request.getParameterNames();
            StringBuffer body = new StringBuffer();
            if (bodyNames != null) {
                while (bodyNames.hasMoreElements()) {
                    String name = bodyNames.nextElement();
                    String values = request.getParameter(name);
                    body.append(name).append("=").append(values).append("&");
                }
            }
            if (body.length() != 0) {
                body.deleteCharAt(body.length() - 1);
                requestTemplate.body(body.toString());
            }
        };
    }
}
