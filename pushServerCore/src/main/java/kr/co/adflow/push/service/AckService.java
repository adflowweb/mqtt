package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Acknowledge;

/**
 * @author nadir93
 * 
 */
public interface AckService {

	/**
	 * @param msgID
	 * @return
	 * @throws Exception
	 */
	Acknowledge[] get(int msgID) throws Exception;

}
