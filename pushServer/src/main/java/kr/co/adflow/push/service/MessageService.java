package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface MessageService {

	Message get(int messageID) throws Exception;

	void post(Message msg) throws Exception;

	void put(Message msg) throws Exception;

	void delete(int messageID) throws Exception;
}
