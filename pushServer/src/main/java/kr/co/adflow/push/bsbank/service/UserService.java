package kr.co.adflow.push.bsbank.service;

import kr.co.adflow.push.domain.bsbank.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface UserService {
	/**
	 * 유저정보가져오기
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	User get(String userID) throws Exception;

	/**
	 * 부서소속지원가져오기
	 * 
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	User[] getUsersByDepartment(String dept) throws Exception;

	/**
	 * 이름으로 직원정보가져오기
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	User[] getUsersByName(String name) throws Exception;
}
