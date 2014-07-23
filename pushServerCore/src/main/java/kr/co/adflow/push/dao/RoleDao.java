package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Role;

/**
 * @author nadir93
 * @date 2014. 7. 21.
 */
public interface RoleDao {

	Role[] get(String userID) throws Exception;

}
