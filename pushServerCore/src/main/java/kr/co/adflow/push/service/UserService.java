package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface UserService {
	User get(String userID) throws Exception;

	int post(User user) throws Exception;

	int put(User user) throws Exception;

	int delete(String userID) throws Exception;

	Token auth(User user) throws Exception;;

	// Token adminAuth(User user) throws Exception;

	User[] getAdmin() throws Exception;
}
