package kr.co.adflow.push.domain;

/**
 * @author nadir93
 * @date 2014. 4. 23.
 * 
 */
public class Group {

	private String userID;
	private String topic;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Group [userID=" + userID + ", topic=" + topic + "]";
	}

}
