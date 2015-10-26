/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import java.util.List;

import kr.co.adflow.pms.domain.Ack;
import kr.co.adflow.pms.domain.AckData;
import kr.co.adflow.pms.domain.MsgParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface AckMapper.
 */
public interface AckMapper {

	/**
	 * Insert ack.
	 * 
	 * @param ack
	 *            the ack
	 * @return the int
	 */
	int insertAck(Ack ack);

	List<AckData> selectAckMessage(MsgParams msgParams);

}
