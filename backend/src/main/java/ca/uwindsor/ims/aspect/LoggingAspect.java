package ca.uwindsor.ims.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    private static final long SLOW_REQUEST_MS = 1000;

    @Around("within(ca.uwindsor.ims.controller..*)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        log.debug("-> {}", method);
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.debug("<- {} [{}ms]", method, duration);
            if (duration > SLOW_REQUEST_MS) {
                log.warn("Slow request: {} [{}ms]", method, duration);
            }
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("x {} [{}ms] - {}", method, duration, ex.getMessage());
            throw ex;
        }
    }

    @Around("within(ca.uwindsor.ims.service..*)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.trace("-> {}", method);
        try {
            Object result = joinPoint.proceed();
            log.trace("<- {}", method);
            return result;
        } catch (Throwable ex) {
            log.warn("x {} - {}", method, ex.getMessage());
            throw ex;
        }
    }
}
