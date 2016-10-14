/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;
import kr.co.adflow.pms.domain.Leader;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommonService.
 */
public interface CommonService {

	/**
	 * Auth user.
	 *
	 * @param auth
	 *            the auth
	 * @return the auth res
	 */
	AuthRes authUser(AuthReq auth) throws Exception;

	/**
	 * Auth token.
	 *
	 * @param auth
	 *            the token
	 * @return the boolean
	 */
	boolean authToken(String token) throws Exception;

	/**
	 * Auth key.
	 *
	 * @param auth
	 *            the key
	 * @return the boolean
	 */
	boolean authKey(String key) throws Exception;

	Leader getLeader() throws Exception;

}
