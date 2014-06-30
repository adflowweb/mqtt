package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Topic;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface GroupService {

	Topic[] get(String userID) throws Exception;

	int post(Topic grp) throws Exception;

	int delete(String userID, String topic) throws Exception;

}
