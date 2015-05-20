/*
 * 
 */
package kr.co.adflow.pms.adm.filter;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.AppKey;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class SvcInterceptor.
 */
public class SvcInterceptor extends HandlerInterceptorAdapter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SvcInterceptor.class);

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;
	
	/** The token mapper. */
	@Autowired
	private TokenMapper tokenMapper;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		logger.info("SvcInterceptor.preHandle");

		String token = request.getHeader(StaticConfig.HEADER_APPLICATION_TOKEN);

		if (token == null || token.trim().length() == 0) {

			logger.error("token error is {}", token);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}

		AppKey tokenKey = new AppKey();

		tokenKey.setApplicationKey(token);
		tokenKey.setRole(StaticConfig.USER_ROLE_SERVICE);

		logger.info("SvcInterceptor.preHandle token {}", token);
		// 1 token 조회
		Date expiredTime = interceptMapper
				.selectCashedApplicationToken(tokenKey);

		if (expiredTime == null) {
			logger.error("sesson expired");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		// 2 만료시간 비교

		long diffTime = expiredTime.getTime() - System.currentTimeMillis();

//		long checkTime = 1000 * 60 * 20;
		// 2015-05-19 만료 시간 1시간으로 변경 요청.
		long checkTime = 1000 * 60 * 60;

		if (diffTime < checkTime) {
			// 3 만료시간 연장
			Token updateToken = new Token();
			updateToken.setTokenId(token);
			updateToken.setExpiredTime(DateUtil.afterMinute(
					pmsConfig.HEADER_APPLICATION_TOKEN_EXPIRED,
					System.currentTimeMillis()));

			tokenMapper.updateTokenExpiredTime(updateToken);
		}

		return true;

	}

}
