package com.global.conf;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import pub.avalon.holygrail.response.beans.Status;
import pub.avalon.holygrail.utils.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/6/19.
 */
public class DynamicRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private ZuulProperties properties;
    private JdbcTemplate jdbcTemplate;

    public DynamicRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.properties中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    public static final String SELECT_ROUTE_SQL = "SELECT * FROM `zuul_route` WHERE zuul_route.`status` = '" + Status.NORMAL + "'";

    /**
     * 数据库路由集合
     */
    public static Map<String, ZuulRoute> DB_ROUTE_MAP;

    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        List<ZuulRoute> routeList = jdbcTemplate.query(SELECT_ROUTE_SQL, new BeanPropertyRowMapper<>(ZuulRoute.class));
        Map<String, ZuulRoute> zuulRouteMap = new HashMap<>();
        for (ZuulRoute zuulRoute : routeList) {
            if (StringUtil.isEmpty(zuulRoute.getPath()) || StringUtil.isEmpty(zuulRoute.getServiceId())) {
                continue;
            }
            ZuulProperties.ZuulRoute zr = new ZuulProperties.ZuulRoute();
            BeanUtils.copyProperties(zuulRoute, zr);
            routes.put(zr.getPath(), zr);
            zuulRouteMap.put(zr.getPath(), zuulRoute);
            DB_ROUTE_MAP = zuulRouteMap;
        }
        return routes;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static class ZuulRoute extends ZuulProperties.ZuulRoute {

        private String loginUrl;

        private String status;

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
