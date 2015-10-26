/*
 * 
 */
package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Acknowledge;

// TODO: Auto-generated Javadoc
/**
 * The Interface AckService.
 *
 * @author nadir93
 */
public interface AckService {

	/**
	 * Gets the.
	 *
	 * @param msgID the msg id
	 * @return the acknowledge[]
	 * @throws Exception the exception
	 */
	Acknowledge[] get(int msgID) throws Exception;

}
