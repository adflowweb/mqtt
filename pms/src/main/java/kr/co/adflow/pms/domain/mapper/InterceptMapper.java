package kr.co.adflow.pms.domain.mapper;

import java.util.Map;

import kr.co.adflow.pms.domain.AppKey;

public interface InterceptMapper {
	
	String selectCashedApplicationKey(AppKey appKey);
	
	String selectCashedUserId(String applicationKey);

}
