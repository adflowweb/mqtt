package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.User;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 *
 */
public interface UserMapper {
	User get(String userID) throws Exception;

	void post(User user) throws Exception;

	void put(User user) throws Exception;

	void delete(String userID) throws Exception;
}
