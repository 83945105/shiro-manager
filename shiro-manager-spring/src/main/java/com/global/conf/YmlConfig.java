package com.global.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import pub.avalon.holygrail.utils.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * yml配置
 * Created by 白超 on 2018/4/8.
 */
@Configuration
public class YmlConfig {

    /**
     * 应用名称
     */
    public static String APPLICATION_NAME;

    /**
     * 根路径
     */
    public static String CONTEXT_PATH;

    /**
     * 当前模式
     */
    public static String ACTIVE;

    /**
     * 文件上传地址
     */
    public static String UPLOAD_PATH;

    /**
     * 静态资源匹配路径
     */
    public static String STATIC_PATH_PATTERN;

    /**
     * 静态资源路径前缀
     * /** => ""
     * /resource/** => /resource
     */
    public static String STATIC_PATH_PREFIX;

    @Value("${spring.application.name}")
    public void setApplicationName(String applicationName) {
        APPLICATION_NAME = applicationName;
    }

    @Value("${server.servlet.context-path:}")
    public void setContextPath(String contextPath) {
        CONTEXT_PATH = contextPath;
    }

    @Value("${spring.profiles.active}")
    public void setActive(String active) {
        ACTIVE = active;
    }

    @Value("${web.upload-path}")
    public void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }


    private static final Pattern STATIC_PATH_PATTERN_REGEX = Pattern.compile("^(.*?)/\\*\\*$");
    private static final String ROOT_PATH = "/**";

    @Value("${spring.mvc.static-path-pattern:/**}")
    public void setStaticPathPattern(String staticPathPattern) {
        STATIC_PATH_PATTERN = staticPathPattern;
        if (StringUtil.isEmpty(STATIC_PATH_PATTERN) || ROOT_PATH.equals(STATIC_PATH_PATTERN)) {
            STATIC_PATH_PREFIX = "";
            return;
        }
        Matcher matcher = STATIC_PATH_PATTERN_REGEX.matcher(STATIC_PATH_PATTERN);
        if (matcher.find()) {
            STATIC_PATH_PREFIX = matcher.group(1);
        }
    }

}
