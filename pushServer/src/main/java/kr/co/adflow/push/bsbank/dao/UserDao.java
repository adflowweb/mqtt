package kr.co.adflow.push.bsbank.dao;

import kr.co.adflow.push.domain.bsbank.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface UserDao {
	/**
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	User get(String userID) throws Exception;

	/**
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	User[] getUsersByDepartment(String dept) throws Exception;

	/**
	 * @param name
	 * @return
	 * @throws Exception
	 */
	User[] getUsersByName(String name) throws Exception;
}
