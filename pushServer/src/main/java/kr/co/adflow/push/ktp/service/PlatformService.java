/*
 * 
 */
package kr.co.adflow.push.ktp.service;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.domain.ktp.request.SessionClean;
import kr.co.adflow.push.domain.ktp.request.SystemMessage;
import kr.co.adflow.push.domain.ktp.request.Ufmi;
import kr.co.adflow.push.domain.ktp.request.UserID;
import kr.co.adflow.push.domain.ktp.request.UserMessage;

// TODO: Auto-generated Javadoc
/**
 * The Interface PlatformService.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface PlatformService {

	/**
	 * Send precheck.
	 *
	 * @param topicName
	 *            the topic name
	 */
	public void sendPrecheck(String topicName);

	/**
	 * Modify fw info.
	 *
	 * @param fwInfo
	 *            the fw info
	 */
	public void modifyFwInfo(FwInfo fwInfo);

	/**
	 * Modify dig info.
	 *
	 * @param gigInfo
	 *            the gig info
	 */
	public void modifyDigInfo(DigInfo gigInfo);

	/**
	 * Send message.
	 *
	 * @param message
	 *            the message
	 */
	public void sendMessage(Message message);

	/**
	 * Modify keep alive time.
	 *
	 * @param keepAliveTime
	 *            the keep alive time
	 */
	public void modifyKeepAliveTime(KeepAliveTime keepAliveTime);

	/**
	 * Send user message.
	 *
	 * @param message
	 *            the message
	 */
	public void sendUserMessage(UserMessage message);

	public void sendSystemMessage(SystemMessage systemMessage);

	/**
	 * Valid user id.
	 *
	 * @param userID
	 *            the user id
	 * @return true, if successful
	 */
	public boolean validUserID(UserID userID);

	/**
	 * Valid ufmi.
	 *
	 * @param ufmi
	 *            the ufmi
	 * @return true, if successful
	 */
	public boolean validUFMI(Ufmi ufmi);

	public void sessionCleanClient(SessionClean sessionClean);

}
