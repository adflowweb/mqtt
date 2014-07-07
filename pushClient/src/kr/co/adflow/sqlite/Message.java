package kr.co.adflow.sqlite;

/**
 * @author nadir93
 * @date 2014. 6. 20.
 */
public class Message {
	private int id;
	private String userID;
	private boolean ack;
	private int type;
	private String content;
	private String receivedate;
	private String category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(String receivedate) {
		this.receivedate = receivedate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", userID=" + userID + ", ack=" + ack
				+ ", type=" + type + ", content=" + content + ", receivedate="
				+ receivedate + ", category=" + category + "]";
	}

}
