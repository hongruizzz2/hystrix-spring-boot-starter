package com.hongrui.middleware.hystrix;

import com.hongrui.middleware.hystrix.annotation.DoHystrix;
import com.hongrui.middleware.hystrix.valve.IValveService;
import com.hongrui.middleware.hystrix.valve.impl.HystrixValveImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hongrui
 * @description
 * @date 2024-11-17 11:53
 */
@Aspect
@Component
public class DoHystrixPoint {

    @Pointcut("@annotation(com.hongrui.middleware.hystrix.annotation.DoHystrix)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
        IValveService valveService = new HystrixValveImpl(doGovern.timeoutValue());
        return valveService.access(jp, getMethod(jp), doGovern, jp.getArgs());
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
