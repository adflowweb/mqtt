/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Token;

// TODO: Auto-generated Javadoc
/**
 * The Interface TokenMapper.
 */
public interface TokenMapper {

	/**
	 * Insert token.
	 * 
	 * @param token
	 *            the token
	 * @return the int
	 */
	int insertToken(Token token);

	/**
	 * Select token.
	 * 
	 * @param tokenId
	 *            the token id
	 * @return the token
	 */
	Token selectToken(String tokenId);

	Token selectUserid(String tokenId);

	/**
	 * Delete expired token.
	 * 
	 * @param userId
	 *            the user id
	 * @return the int
	 */
	int deleteExpiredToken(String userId);

	/**
	 * Update token expired time.
	 * 
	 * @param token
	 *            the token
	 * @return the int
	 */
	int updateTokenExpiredTime(Token token);

	/**
	 * Delete user token.
	 * 
	 * @param userId
	 *            the user id
	 * @return the int
	 */
	int deleteUserToken(String userId);

	List<Token> getApiCode(String userId);

	/**
	 * Select appKey.
	 * 
	 * @param String
	 *            the userId
	 * @return the String
	 */
	String selectApplicationKey(String userId);

	Token getLatest(Token tokenID);

	boolean checkToken(String token);

}
