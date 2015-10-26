package kr.co.adflow.push.service;

/**
 * @author nadir93
 * 
 */
public interface PushService {

	public String auth(String url, String userID, String deviceID)
			throws Exception;

	public void publish(String topic, byte[] payload, int qos) throws Exception;

	public void preCheck(String sender, String topic) throws Exception;

	public boolean isConnected();

	public void unsubscribe(String topic) throws Exception;

	public void subscribe(String topic, int qos) throws Exception;

	public int getLostCout();

}
