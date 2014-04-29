package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface UserDAO {
	User get(String userID) throws Exception;

	int post(User user) throws Exception;

	int put(User user) throws Exception;

	int delete(String userID) throws Exception;
}
