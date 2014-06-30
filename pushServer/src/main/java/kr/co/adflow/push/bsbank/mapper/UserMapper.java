package kr.co.adflow.push.bsbank.mapper;

import kr.co.adflow.push.domain.bsbank.User;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 * 
 */
public interface UserMapper {
	User get(String userID) throws Exception;

	User[] getUsersByDepartment(String dept);
}
