package com.hongrui.middleware.hystrix.valve.impl;

import com.alibaba.fastjson.JSON;
import com.hongrui.middleware.hystrix.annotation.DoHystrix;
import com.hongrui.middleware.hystrix.valve.IValveService;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author hongrui
 * @description
 * @date 2024-11-17 11:48
 */
public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {

    private ProceedingJoinPoint jp;
    private Method method;
    private DoHystrix doHystrix;

    public HystrixValveImpl(int time) {

        /*********************************************************************************************
         * 置HystrixCommand的属性
         * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
         * CommandKey：          该命令的名称
         * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
         * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
         * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
         *********************************************************************************************/

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(time)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                        .Setter().withCoreSize(10)));
    }


    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args) throws Throwable {
        this.jp = jp;
        this.method = method;
        this.doHystrix = doHystrix;

        // 设置熔断超时事件
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue()));
        return this.execute();
    }

    @Override
    protected Object run() throws Exception {
        try {
            return jp.proceed();
        } catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    protected Object getFallback() {
        return JSON.parseObject(doHystrix.returnJson(), method.getReturnType());
    }

}
