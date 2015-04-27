/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.CDR;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.MsgParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageMapper.
 */
public interface MessageMapper {

	/**
	 * Insert message.
	 *
	 * @param msg the msg
	 * @return the int
	 */
	int insertMessage(Message msg);

	/**
	 * Insert content.
	 *
	 * @param msg the msg
	 * @return the int
	 */
	int insertContent(Message msg);

	/**
	 * Select.
	 *
	 * @param msgId the msg id
	 * @return the message
	 */
	Message select(String msgId);

	/**
	 * Select list.
	 *
	 * @param params the params
	 * @return the list
	 */
	List<Message> selectList(Map<String, Object> params);

	/**
	 * Update status.
	 *
	 * @param msg the msg
	 * @return the int
	 */
	int updateStatus(Message msg);

	/**
	 * Select reservation list.
	 *
	 * @param params the params
	 * @return the list
	 */
	List<Message> selectReservationList(Map<String, Object> params);
	
	/**
	 * Select reservation list.
	 *
	 * @param params the params
	 * @return the message type
	 */
	int selectMessageType(Map<String, Object> params);

	/**
	 * Gets the svc message list.
	 *
	 * @param params the params
	 * @return the svc message list
	 */
	List<Message> getSvcMessageList(MsgParams params);

	/**
	 * Gets the svc message list cnt.
	 *
	 * @param params the params
	 * @return the svc message list cnt
	 */
	int getSvcMessageListCnt(MsgParams params);

	/**
	 * Gets the svc resevation message list.
	 *
	 * @param params the params
	 * @return the svc resevation message list
	 */
	List<Message> getSvcResevationMessageList(MsgParams params);

	/**
	 * Gets the svc resevation message list cnt.
	 *
	 * @param params the params
	 * @return the svc resevation message list cnt
	 */
	int getSvcResevationMessageListCnt(MsgParams params);

	/**
	 * Gets the message result.
	 *
	 * @param param the param
	 * @return the message result
	 */
	List<Message> getMessageResult(MsgIdsParams param);

	/**
	 * Cancel message.
	 *
	 * @param msg the msg
	 * @return the int
	 */
	int cancelMessage(Message msg);

	/**
	 * Cancel reservation list.
	 *
	 * @param params the params
	 * @return the int
	 */
	int cancelReservationList(MsgIdsParams params);
	
	
	List<CDR> getCDRList2(Map<String, Object> params);

}
