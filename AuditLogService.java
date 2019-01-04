package com.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@EnableAspectJAutoProxy
@Component
public class AuditLogService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditLogService.class);

	@Pointcut("execution(* com.dao..*.*(..))")
	public void methodPointcut() {
	}

	@Around("methodPointcut()")
	public Object profile(final ProceedingJoinPoint pjp) throws Throwable {
		final StopWatch stopWatch = new StopWatch();
		try {
			final long start = getCurrentTime();
			stopWatch.start();
			final Object output = pjp.proceed();
			final long elapsedTime = getCurrentTime() - start;
			LOGGER.info(pjp.getSignature().getName() + " Execution Time is " + elapsedTime + " ms");
			return output;
		} finally {
			stopWatch.stop();
		}
	}

	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
