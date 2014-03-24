package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
public class PersonalMessage extends Message {
	private String recipient;

	/**
	 * @return
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Override
	public String toString() {
		return "PersonalMessage [recipient=" + recipient + ", toString()="
				+ super.toString() + "]";
	}

}
