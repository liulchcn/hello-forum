package com.hello.model.annotation;


import org.apache.htrace.shaded.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 这边我属于临时加入的包不知道对不对"import org.apache.htrace.shaded.fasterxml.jackson.annotation.JacksonAnnotation"依赖是这个
 *
 */
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface IdEncrypt {
}