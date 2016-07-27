/*
 * 
 */
package kr.co.adflow.push.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class LoggingAspect.
 *
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Aspect
@Component
public class LoggingAspect {

	/** The logger. */
	Logger logger;

	/**
	 * Log before.
	 *
	 * @param joinPoint
	 *            the join point
	 */
	@Before("execution(* kr.co.adflow.push..*.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
		if (logger.isDebugEnabled()) {

		}
		// logger.debug(joinPoint.getSignature().getName() + "시작");
	}

	/**
	 * Log after.
	 *
	 * @param joinPoint
	 *            the join point
	 */
	@After("execution(* kr.co.adflow.push..*.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
		if (logger.isDebugEnabled()) {

		}
		// logger.debug(joinPoint.getSignature().getName() + "종료");
	}

}
