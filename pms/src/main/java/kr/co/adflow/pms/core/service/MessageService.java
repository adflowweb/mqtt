/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.Map;

import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.request.MessageReq;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserMessageService.
 */
public interface MessageService {

	/**
	 * Send message.
	 * 
	 * @param appKey
	 *            the app key
	 * @param msg
	 *            the msg
	 * @return the list
	 */
	public int sendMessage(MessageReq msg, String appKey) throws Exception;

	public MessagesRes getMessageListById(Map<String, String> params)
			throws Exception;

	public MessagesRes getResevationMessageList(Map<String, String> params)
			throws Exception;

	public int cancelReservationList(String appKey, ReservationCancelReq reqIds)
			throws Exception;

}