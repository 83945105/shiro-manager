package com.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 未实现指定接口的异常
 * Created by 白超 on 2018/6/8.
 */
public class UnimplementedInterfaceException extends AuthenticationException {

    public UnimplementedInterfaceException() {
    }

    public UnimplementedInterfaceException(String message) {
        super(message);
    }

    public UnimplementedInterfaceException(Throwable cause) {
        super(cause);
    }

    public UnimplementedInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }
}
