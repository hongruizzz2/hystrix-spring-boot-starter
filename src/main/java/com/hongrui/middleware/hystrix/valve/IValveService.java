package com.hongrui.middleware.hystrix.valve;

import com.hongrui.middleware.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author hongrui
 * @description
 * @date 2024-11-17 11:47
 */
public interface IValveService {

    Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable;
}
