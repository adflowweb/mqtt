/*
 * 
 */
package kr.co.adflow.pms.adm.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonInterceptor.
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(CommonInterceptor.class);

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;
	
	/** The token mapper. */
	@Autowired
	private TokenMapper tokenMapper;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		logger.info("CommonInterceptor.preHandle");

		String token = request.getHeader(StaticConfig.HEADER_APPLICATION_TOKEN);

		if (token == null || token.trim().length() == 0) {

			logger.error("token error is {}", token);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		return true;

	}

}
