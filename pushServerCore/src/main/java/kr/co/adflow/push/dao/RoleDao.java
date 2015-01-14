/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Role;

// TODO: Auto-generated Javadoc
/**
 * The Interface RoleDao.
 *
 * @author nadir93
 * @date 2014. 7. 21.
 */
public interface RoleDao {

	/**
	 * Gets the.
	 *
	 * @param roleID the role id
	 * @return the role[]
	 * @throws Exception the exception
	 */
	Role[] get(String roleID) throws Exception;

	/**
	 * 모든 role 정보가져오기.
	 *
	 * @return the role[]
	 * @throws Exception the exception
	 */
	Role[] get() throws Exception;

}
