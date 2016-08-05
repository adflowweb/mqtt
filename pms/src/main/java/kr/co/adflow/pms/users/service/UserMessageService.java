/*
 * 
 */
package kr.co.adflow.pms.users.service;

import kr.co.adflow.pms.users.request.MessageReq;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserMessageService.
 */
public interface UserMessageService {

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

	/**
	 * Send message.
	 *
	 * @param appKey
	 *            the app key
	 * @param msg
	 *            the msg
	 * @return the list
	 */
	public Integer groupListCnt(String groupTopic) throws Exception;

}
