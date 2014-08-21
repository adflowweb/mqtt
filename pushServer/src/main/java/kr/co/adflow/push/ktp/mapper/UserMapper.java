package kr.co.adflow.push.ktp.mapper;

import kr.co.adflow.push.domain.ktp.User;

/**
 * @author nadir93
 * @date 2014. 4. 22.
 * 
 */
public interface UserMapper {
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
	 * @param sbsd
	 * @return
	 * @throws Exception
	 */
	User[] getUsersBySBSD(String sbsd) throws Exception;

	/**
	 * @param name
	 * @return
	 * @throws Exception
	 */
	User[] getUsersByName(String name) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	User[] getAllUser() throws Exception;
}
