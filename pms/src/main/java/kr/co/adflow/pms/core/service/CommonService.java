/*
 * 
 */
package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.AuthReq;
import kr.co.adflow.pms.core.response.AuthRes;

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

}
