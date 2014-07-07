package kr.co.adflow.push.service;

import kr.co.adflow.push.domain.Message;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
public interface MqttService {

	void destroy() throws Exception;

	boolean isConnected() throws Exception;

	String getErrorMsg() throws Exception;

	IMqttDeliveryToken publish(Message msg) throws Exception;

	double getTps() throws Exception;
}
