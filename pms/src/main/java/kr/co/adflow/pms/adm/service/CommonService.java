/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommonService.
 */
public interface CommonService {

	/**
	 * Auth user.
	 *
	 * @param auth the auth
	 * @return the auth res
	 */
	AuthRes authUser(AuthReq auth) throws Exception;

}
