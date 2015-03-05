/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.MessageReq;

// TODO: Auto-generated Javadoc
/**
 * The Interface SvcAdmService.
 */
public interface SvcAdmService {

	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the list
	 */
	public List<Map<String, String>> sendMessage(String appKey, MessageReq msg);

}
