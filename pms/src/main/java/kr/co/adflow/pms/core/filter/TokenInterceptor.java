package kr.co.adflow.pms.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(TokenInterceptor.class);

	@Autowired
	InterceptMapper interceptMapper;

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

		String userId = interceptMapper.selectCashedUserId(applicationKey);
		if (userId == null || userId.trim().length() == 0) {
			logger.error("User not found ..please check your request{}",
					applicationKey);
			response.sendError(401);
			return false;

		}

		return true;
	}

}
