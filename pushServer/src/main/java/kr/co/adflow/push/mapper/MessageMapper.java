package kr.co.adflow.push.mapper;

import java.util.List;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 6. 11.
 */
public interface MessageMapper {
	Message get(int id) throws Exception;

	List<Message> getReservation() throws Exception;

	int getID(Message msg) throws Exception;

	int post(Message msg) throws Exception;

	int put(Message msg) throws Exception;

	int delete(int id) throws Exception;

	int postContent(Message msg) throws Exception;
}
