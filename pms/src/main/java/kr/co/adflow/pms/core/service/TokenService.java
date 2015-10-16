/*
 * 
 */
package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenRes;

// TODO: Auto-generated Javadoc
/**
 * The Interface CommonService.
 */
public interface TokenService {

	/**
	 * Auth user.
	 * 
	 * @param auth
	 *            the auth
	 * @return the auth res
	 */
	TokenRes createToken(TokenReq auth,String requestUserId) throws Exception;

	boolean authToken(String token) throws Exception;

}
