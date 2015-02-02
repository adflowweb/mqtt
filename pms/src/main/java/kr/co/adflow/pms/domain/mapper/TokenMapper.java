package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.Token;

public interface TokenMapper {

	int insertToken(Token token);

	Token selectToken(String tokenId);
	
	int deleteExpiredToken(String userId);
	
	int updateTokenExpiredTime(Token token);

}
