package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.ResponseData;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface TokenService {

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	ResponseData get(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	ResponseData post(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	ResponseData put(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	ResponseData delete(String token) throws Exception;

	/**
	 * @param token
	 * @return
	 * @throws Exception
	 */
	ResponseData validate(String token) throws Exception;
}
