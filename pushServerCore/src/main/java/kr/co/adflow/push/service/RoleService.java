/*
 * 
 */
package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Role;

// TODO: Auto-generated Javadoc
/**
 * The Interface RoleService.
 *
 * @author nadir93
 * @date 2014. 7. 21.
 */
public interface RoleService {

	/**
	 * Gets the by user.
	 *
	 * @param userID the user id
	 * @return the by user
	 * @throws Exception the exception
	 */
	Role[] getByUser(String userID) throws Exception;

	/**
	 * Gets the by role.
	 *
	 * @param roleID the role id
	 * @return the by role
	 * @throws Exception the exception
	 */
	Role[] getByRole(String roleID) throws Exception;

	/**
	 * 모든 role정보가져오기.
	 *
	 * @return the role[]
	 * @throws Exception the exception
	 */
	Role[] get() throws Exception;

}
