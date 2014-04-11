package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class Message {
	private String message;
	private String sender;
	private String receiver;
	private int qos;
	private boolean retained;

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + ", sender=" + sender
				+ ", receiver=" + receiver + ", qos=" + qos + ", retained="
				+ retained + "]";
	}
}