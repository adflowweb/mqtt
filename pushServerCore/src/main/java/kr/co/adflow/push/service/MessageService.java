package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface MessageService {

	Message get(int msgID) throws Exception;

	Message[] getMsgs() throws Exception;

	int post(Message msg) throws Exception;

	int put(Message msg) throws Exception;

	int delete(int msgID) throws Exception;

	Message[] getReservationMsgs() throws Exception;

	Message[] getDeliveredMsgs() throws Exception;
}
