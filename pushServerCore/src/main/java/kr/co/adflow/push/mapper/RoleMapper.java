/*
 * 
 */
package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Role;

// TODO: Auto-generated Javadoc
/**
 * The Interface RoleMapper.
 */
public interface RoleMapper {

	/**
	 * Gets the.
	 *
	 * @param role the role
	 * @return the role[]
	 * @throws Exception the exception
	 */
	Role[] get(String role) throws Exception;

	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 * @throws Exception the exception
	 */
	Role[] getRoles() throws Exception;

}
