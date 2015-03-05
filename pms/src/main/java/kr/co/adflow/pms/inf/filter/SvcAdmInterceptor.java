/*
 * 
 */
package kr.co.adflow.pms.inf.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.domain.AppKey;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class SvcAdmInterceptor.
 */
public class SvcAdmInterceptor extends HandlerInterceptorAdapter {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SvcAdmInterceptor.class);

	/** The Constant DEFAULT_ROLE. */
	private static final String DEFAULT_ROLE = "svcadm";

	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

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

		String ipFilters = interceptMapper.selectCashedApplicationKey(this
				.getAppKey(applicationKey));

		logger.info("ipFilters {}", ipFilters);

		if (ipFilters == null || ipFilters.trim().length() == 0) {

			logger.error("ipFilters error is {}", ipFilters);
			response.sendError(401);
			return false;
		}

		String remoteIpAddress = this.getRemoteIpAddress(request);
		logger.info("remoteIpAddress :: {}", remoteIpAddress);

		if ("0.0.0.0".equals(ipFilters)) {
			response.sendError(401);
			return false;
		}

		if ("*.*.*.*".equals(ipFilters)) {
			return true;
		}

		if (!this.ipFiltering(remoteIpAddress, ipFilters)) {
			response.sendError(401);
			return false;
		}

		return true;

	}

	/**
	 * Ip filtering.
	 *
	 * @param remoteIpAddress the remote ip address
	 * @param ipFilters the ip filters
	 * @return true, if successful
	 */
	private boolean ipFiltering(String remoteIpAddress, String ipFilters) {

		logger.info("remoteIpAddress :: {}", remoteIpAddress);
		if (ipFilters.indexOf(StaticConfig.INTERCEPTER_IP_FILTER_DELIM) == -1) {
			if (remoteIpAddress.equals(ipFilters.trim())) {
				return true;
			} else {
				return false;
			}

		} else {
			String[] ipArray = ipFilters
					.split(StaticConfig.INTERCEPTER_IP_FILTER_DELIM);
			for (int i = 0; i < ipArray.length; i++) {

				if (remoteIpAddress.equals(ipArray[i].trim())) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Gets the remote ip address.
	 *
	 * @param request the request
	 * @return the remote ip address
	 */
	private String getRemoteIpAddress(HttpServletRequest request) {
		// TODO 필요시 x-forward-for 로직 추가
		return request.getRemoteAddr().trim();
	}

	/**
	 * Gets the app key.
	 *
	 * @param applicationKey the application key
	 * @return the app key
	 */
	private AppKey getAppKey(String applicationKey) {

		AppKey appKey = new AppKey();
		appKey.setApplicationKey(applicationKey);
		appKey.setRole(DEFAULT_ROLE);

		return appKey;
	}

}
