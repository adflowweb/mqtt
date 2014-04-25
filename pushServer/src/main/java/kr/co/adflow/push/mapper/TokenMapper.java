package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Token;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 * 
 */
public interface TokenMapper {
	Token get(String token) throws Exception;

	void post(Token token) throws Exception;

	void put(Token token) throws Exception;

	void delete(String token) throws Exception;
}
