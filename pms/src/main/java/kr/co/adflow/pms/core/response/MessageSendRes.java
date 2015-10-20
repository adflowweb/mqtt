package kr.co.adflow.pms.core.response;

public class MessageSendRes {

	private String receiver;
	private String content;
	private String msgId;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "MessageSendRes [receiver=" + receiver + ", content=" + content
				+ ", msgId=" + msgId + "]";
	}
	
	

}
