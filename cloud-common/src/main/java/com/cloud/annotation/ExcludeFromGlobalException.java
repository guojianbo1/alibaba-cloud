package com.cloud.annotation;

import com.cloud.exception.GlobalExceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法不进行全局异常捕捉进行统一返回，而是继续抛出异常
 * @author guojianbo
 * @see GlobalExceptionHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeFromGlobalException {
}
