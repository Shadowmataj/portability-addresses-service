package com.portability.addresses_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.portability.addresses_service.service.*.*(..))")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("==> Executing method: {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        log.debug("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("<== Method executed successfully: {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        log.debug("Result: {}", result);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("<!> Exception in method: {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        log.error("Error message: {}", exception.getMessage());
    }

    @Around("serviceLayer()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        
        log.info("[@] Starting: {}.{}()", className, methodName);
        
        try {
            Object result = proceedingJoinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            log.info("[✓] Completed: {}.{}() in {} ms", className, methodName, executionTime);
            
            return result;
        } catch (Throwable throwable) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[✗] Failed: {}.{}() after {} ms", className, methodName, executionTime);
            throw throwable;
        }
    }
}
