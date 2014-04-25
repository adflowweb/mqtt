package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Group;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface GroupService {

	Group get(String groupID) throws Exception;

	void post() throws Exception;

	void put() throws Exception;

	void delete() throws Exception;

}
