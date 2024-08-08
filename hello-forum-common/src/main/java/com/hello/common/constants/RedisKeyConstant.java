package com.hello.common.constants;

public class RedisKeyConstant {
    /**
     * 用户注册锁，Key Prefix + 用户名
     */
    public static final String LOCK_USER_REGISTER = "helloform-user-service:lock:user-register:";
}
