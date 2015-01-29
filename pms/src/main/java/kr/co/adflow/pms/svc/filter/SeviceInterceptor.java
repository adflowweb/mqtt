package kr.co.adflow.pms.svc.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.domain.AppKey;


import kr.co.adflow.pms.domain.mapper.InterceptMapper;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SeviceInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SeviceInterceptor.class);
	
	private static final String SERVICE_KEY = "x-application-key";
	
	@Autowired
	private InterceptMapper interceptMapper;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		    throws Exception {
		
		String applicationKey = request.getHeader(SERVICE_KEY);
		
		if (applicationKey == null || applicationKey.trim().length() == 0) {
			
			logger.error("applicationKey error is {}", applicationKey);
			response.sendError(401);
			return false;
		} 
		
//		Map<String,String> resultMap = interceptMapper.selectCashedApplicationKey(this.getAppKey(applicationKey));
//		
//		String ipFilters = resultMap.get("ipFilters");
		
		String ipFilters = interceptMapper.selectCashedApplicationKey(this.getAppKey(applicationKey));
		
		logger.info("ipFilters {}",ipFilters);

		if (ipFilters == null || ipFilters.trim().length() == 0) {
			
			logger.error("ipFilters error is {}", ipFilters);
			response.sendError(401);
			return false;
		}
		if ("0.0.0.0".equals(ipFilters)) {
			response.sendError(401);
			return false;
		}
		
		if ("*.*.*.*".equals(ipFilters)) {
			return true;
		}
		
		return this.ipFiltering(getRemoteIpAddress(request),ipFilters);
		
	}
	
	private boolean ipFiltering(String remoteIpAddress, String ipFilters) {
		
		logger.info("remoteIpAddress :: {}",remoteIpAddress);
		

		
		return true;
	}

	private String getRemoteIpAddress(HttpServletRequest request) {
		
		return request.getRemoteAddr();
	}
	
	private AppKey getAppKey(String applicationKey) {
		
		AppKey appKey = new AppKey();
		appKey.setApplicationKey(applicationKey);
		appKey.setRole("svc");
		
		return appKey;
	}
	


}
