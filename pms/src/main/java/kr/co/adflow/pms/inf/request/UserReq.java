package kr.co.adflow.pms.inf.request;

public class UserReq {

	private String userId;
	private String password;
	private int msgCntLimit;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMsgCntLimit() {
		return msgCntLimit;
	}

	public void setMsgCntLimit(int msgCntLimit) {
		this.msgCntLimit = msgCntLimit;
	}

}
