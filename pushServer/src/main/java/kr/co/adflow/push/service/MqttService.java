package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Message;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
public interface MqttService {

	void destroy() throws Exception;

	boolean isConnected() throws Exception;

	String getErrorMsg() throws Exception;

	void publish(Message msg) throws Exception;
}
