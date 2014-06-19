package kr.co.adflow.push.mapper;

import java.util.List;

import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 6. 11.
 */
public interface MessageMapper {
	Message get(int id) throws Exception;

	List<Message> getUndeliveredMsg() throws Exception;

	// int getID(Message msg) throws Exception;

	int postMsg(Message msg) throws Exception;

	int putMsg(Message msg) throws Exception;

	int putIssue(Message msg) throws Exception;

	int postAck(Acknowledge ack) throws Exception;

	int deleteMsg(int id) throws Exception;

	int postContent(Message msg) throws Exception;

	int putContent(Message msg) throws Exception;
}
