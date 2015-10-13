/*
 * 
 */
package kr.co.adflow.pms.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.domain.mapper.TokenMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class UserInterceptor.
 */
public class MessageInterceptor extends HandlerInterceptorAdapter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageInterceptor.class);

	/** The intercept mapper. */
	@Autowired
	private TokenMapper tokenMapper;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String applicationKey = request
				.getHeader(StaticConfig.HEADER_APPLICATION_KEY);

		if (applicationKey == null || applicationKey.trim().length() == 0) {

			logger.error(
					"applicationKey not found ..please check your request{}",
					applicationKey);
			response.sendError(401);
			return false;
		}

		boolean appKeyCheck = tokenMapper.checkToken(applicationKey);

		if (!appKeyCheck) {
			logger.error("applicationKey not found  {}", applicationKey);
			response.sendError(401);
			return false;
		}

		return true;

	}

}
