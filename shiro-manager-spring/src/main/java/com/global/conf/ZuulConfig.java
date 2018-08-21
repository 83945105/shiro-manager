package com.global.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by 白超 on 2018/6/19.
 */
@Configuration
public class ZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties serverProperties;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public DynamicRouteLocator routeLocator() {
        DynamicRouteLocator routeLocator = new DynamicRouteLocator(this.serverProperties.getServlet().getServletPrefix(), this.zuulProperties);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }
}
