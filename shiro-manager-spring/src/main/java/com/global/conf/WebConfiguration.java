package com.global.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.*;

/**
 * web配置
 * Created by 白超 on 2018-4-5.
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Value("${web.cors.allow}")
    private String allow;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if ("true".equals(allow)) {
            //设置允许跨域的路径
            registry.addMapping("/**")
                    //设置允许跨域请求的域名
                    .allowedOrigins("*")
                    //是否允许证书 不再默认开启
                    .allowCredentials(true)
                    //设置允许的方法
                    .allowedMethods("*")
                    //跨域允许时间
                    .maxAge(3600);
        }
    }

    @Value("${web.upload-path}")
    private String uploadPath;
    @Value("${spring.mvc.static-path-pattern:/**}")
    private String staticPathPattern;
    @Value("${spring.resources.static-locations:classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/}")
    private String staticLocations;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration registration = registry.addResourceHandler(staticPathPattern);
        for (String location : staticLocations.split(",")) {
            registration.addResourceLocations(location);
        }
//        registration.addResourceLocations("file:" + uploadPath);
        super.addResourceHandlers(registry);
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new HttpPutFormContentFilter();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
