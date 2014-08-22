package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface UserDao {
	User get(String userID) throws Exception;

	int post(User user) throws Exception;

	int put(User user) throws Exception;

	//KTP-skip-start
//	int putWithoutRole(User user) throws Exception;
	//KTP-skip-end

	int delete(String userID) throws Exception;

	boolean auth(User user) throws Exception;

	User[] getAdmin() throws Exception;
}
