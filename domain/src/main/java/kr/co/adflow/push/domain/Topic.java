/*
 * 
 */
package kr.co.adflow.push.domain;

// TODO: Auto-generated Javadoc
/**
 * The Class Topic.
 *
 * @author nadir93
 * @date 2014. 4. 23.
 */
public class Topic {

	/** The user id. */
	private String userID;
	
	/** The topic. */
	private String topic;

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userID the new user id
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Sets the topic.
	 *
	 * @param topic the new topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [userID=" + userID + ", topic=" + topic + "]";
	}

}
