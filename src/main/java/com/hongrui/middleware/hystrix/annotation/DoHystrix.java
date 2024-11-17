package com.hongrui.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongrui
 * @description
 * @date 2024-11-17 11:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoHystrix {

    String returnJson() default ""; // 失败结果JSON
    int timeoutValue() default 0; // 超时熔断

}
