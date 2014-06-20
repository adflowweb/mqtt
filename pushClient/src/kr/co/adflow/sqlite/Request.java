package kr.co.adflow.sqlite;

/**
 * @author nadir93
 * @date 2014. 6. 20.
 */
public class Request {

	// id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT, content TEXT, senddate
	// TEXT

	private int id;
	private String topic;
	private String content;
	private String senddate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getSenddate() {
		return senddate;
	}

	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", topic=" + topic + ", content="
				+ content + ", senddate=" + senddate + "]";
	}

}
