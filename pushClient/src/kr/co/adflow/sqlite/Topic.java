package kr.co.adflow.sqlite;

/**
 * @author nadir93
 * @date 2014. 6. 26.
 */
public class Topic {

	String userid;
	String topic;
	boolean subscribe;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	@Override
	public String toString() {
		return "Topic [userid=" + userid + ", topic=" + topic + ", subscribe="
				+ subscribe + "]";
	}

}
