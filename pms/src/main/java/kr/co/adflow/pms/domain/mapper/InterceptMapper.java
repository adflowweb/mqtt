package kr.co.adflow.pms.domain.mapper;

import java.util.Date;

import kr.co.adflow.pms.domain.AppKey;

public interface InterceptMapper {

	String selectCashedApplicationKey(AppKey appKey);
	
	Date selectCashedApplicationToken(AppKey appKey);

	String selectCashedUserId(String applicationKey);
	
	int getCashedMessageSizeLimit(String userId);
	
	int getCashedMessageExpiry(String userId);
	
	int getCashedMessageQos(String userId);

}
