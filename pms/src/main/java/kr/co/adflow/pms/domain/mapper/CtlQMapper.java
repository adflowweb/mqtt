/*
 * 
 */
package kr.co.adflow.pms.domain.mapper;

import kr.co.adflow.pms.domain.CtlQ;

// TODO: Auto-generated Javadoc
/**
 * The Interface CtlQMapper.
 */
public interface CtlQMapper {

	/**
	 * Insert q.
	 *
	 * @param ctlQ the ctl q
	 * @return the int
	 */
	int insertQ(CtlQ ctlQ);

	/**
	 * Fetch q.
	 *
	 * @param ctlQ the ctl q
	 * @return the ctl q
	 */
	CtlQ fetchQ(CtlQ ctlQ);

	/**
	 * Delete q.
	 *
	 * @param msgId the msg id
	 * @return the int
	 */
	int deleteQ(String msgId);

}
