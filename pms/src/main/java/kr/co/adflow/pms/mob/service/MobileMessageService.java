/*
 * 
 */
package kr.co.adflow.pms.mob.service;

import kr.co.adflow.pms.mob.request.MessageReq;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserMessageService.
 */
public interface MobileMessageService {

	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the list
	 */
	public int sendMessage(MessageReq msg, String appKey) throws Exception;
	
	
	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the list
	 */
	public Integer groupListCnt(String groupTopic) throws Exception;

}
