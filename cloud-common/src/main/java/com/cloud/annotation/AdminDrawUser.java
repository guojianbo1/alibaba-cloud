package com.cloud.annotation;

import com.cloud.aspect.AdminDrawUserAspect;

import java.lang.annotation.*;

/**
 * 将header中的用户信息放在当前线程MDC中，便于AdminUserUtils获取
 *
 * @author guojianbo
 * @date 2023/6/16 10:15
 * @see AdminDrawUserAspect
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdminDrawUser {

}
