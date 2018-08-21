package com.global.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Message
 * Created by 白超 on 2018/6/20.
 */
@Configuration
public class MessageConfig {

    public static String EXCEPTION_DEFAULT_MESSAGE = "未知错误";
    public static String EXCEPTION_NEED_LOGIN_MESSAGE = "您需要登陆后才能进行该操作";
    public static String EXCEPTION_NOT_FOUND_MESSAGE = "您访问的地址不存在";
    public static String EXCEPTION_UNKNOWN_ACCOUNT_MESSAGE = "账号不存在";
    public static String EXCEPTION_INCORRECT_CREDENTIALS_MESSAGE = "用户名或密码错误";
    public static String EXCEPTION_LOCKED_ACCOUNT_MESSAGE = "您的账户已被冻结";
    public static String EXCEPTION_NO_AUTHENTICATION_MESSAGE = "您没有权限做此操作";
    public static String EXCEPTION_UNSUPPORTED_TOKEN_MESSAGE = "身份异常";
    public static String EXCEPTION_DISABLED_ACCOUNT_MESSAGE = "您的账户已被禁用";
    public static String EXCEPTION_EXCESSIVE_ATTEMPTS_MESSAGE = "您的操作太频繁,请稍后再试";
    public static String EXCEPTION_CONCURRENT_ACCESS_MESSAGE = "您的账户已在别处登录,请退出后重试";
    public static String EXCEPTION_ACCOUNT_MESSAGE = "账户异常";
    public static String EXCEPTION_EXPIRED_CREDENTIALS_MESSAGE = "您的账户凭据已过期";
    public static String EXCEPTION_CREDENTIALS_MESSAGE = "账户异常";
    public static String EXCEPTION_AUTHENTICATION_MESSAGE = "账户异常";
    public static String EXCEPTION_UNAUTHENTICATED_MESSAGE = "您没有权限做此操作";
    public static String EXCEPTION_UNAUTHORIZED_MESSAGE = "您没有权限做此操作";
    public static String EXCEPTION_FEIGN_MESSAGE = "请求超时";
    public static String EXCEPTION_THIRD_PARTY_LOGIN_MESSAGE = "第三方登录失败";


    @Value("${exception-default-message}")
    public void setExceptionDefaultMessage(String exceptionDefaultMessage) {
        EXCEPTION_DEFAULT_MESSAGE = exceptionDefaultMessage;
    }

    @Value("${exception-need-login-message}")
    public void setExceptionNeedLoginMessage(String exceptionNeedLoginMessage) {
        EXCEPTION_NEED_LOGIN_MESSAGE = exceptionNeedLoginMessage;
    }

    @Value("${exception-not-found-message}")
    public void setExceptionNotFoundMessage(String exceptionNotFoundMessage) {
        EXCEPTION_NOT_FOUND_MESSAGE = exceptionNotFoundMessage;
    }

    @Value("${exception-unknown-account-message}")
    public void setExceptionUnknownAccountMessage(String exceptionUnknownAccountMessage) {
        EXCEPTION_UNKNOWN_ACCOUNT_MESSAGE = exceptionUnknownAccountMessage;
    }

    @Value("${exception-incorrect-credentials-message}")
    public void setExceptionIncorrectCredentialsMessage(String exceptionIncorrectCredentialsMessage) {
        EXCEPTION_INCORRECT_CREDENTIALS_MESSAGE = exceptionIncorrectCredentialsMessage;
    }

    @Value("${exception-locked-account-message}")
    public void setExceptionLockedAccountMessage(String exceptionLockedAccountMessage) {
        EXCEPTION_LOCKED_ACCOUNT_MESSAGE = exceptionLockedAccountMessage;
    }

    @Value("${exception-no-authentication-message}")
    public void setExceptionNoAuthenticationMessage(String exceptionNoAuthenticationMessage) {
        EXCEPTION_NO_AUTHENTICATION_MESSAGE = exceptionNoAuthenticationMessage;
    }

    @Value("${exception-unsupported-token-message}")
    public void setExceptionUnsupportedTokenMessage(String exceptionUnsupportedTokenMessage) {
        EXCEPTION_UNSUPPORTED_TOKEN_MESSAGE = exceptionUnsupportedTokenMessage;
    }

    @Value("${exception-disabled-account-message}")
    public void setExceptionDisabledAccountMessage(String exceptionDisabledAccountMessage) {
        EXCEPTION_DISABLED_ACCOUNT_MESSAGE = exceptionDisabledAccountMessage;
    }

    @Value("${exception-excessive-attempts-message}")
    public void setExceptionExcessiveAttemptsMessage(String exceptionExcessiveAttemptsMessage) {
        EXCEPTION_EXCESSIVE_ATTEMPTS_MESSAGE = exceptionExcessiveAttemptsMessage;
    }

    @Value("${exception-concurrent-access-message}")
    public void setExceptionConcurrentAccessMessage(String exceptionConcurrentAccessMessage) {
        EXCEPTION_CONCURRENT_ACCESS_MESSAGE = exceptionConcurrentAccessMessage;
    }

    @Value("${exception-account-message}")
    public void setExceptionAccountMessage(String exceptionAccountMessage) {
        EXCEPTION_ACCOUNT_MESSAGE = exceptionAccountMessage;
    }

    @Value("${exception-expired-credentials-message}")
    public void setExceptionExpiredCredentialsMessage(String exceptionExpiredCredentialsMessage) {
        EXCEPTION_EXPIRED_CREDENTIALS_MESSAGE = exceptionExpiredCredentialsMessage;
    }

    @Value("${exception-credentials-message}")
    public void setExceptionCredentialsMessage(String exceptionCredentialsMessage) {
        EXCEPTION_CREDENTIALS_MESSAGE = exceptionCredentialsMessage;
    }

    @Value("${exception-authentication-message}")
    public void setExceptionAuthenticationMessage(String exceptionAuthenticationMessage) {
        EXCEPTION_AUTHENTICATION_MESSAGE = exceptionAuthenticationMessage;
    }

    @Value("${exception-unauthenticated-message}")
    public void setExceptionUnauthenticatedMessage(String exceptionUnauthenticatedMessage) {
        EXCEPTION_UNAUTHENTICATED_MESSAGE = exceptionUnauthenticatedMessage;
    }

    @Value("${exception-unauthorized-message}")
    public void setExceptionUnauthorizedMessage(String exceptionUnauthorizedMessage) {
        EXCEPTION_UNAUTHORIZED_MESSAGE = exceptionUnauthorizedMessage;
    }

    @Value("${exception-feign-message}")
    public void setExceptionFeignMessage(String exceptionFeignMessage) {
        EXCEPTION_FEIGN_MESSAGE = exceptionFeignMessage;
    }

    @Value("${exception-third-party-login-message}")
    public void setExceptionThirdPartyLoginMessage(String exceptionThirdPartyLoginMessage) {
        EXCEPTION_THIRD_PARTY_LOGIN_MESSAGE = exceptionThirdPartyLoginMessage;
    }
}
