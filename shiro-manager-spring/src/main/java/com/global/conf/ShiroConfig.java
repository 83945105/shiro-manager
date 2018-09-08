package com.global.conf;

import com.dt.core.engine.MySqlEngine;
import com.dt.jdbc.core.SpringJdbcEngine;
import com.shiro.entity.JurResGet;
import com.shiro.filter.AjaxFormAuthenticationFilter;
import com.shiro.filter.ResourceCheckFilter;
import com.shiro.model.JurResModel;
import com.shiro.modules.LoginAuthenticationModule;
import com.shiro.modules.RequestAuthenticationModule;
import com.shiro.modules.SecurityUtilsModule;
import com.shiro.modules.ShiroModules;
import com.shiro.permission.UrlPermissionResovler;
import com.shiro.plugins.SeparationSessionManager;
import com.shiro.realm.LoginRealm;
import com.shiro.utils.TableUtils;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.fusesource.jansi.Ansi;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import pub.avalon.holygrail.utils.StringUtil;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author 白超
 * @date 2018/6/25
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private SpringJdbcEngine jdbcEngine;

    public static Boolean ENABLED = true;

    public static String SESSION_ID_NAME = "JSESSIONID";

    public static long SESSION_VALIDATION_INTERVAL = 600000;

    public static long SESSION_TIME_OUT = 600000;

    public static int COOKIE_MAX_AGE = -1;

    public static String CACHE_TYPE = "ehcache";

    public static boolean ENABLED_AUTHENTICATION_CACHING = true;

    public static boolean ENABLED_AUTHORIZATION_CACHING = true;

    public static String EHCACHE_CONFIG_LOCATION = "classpath:ehcache/shiro-ehcache.xml";

    public static String EHCACHE_AUTHENTICATION_NAME = "shiro-authenticationCache";

    public static String EHCACHE_AUTHORIZATION_NAME = "shiro-authorizationCache";

    public static String LOGIN_USER_NAME_PARAM = "username";

    public static String LOGIN_PASS_WORD_PARAM = "password";

    public static String LOGIN_POST_URL = "";

    public static String LOGIN_PAGE_URL = "";

    public static String LOGIN_SUCCESS_URL = "";

    @Value("${shiro.enabled}")
    public void setENABLED(boolean enabled) {
        ENABLED = enabled;
    }

    @Value("${shiro.session-id-name}")
    public void setSessionIdName(String sessionIdName) {
        SESSION_ID_NAME = sessionIdName;
    }

    @Value("${shiro.session-validation-interval}")
    public void setSessionValidationInterval(long sessionValidationInterval) {
        SESSION_VALIDATION_INTERVAL = sessionValidationInterval;
    }

    @Value("${shiro.session-time-out}")
    public void setSessionTimeOut(long sessionTimeOut) {
        SESSION_TIME_OUT = sessionTimeOut;
    }

    @Value("${shiro.cookie-max-age}")
    public void setCookieMaxAge(int cookieMaxAge) {
        COOKIE_MAX_AGE = cookieMaxAge;
    }

    @Value("${shiro.cache-type}")
    public void setCacheType(String cacheType) {
        CACHE_TYPE = cacheType;
    }

    @Value("${shiro.enabled-authentication-caching}")
    public void setEnabledAuthenticationCaching(boolean enabledAuthenticationCaching) {
        ENABLED_AUTHENTICATION_CACHING = enabledAuthenticationCaching;
    }

    @Value("${shiro.enabled-authorization-caching}")
    public void setEnabledAuthorizationCaching(boolean enabledAuthorizationCaching) {
        ENABLED_AUTHORIZATION_CACHING = enabledAuthorizationCaching;
    }

    @Value("${shiro.ehcache-config-location}")
    public void setEhcacheConfigLocation(String ehcacheConfigLocation) {
        EHCACHE_CONFIG_LOCATION = ehcacheConfigLocation;
    }

    @Value("${shiro.ehcache-authentication-name}")
    public void setEhcacheAuthenticationName(String ehcacheAuthenticationName) {
        EHCACHE_AUTHENTICATION_NAME = ehcacheAuthenticationName;
    }

    @Value("${shiro.ehcache-authorization-name}")
    public void setEhcacheAuthorizationName(String ehcacheAuthorizationName) {
        EHCACHE_AUTHORIZATION_NAME = ehcacheAuthorizationName;
    }

    @Value("${shiro.login-user-name-param}")
    public void setLoginUserNameParam(String loginUserNameParam) {
        LOGIN_USER_NAME_PARAM = loginUserNameParam;
    }

    @Value("${shiro.login-pass-word-param}")
    public void setLoginPassWordParam(String loginPassWordParam) {
        LOGIN_PASS_WORD_PARAM = loginPassWordParam;
    }

    @Value("${shiro.login-post-url}")
    public void setLoginPostUrl(String loginPostUrl) {
        LOGIN_POST_URL = loginPostUrl;
    }

    @Value("${shiro.login-page-url}")
    public void setLoginPageUrl(String loginPageUrl) {
        LOGIN_PAGE_URL = loginPageUrl;
    }

    @Value("${shiro.login-success-url}")
    public void setLoginSuccessUrl(String loginSuccessUrl) {
        LOGIN_SUCCESS_URL = loginSuccessUrl;
    }

    @Bean
    public ShiroModules shiroModules(LoginAuthenticationModule<?> loginAuthenticationModule,
                                     RequestAuthenticationModule requestAuthenticationModule) {
        ShiroModules modules = new ShiroModules();
        modules.addModule(loginAuthenticationModule)
                .addModule(requestAuthenticationModule);
        return modules;
    }

    @Bean("securityUtilsModule")
    public SecurityUtilsModule securityUtilsModule() {
        return new SecurityUtilsModule.DefaultSecurityUtilsModule();
    }

    @Bean("realm")
    public Realm realm(ShiroModules modules) {
        LoginRealm realm = new LoginRealm();
        realm.setModules(modules);
        /*开启缓存*/
        realm.setCachingEnabled(true);
        /*打开认证缓存功能*/
        realm.setAuthenticationCachingEnabled(ShiroConfig.ENABLED_AUTHENTICATION_CACHING);
        realm.setAuthenticationCacheName(ShiroConfig.EHCACHE_AUTHENTICATION_NAME);
        /*打开授权缓存功能*/
        realm.setAuthorizationCachingEnabled(ShiroConfig.ENABLED_AUTHORIZATION_CACHING);
        realm.setAuthorizationCacheName(ShiroConfig.EHCACHE_AUTHORIZATION_NAME);
        return realm;
    }

    @Value("${shiro.session.validationInterval:1800000}")
    long validationInterval;

    @Bean("sessionValidationScheduler")
    public SessionValidationScheduler sessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(validationInterval);
        return scheduler;
    }

    @Bean("sessionIdGenerator")
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean("sessionDAO")
    public SessionDAO sessionDAO(SessionIdGenerator sessionIdGenerator) {
        EnterpriseCacheSessionDAO ed = new EnterpriseCacheSessionDAO();
        ed.setActiveSessionsCacheName("shiro-activeSessionCache");
        ed.setSessionIdGenerator(sessionIdGenerator);
        return ed;
    }

    @Bean("sessionIdCookie")
    public Cookie sessionIdCookie() {
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName(ShiroConfig.SESSION_ID_NAME);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(ShiroConfig.COOKIE_MAX_AGE);
        return cookie;
    }

    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager(SessionValidationScheduler sessionValidationScheduler,
                                                   SessionDAO sessionDAO,
                                                   Cookie sessionIdCookie) {
        SeparationSessionManager sm = new SeparationSessionManager();
        sm.setGlobalSessionTimeout(ShiroConfig.SESSION_TIME_OUT);
        sm.setSessionValidationScheduler(sessionValidationScheduler);
        sm.setDeleteInvalidSessions(true);
        sm.setSessionValidationSchedulerEnabled(true);
        sm.setSessionDAO(sessionDAO);
        sm.setSessionIdCookieEnabled(true);
        sm.setSessionIdCookie(sessionIdCookie);
        return sm;
    }

    @Bean("permissionResolver")
    public PermissionResolver permissionResolver() {
        return new UrlPermissionResovler();
    }

    @Bean("authorizer")
    public Authorizer authorizer(PermissionResolver permissionResolver) {
        ModularRealmAuthorizer ma = new ModularRealmAuthorizer();
        ma.setPermissionResolver(permissionResolver);
        return ma;
    }

    @Bean("ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile(ShiroConfig.EHCACHE_CONFIG_LOCATION);
        return cacheManager;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(Realm realm,
                                                     SessionManager sessionManager,
                                                     Authorizer authorizer,
                                                     EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager dm = new DefaultWebSecurityManager();
        dm.setAuthorizer(authorizer);
        dm.setSessionManager(sessionManager);
        dm.setCacheManager(ehCacheManager);
        dm.setRealm(realm);
        return dm;
    }

    @Value("${shiro.filter.loginUrl}")
    String login_url;
    @Value("${shiro.filter.unauthorizedUrl}")
    String unauthorizedUrl;

    public static final String formFilterName = "authc";

    public FormAuthenticationFilter formAuthenticationFilter(ShiroModules modules) {
        AjaxFormAuthenticationFilter ff = new AjaxFormAuthenticationFilter();
        ff.setUsernameParam(ShiroConfig.LOGIN_USER_NAME_PARAM);
        ff.setPasswordParam(ShiroConfig.LOGIN_PASS_WORD_PARAM);
        ff.setModules(modules);
        ff.setLoginUrl(ShiroConfig.LOGIN_POST_URL);
        ff.setSuccessUrl(ShiroConfig.LOGIN_SUCCESS_URL);
        ff.setCrossDomain(true);
        return ff;
    }

    public ResourceCheckFilter resourceCheckFilter(ShiroModules modules) {
        ResourceCheckFilter rf = new ResourceCheckFilter();
        rf.setModules(modules);
        return rf;
    }

    @Value("${spring.mvc.static-path-pattern}")
    String staticPathPattern;

    public static final Set<String> RES_TABLE_NAMES = new HashSet<>();
    public static final Set<String> ROLE_TABLE_NAMES = new HashSet<>();
    public static final Set<String> ROLE_RES_TABLE_NAMES = new HashSet<>();
    public static final Set<String> ROLE_USER_TABLE_NAMES = new HashSet<>();

    private static final String ROOT_PATH = "/**";

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              ShiroModules modules,
                                              JdbcTemplate jdbcTemplate) {
        /*获取权限表信息开始*/
        try {
            List<String> tables = jdbcTemplate.queryForList("show tables", String.class);
            for (String table : tables) {
                if (table.matches(TableUtils.RES_TABLE_NAME_REGEX)) {
                    RES_TABLE_NAMES.add(table);
                } else if (table.matches(TableUtils.ROLE_TABLE_NAME_REGEX)) {
                    ROLE_TABLE_NAMES.add(table);
                } else if (table.matches(TableUtils.ROLE_RES_TABLE_NAME_REGEX)) {
                    ROLE_RES_TABLE_NAMES.add(table);
                } else if (table.matches(TableUtils.ROLE_USER_TABLE_NAME_REGEX)) {
                    ROLE_USER_TABLE_NAMES.add(table);
                }
            }
        } catch (DataAccessException e) {
            System.err.println("获取权限表信息失败");
            e.printStackTrace();
        }
        /*获取权限表信息结束*/
        if (ENABLED) {
            ShiroFilterFactoryBean fb = new ShiroFilterFactoryBean();
            fb.setSecurityManager(securityManager);
            fb.setLoginUrl(login_url);
            fb.setUnauthorizedUrl(unauthorizedUrl);
            Map<String, Filter> filters = new LinkedHashMap<>();
            filters.put(formFilterName, formAuthenticationFilter(modules));
            filters.put("resFilter", resourceCheckFilter(modules));
            fb.setFilters(filters);
            Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
            /*静态资源放行开始*/
            if (!StringUtil.isEmpty(staticPathPattern) && !ROOT_PATH.equals(staticPathPattern)) {
                filterChainDefinitionMap.put(staticPathPattern, "anon");
            }
            /*静态资源放行结束*/
            /*通用放行开始*/
            List<String> publicPathList = new ArrayList<>();
            try {
                List<JurResGet> commonResList = this.jdbcEngine.queryForList(JurResGet.class, MySqlEngine.main(TableUtils.ROOT_RES_TABLE_NAME, JurResModel.class)
                        .where((condition, mainTable) -> condition
                                .and(mainTable.parentId().equalTo("7be0ba2c-d0c6-4c6b-bde1-bd8b21a0779f"))));
                for (JurResGet commonRes : commonResList) {
                    filterChainDefinitionMap.put(commonRes.getUrl(), "anon");
                    publicPathList.add(commonRes.getUrl());
                }
            } catch (Exception e) {
                System.err.println("获取通用放行资源失败");
                e.printStackTrace();
            }
            /*通用放行结束*/
            /*打印信息开始*/
            List<String> staticPathList = new ArrayList<>();
            if (!StringUtil.isEmpty(staticPathPattern) && !ROOT_PATH.equals(staticPathPattern)) {
                staticPathList.add(staticPathPattern);
            }
            this.printEnabledInfo(staticPathList, publicPathList);
            /*打印信息结束*/
            filterChainDefinitionMap.put(ShiroConfig.LOGIN_PAGE_URL, "anon");
            filterChainDefinitionMap.put(ShiroConfig.LOGIN_POST_URL, formFilterName + ",anon");
            filterChainDefinitionMap.put("/**", formFilterName + ",resFilter");
            fb.setFilterChainDefinitionMap(filterChainDefinitionMap);
            return fb;
        }
        // 禁用处理
        /*打印信息开始*/
        this.printDisabledInfo();
        /*打印信息结束*/
        ShiroFilterFactoryBean fb = new ShiroFilterFactoryBean();
        fb.setSecurityManager(securityManager);
        fb.setLoginUrl(login_url);
        fb.setUnauthorizedUrl(unauthorizedUrl);
        Map<String, Filter> filters = new LinkedHashMap<>();
        fb.setFilters(filters);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/**", "anon");
        fb.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return fb;
    }

    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager);
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aa = new AuthorizationAttributeSourceAdvisor();
        aa.setSecurityManager(securityManager);
        return aa;
    }

    private void printDisabledInfo() {
        System.out.println(Ansi.ansi().eraseScreen()
                .fg(Ansi.Color.RED)
                .a("**************************************************\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("权限管理模块")
                .fg(Ansi.Color.RED)
                .a("\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("开发者：")
                .fg(Ansi.Color.YELLOW)
                .a("白超")
                .fg(Ansi.Color.RED)
                .a("\n")
                .a("||   ")
                .fg(Ansi.Color.GREEN)
                .a("有问题请联系 ")
                .fg(Ansi.Color.YELLOW)
                .a("QQ：83945105 微信：wx_83945105")
                .fg(Ansi.Color.RED)
                .a("\n")
                .a("||\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("状态： ")
                .fg(Ansi.Color.RED)
                .a("已禁用")
                .fg(Ansi.Color.RED)
                .a("\n")
                .a("**************************************************")
                .reset());
    }

    private void printEnabledInfo(List<String> staticPathList, List<String> publicPathList) {
        Ansi ansi = Ansi.ansi().eraseScreen()
                .fg(Ansi.Color.GREEN)
                .a("**************************************************\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("权限管理模块")
                .fg(Ansi.Color.GREEN)
                .a("\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("开发者：")
                .fg(Ansi.Color.YELLOW)
                .a("白超")
                .fg(Ansi.Color.GREEN)
                .a("\n")
                .a("||   ")
                .fg(Ansi.Color.GREEN)
                .a("有问题请联系 ")
                .fg(Ansi.Color.YELLOW)
                .a("QQ：83945105 微信：wx_83945105")
                .fg(Ansi.Color.GREEN)
                .a("\n")
                .a("||\n")
                .a("||                  ")
                .fg(Ansi.Color.GREEN)
                .a("状态： ")
                .fg(Ansi.Color.GREEN)
                .a("已启用")
                .fg(Ansi.Color.GREEN)
                .a("\n");

        ansi.a("||\n")
                .a("||   ")
                .fg(Ansi.Color.GREEN)
                .a("放行静态资源地址：");

        if (staticPathList != null && staticPathList.size() > 0) {
            for (String staticPath : staticPathList) {
                ansi.fg(Ansi.Color.GREEN)
                        .a("\n||   ")
                        .fg(Ansi.Color.CYAN)
                        .a(staticPath);

            }
        } else {
            ansi.fg(Ansi.Color.GREEN)
                    .a("\n||   ")
                    .fg(Ansi.Color.RED)
                    .a("无");
        }
        ansi.fg(Ansi.Color.GREEN)
                .a("\n");
        ansi.a("||\n")
                .a("||   ")
                .fg(Ansi.Color.GREEN)
                .a("通用放行地址：");
        if (publicPathList != null && publicPathList.size() > 0) {
            for (String publicPath : publicPathList) {
                ansi.fg(Ansi.Color.GREEN)
                        .a("\n||   ")
                        .fg(Ansi.Color.CYAN)
                        .a(publicPath);
            }
        } else {
            ansi.fg(Ansi.Color.GREEN)
                    .a("\n||   ")
                    .fg(Ansi.Color.RED)
                    .a("无");
        }
        ansi.fg(Ansi.Color.GREEN)
                .a("\n");
        System.out.println(ansi
                .a("**************************************************")
                .reset());
    }
}
