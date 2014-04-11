package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SendMessageResponseData extends ResponseData {

	private boolean sendMessage;

	public SendMessageResponseData(boolean sendMessage) {
		super();
		this.sendMessage = sendMessage;
	}

	public boolean isSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(boolean sendMessage) {
		this.sendMessage = sendMessage;
	}

	@Override
	public String toString() {
		return "SendMessageResponseData [sendMessage=" + sendMessage + "]";
	}
}
