/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Acknowledge;

// TODO: Auto-generated Javadoc
/**
 * The Interface AckDao.
 *
 * @author nadir93
 */
public interface AckDao {

	/**
	 * Gets the.
	 *
	 * @param msgID the msg id
	 * @return the acknowledge[]
	 * @throws Exception the exception
	 */
	Acknowledge[] get(int msgID) throws Exception;

}
