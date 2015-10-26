/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.Map;

import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.request.ReservationCancelReq;
import kr.co.adflow.pms.core.response.AckRes;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.domain.Message;

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
	public Message sendMessage(MessageReq msg, String appKey) throws Exception;



	public MessagesListRes getMessageList(Map<String, String> params)
			throws Exception;

	// public MessagesListRes getResevationMessageList(Map<String, String>
	// params)
	// throws Exception;
	//
	// public int cancelReservationList(String appKey, ReservationCancelReq
	// reqIds)
	// throws Exception;

	public StatisticsRes getStatistics(Map<String, String> params)
			throws Exception;

	public AckRes getAckMessage(Map<String, String> params, String msgId)
			throws Exception;

}