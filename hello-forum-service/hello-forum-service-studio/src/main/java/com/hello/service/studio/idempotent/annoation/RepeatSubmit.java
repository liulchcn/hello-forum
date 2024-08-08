package com.hello.service.studio.idempotent.annoation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    enum Type{PARAM, TOKEN}
    Type limitType() default Type.PARAM;
    /**
     * 触发幂等失败逻辑时，返回的错误提示信息
     */
    String message() default "您操作太快，请稍后再试";

    /**
     * 间隔时间，单位（ms）
     * @return
     */
    long interval() default 3600L;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
