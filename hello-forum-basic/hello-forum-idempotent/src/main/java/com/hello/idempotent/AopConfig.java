package com.hello.idempotent;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    // 可以在这里定义Bean，或将切面类放在这里
}
