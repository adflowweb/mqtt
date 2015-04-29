/*
 * 
 */
package kr.co.adflow.pms.users.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.domain.push.mapper.ValidationMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class UserInterceptor.
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(UserInterceptor.class);


	/** The intercept mapper. */
	@Autowired
	private ValidationMapper validationMapper;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String applicationKey = request
				.getHeader(StaticConfig.HEADER_APPLICATION_KEY);

		if (applicationKey == null || applicationKey.trim().length() == 0) {

			logger.error("applicationKey error is {}", applicationKey);
			response.sendError(401);
			return false;
		}

		// Map<String,String> resultMap =
		// interceptMapper.selectCashedApplicationKey(this.getAppKey(applicationKey));
		//
		// String ipFilters = resultMap.get("ipFilters");

		boolean  appKeyCheck = validationMapper.checkToken(applicationKey);
		
		
		if (!appKeyCheck) {
			logger.error("applicationKey not serche {}", applicationKey);
			response.sendError(401);
			return false;
		}

		return true;

	}

}
