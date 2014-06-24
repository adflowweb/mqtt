package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Group;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface GroupDao {

	Group[] get(String userID) throws Exception;

	int post(Group grp) throws Exception;

	int delete(String userID, String topic) throws Exception;

}
