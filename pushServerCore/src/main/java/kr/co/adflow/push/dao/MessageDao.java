package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface MessageDao {
	Message get(int msgID) throws Exception;

	int post(Message msg) throws Exception;

	int put(Message msg) throws Exception;

	int delete(int msgID) throws Exception;

	Message[] getReservationMsgs() throws Exception;
}
