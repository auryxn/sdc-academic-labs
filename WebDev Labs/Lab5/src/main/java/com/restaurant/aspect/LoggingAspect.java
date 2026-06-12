package com.restaurant.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.restaurant.controller.*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(">> {}({})", method, args.length > 0 ? args[0] : "");
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        log.info("<< {} completed in {}ms", method, duration);

        return result;
    }

    @Around("execution(* com.restaurant.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String method = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        if (duration > 100) {
            log.warn("SLOW {} took {}ms", method, duration);
        } else {
            log.debug("{} took {}ms", method, duration);
        }

        return result;
    }

    @Around("execution(* com.restaurant.exception.*.*(..))")
    public Object logExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("Exception in {}: {}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }
}
