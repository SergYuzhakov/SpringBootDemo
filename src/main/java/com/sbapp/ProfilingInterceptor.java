package com.sbapp;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class ProfilingInterceptor {

    @Around("@annotation(com.sbapp.annotations.TimeLogger)")
    public Object simpleAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start();

        Object returnValue = joinPoint.proceed();

        sw.stop();
        String method = joinPoint.getSignature().getName();
        log.info("Executed method: {}, took - {} ms",
                method,
                sw.getTotalTimeMillis());
        return returnValue;
    }
}
