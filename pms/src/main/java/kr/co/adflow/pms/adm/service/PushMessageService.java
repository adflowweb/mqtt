/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AddressMessageReq;
import kr.co.adflow.pms.adm.request.MessageIdsReq;
import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.domain.MessageResult;

// TODO: Auto-generated Javadoc
/**
 * The Interface PushMessageService.
 */
public interface PushMessageService {

	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the list
	 */
	public List<Map<String, String>> sendMessage(String appKey, MessageReq msg) throws Exception;
	
	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the list
	 */
	public List<Map<String, String>> sendAddressMessage(String appKey, AddressMessageReq addressMsg) throws Exception;
	

	/**
	 * Gets the message result.
	 *
	 * @param msgIds the msg ids
	 * @param appKey the app key
	 * @return the message result
	 */
	public List<MessageResult> getMessageResult(MessageIdsReq msgIds,
			String appKey) throws Exception;

	/**
	 * Valid phone no.
	 *
	 * @param phoneNo the phone no
	 * @return the boolean
	 */
	public Boolean validPhoneNo(String phoneNo);

	/**
	 * Valid ufmi no.
	 *
	 * @param ufmiNo the ufmi no
	 * @return the boolean
	 */
	public Boolean validUfmiNo(String ufmiNo);

	/**
	 * Cancel message.
	 *
	 * @param appKey the app key
	 * @param msgId the msg id
	 * @return the integer
	 */
	public Integer cancelMessage(String appKey, String msgId);
	

}
