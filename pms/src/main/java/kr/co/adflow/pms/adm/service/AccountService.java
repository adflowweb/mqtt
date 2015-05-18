/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.domain.User;

// TODO: Auto-generated Javadoc
/**
 * The Interface AccountService.
 */
public interface AccountService {

	/**
	 * Retrieve account.
	 *
	 * @param appKey the app key
	 * @return the user
	 */
	public User retrieveAccount(String appKey) throws Exception;

	/**
	 * Modify password.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the int
	 */
	public int modifyPassword(PasswordReq req, String appKey) throws Exception;

	/**
	 * Modify account.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the int
	 */
	public int modifyAccount(AccountReq req, String appKey) throws Exception;
	
	
	/**
	 * Modify password.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the int
	 */
	public int modifyUserName(UserReq req, String appKey) throws Exception;

}
