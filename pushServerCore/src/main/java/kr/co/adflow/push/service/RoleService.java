package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Role;

/**
 * @author nadir93
 * @date 2014. 7. 21.
 */
public interface RoleService {

	Role[] getByUser(String userID) throws Exception;

	Role[] getByRole(String roleID) throws Exception;

	/**
	 * 모든 role정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Role[] get() throws Exception;

}
