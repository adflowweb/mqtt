/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.domain.Ack;
import kr.co.adflow.pms.domain.AckCallback;

// TODO: Auto-generated Javadoc
/**
 * The Interface AckMapper.
 */
public interface AckMapper {

	/**
	 * Insert ack.
	 *
	 * @param ack the ack
	 * @return the int
	 */
	int insertAck(Ack ack);

	/**
	 * Gets the callback list.
	 *
	 * @param params the params
	 * @return the callback list
	 */
	List<AckCallback> getCallbackList(Map<String, Object> params);

	/**
	 * Update ack callback.
	 *
	 * @param params the params
	 * @return the int
	 */
	int updateAckCallback(Map<String, Object> params);

}
