package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Token;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 * 
 */
public interface TokenMapper {
	Token get(String token) throws Exception;

	Token getLatest(Token token) throws Exception;

	int post(Token token) throws Exception;

	int put(Token token) throws Exception;

	int delete(String token) throws Exception;
	
	//140901 <kicho> - start
	Token[] getByUser(String userID) throws Exception;
	//140901 <kicho> - end
	
	//140902 <kicho> - start
	Token[] getMultiByUser(String userID) throws Exception;
	//140902 <kicho> - end
}
