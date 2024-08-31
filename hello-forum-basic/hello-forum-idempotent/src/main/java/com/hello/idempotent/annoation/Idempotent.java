package com.hello.idempotent.annoation;

import com.hello.idempotent.enums.IdempotentSceneEnum;
import com.hello.idempotent.enums.IdempotentTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 幂等key
     * @return
     */
    String key() default "";

    String Message() default "您操作太快了，请稍后再试";

    /**
     * 验证幂等类型
     * @return
     */
    IdempotentTypeEnum type() default IdempotentTypeEnum.PARAM;

    /**
     * 验证幂等情景
     */
    IdempotentSceneEnum scene() default IdempotentSceneEnum.RESTAPI;

    /**
     * 设置防重令牌前缀，MQ 幂等去重可选
     * @return
     */
    String uniqueKeyPrefix() default "";

    long keyTimeout() default 3600L;

}
