package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface MessageDAO {
	Message get(String messageID) throws Exception;

	void post(Message msg) throws Exception;

	void put(Message msg) throws Exception;

	void delete(String messageID) throws Exception;
}
