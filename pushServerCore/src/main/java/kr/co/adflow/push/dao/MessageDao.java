/*
 * 
 */
package kr.co.adflow.push.dao;

import java.util.Map;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.MessagesRes;
import kr.co.adflow.push.domain.ktp.MsgParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageDao.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface MessageDao {

	/**
	 * Gets the.
	 *
	 * @param msgID
	 *            the msg id
	 * @return the message
	 * @throws Exception
	 *             the exception
	 */
	Message get(String msgID) throws Exception;

	/**
	 * Post.
	 *
	 * @param msg
	 *            the msg
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	int post(Message msg) throws Exception;

	/**
	 * Put.
	 *
	 * @param msg
	 *            the msg
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	int put(Message msg) throws Exception;

	/**
	 * Put issue.
	 *
	 * @param msg
	 *            the msg
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	int putIssue(Message msg) throws Exception;

	/**
	 * Delete.
	 *
	 * @param msgID
	 *            the msg id
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	int delete(String msgID) throws Exception;

	/**
	 * Gets the reservation msgs.
	 *
	 * @return the reservation msgs
	 * @throws Exception
	 *             the exception
	 */
	Message[] getReservationMsgs() throws Exception;

	/**
	 * Gets the delivered msgs.
	 *
	 * @return the delivered msgs
	 * @throws Exception
	 *             the exception
	 */
	Message[] getDeliveredMsgs() throws Exception;

	/**
	 * Gets the Message List.
	 *
	 * @return the MessagesRes
	 * @throws Exception
	 *             the exception
	 */
	MessagesRes getMessageList(MsgParams msgParams) throws Exception;
}
