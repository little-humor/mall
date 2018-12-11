package com.humor.user.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author zhangshaoze
 * @date 2018/11/27 4:31 PM
 */
@Slf4j
@Aspect
@Component
public class MethodRuningLog {


    @Pointcut("execution(* com.humor.user.controller.UserController.login(..))")
    public void logStart(){}

    @Before("logStart())")
    public void start(){
        log.info("method run start...");
    }
}
