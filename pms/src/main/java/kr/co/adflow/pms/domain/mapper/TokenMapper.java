/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.Token;

// TODO: Auto-generated Javadoc
/**
 * The Interface TokenMapper.
 */
public interface TokenMapper {

	/**
	 * Insert token.
	 *
	 * @param token the token
	 * @return the int
	 */
	int insertToken(Token token);

	/**
	 * Select token.
	 *
	 * @param tokenId the token id
	 * @return the token
	 */
	Token selectToken(String tokenId);

	/**
	 * Delete expired token.
	 *
	 * @param userId the user id
	 * @return the int
	 */
	int deleteExpiredToken(String userId);

	/**
	 * Update token expired time.
	 *
	 * @param token the token
	 * @return the int
	 */
	int updateTokenExpiredTime(Token token);

	/**
	 * Delete user token.
	 *
	 * @param userId the user id
	 * @return the int
	 */
	int deleteUserToken(String userId);

}
