package kr.co.adflow.sqlite;

/**
 * @author nadir93
 * @date 2014. 6. 20.
 */
public class Job {

	// id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT, content TEXT, senddate
	// TEXT
	public static int PUBLISH = 0;
	public static int SUBSCRIBE = 1;
	public static int UNSUBSCRIBE = 2;

	private int id;
	private int type;
	private String topic;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", type=" + type + ", topic=" + topic
				+ ", content=" + content + "]";
	}

}
