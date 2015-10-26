/*
 * 
 */
package kr.co.adflow.pms.core.service;

import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenInfoRes;
import kr.co.adflow.pms.core.response.TokenRes;
import kr.co.adflow.pms.domain.Token;

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
	TokenRes createToken(TokenReq userInfo, String requestUserId)
			throws Exception;

	boolean authToken(String token) throws Exception;

	Token getTokenInfo(String token) throws Exception;

}
