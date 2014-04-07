package kr.co.adflow.push.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Aspect
@Component
public class LoggingAspect {
	Logger logger;

	/**
	 * @param joinPoint
	 */
	@Before("execution(* kr.co.adflow.push..*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature()
				.getDeclaringTypeName());
		logger.debug(joinPoint.getSignature().getName() + "시작");
	}

	/**
	 * @param joinPoint
	 */
	@After("execution(* kr.co.adflow.push..*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature()
				.getDeclaringTypeName());
		logger.debug(joinPoint.getSignature().getName() + "종료");
	}

}
