/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.Date;

import kr.co.adflow.pms.domain.AppKey;

// TODO: Auto-generated Javadoc
/**
 * The Interface InterceptMapper.
 */
public interface InterceptMapper {

	/**
	 * Select cashed application key.
	 *
	 * @param appKey the app key
	 * @return the string
	 */
	String selectCashedApplicationKey(AppKey appKey);

	/**
	 * Select cashed application token.
	 *
	 * @param appKey the app key
	 * @return the date
	 */
	Date selectCashedApplicationToken(AppKey appKey);
	
	/**
	 * Select cashed application token Common Auth.
	 *
	 * @param appKey the app key
	 * @return the date
	 */
	Date selectCashedApplicationTokenCmm(AppKey appKey);
	
	
	/**
	 * Select cashed application token Common Auth.
	 *
	 * @param appKey the app key
	 * @return the date
	 */
	String selectCashedApplicationKeyCmm(AppKey appKey);
	

	/**
	 * Select cashed user id.
	 *
	 * @param applicationKey the application key
	 * @return the string
	 */
	String selectCashedUserId(String applicationKey);

	/**
	 * Gets the cashed message size limit.
	 *
	 * @param userId the user id
	 * @return the cashed message size limit
	 */
	int getCashedMessageSizeLimit(String userId);

	/**
	 * Gets the cashed message expiry.
	 *
	 * @param userId the user id
	 * @return the cashed message expiry
	 */
	int getCashedMessageExpiry(String userId);

	/**
	 * Gets the cashed message qos.
	 *
	 * @param userId the user id
	 * @return the cashed message qos
	 */
	int getCashedMessageQos(String userId);

}
