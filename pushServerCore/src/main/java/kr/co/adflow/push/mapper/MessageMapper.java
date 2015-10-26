/*
 * 
 */
package kr.co.adflow.push.mapper;

import java.util.List;

import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Sms;
import kr.co.adflow.push.domain.ktp.MsgParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageMapper.
 *
 * @author nadir93
 * @date 2014. 6. 11.
 */
public interface MessageMapper {
	
	/**
	 * Gets the.
	 *
	 * @param id the id
	 * @return the message
	 * @throws Exception the exception
	 */
	Message get(int id) throws Exception;

	/**
	 * Gets the undelivered msgs.
	 *
	 * @return the undelivered msgs
	 * @throws Exception the exception
	 */
	List<Message> getUndeliveredMsgs() throws Exception;

	/**
	 * Gets the undelivered sms msgs.
	 *
	 * @return the undelivered sms msgs
	 * @throws Exception the exception
	 */
	List<Message> getUndeliveredSmsMsgs() throws Exception;

	// int getID(Message msg) throws Exception;

	/**
	 * Post msg.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int postMsg(Message msg) throws Exception;

	/**
	 * Put msg.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int putMsg(Message msg) throws Exception;

	/**
	 * Put issue.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int putIssue(Message msg) throws Exception;

	/**
	 * Post ack.
	 *
	 * @param ack the ack
	 * @return the int
	 * @throws Exception the exception
	 */
	int postAck(Acknowledge ack) throws Exception;

	/**
	 * Gets the ack.
	 *
	 * @param ack the ack
	 * @return the ack
	 * @throws Exception the exception
	 */
	boolean getAck(Acknowledge ack) throws Exception;

	/**
	 * Delete msg.
	 *
	 * @param id the id
	 * @return the int
	 * @throws Exception the exception
	 */
	int deleteMsg(int id) throws Exception;

	/**
	 * Post content.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int postContent(Message msg) throws Exception;

	/**
	 * Put content.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int putContent(Message msg) throws Exception;

	/**
	 * Put issue sms.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int putIssueSms(Message msg) throws Exception;

	/**
	 * Gets the reservation msgs.
	 *
	 * @return the reservation msgs
	 * @throws Exception the exception
	 */
	Message[] getReservationMsgs() throws Exception;

	/**
	 * Put status.
	 *
	 * @param msg the msg
	 * @return the int
	 * @throws Exception the exception
	 */
	int putStatus(Message msg) throws Exception;

	/**
	 * Gets the ack all.
	 *
	 * @param msgID the msg id
	 * @return the ack all
	 * @throws Exception the exception
	 */
	Acknowledge[] getAckAll(int msgID) throws Exception;

	/**
	 * Gets the sms.
	 *
	 * @param sms the sms
	 * @return the sms
	 * @throws Exception the exception
	 */
	boolean getSms(Sms sms) throws Exception;

	/**
	 * Post sms.
	 *
	 * @param sms the sms
	 * @return the int
	 * @throws Exception the exception
	 */
	int postSms(Sms sms) throws Exception;

	/**
	 * Gets the delivered msgs.
	 *
	 * @return the delivered msgs
	 * @throws Exception the exception
	 */
	Message[] getDeliveredMsgs() throws Exception;
	
	/**
	 * Gets the Message List Count.
	 *
	 * @return the int
	 * @throws Exception the exception
	 */
	int getMessageListCnt(MsgParams msgParams) throws Exception;
	
	/**
	 * Gets the delivered msgs.
	 *
	 * @return the List<Message>
	 * @throws Exception the exception
	 */
	List<Message> getMessageList(MsgParams msgParams) throws Exception;
}
