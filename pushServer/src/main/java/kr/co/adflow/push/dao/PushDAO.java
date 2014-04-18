package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.AvailableResponseData;
import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public interface PushDAO {
	/**
	 * @return
	 * @throws Exception
	 */
	AvailableResponseData isAvailable() throws Exception;

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	boolean sendMessage(Message msg) throws Exception;

	/**
	 * @throws Exception
	 */
	void shutdown() throws Exception;
}