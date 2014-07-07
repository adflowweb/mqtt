package kr.co.adflow.push.mapper;

import java.util.List;

import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Sms;

/**
 * @author nadir93
 * @date 2014. 6. 11.
 */
public interface MessageMapper {
	Message get(int id) throws Exception;

	List<Message> getUndeliveredMsgs() throws Exception;

	List<Message> getUndeliveredSmsMsgs() throws Exception;

	// int getID(Message msg) throws Exception;

	int postMsg(Message msg) throws Exception;

	int putMsg(Message msg) throws Exception;

	int putIssue(Message msg) throws Exception;

	int postAck(Acknowledge ack) throws Exception;

	boolean getAck(Acknowledge ack) throws Exception;

	int deleteMsg(int id) throws Exception;

	int postContent(Message msg) throws Exception;

	int putContent(Message msg) throws Exception;

	int putIssueSms(Message msg) throws Exception;

	Message[] getReservationMsgs() throws Exception;

	int putStatus(Message msg) throws Exception;

	Acknowledge[] getAckAll(int msgID) throws Exception;

	boolean getSms(Sms sms) throws Exception;

	int postSms(Sms sms) throws Exception;

	Message[] getDeliveredMsgs() throws Exception;
}
