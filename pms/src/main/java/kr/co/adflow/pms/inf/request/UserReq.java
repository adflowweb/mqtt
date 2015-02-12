package kr.co.adflow.pms.inf.request;

import org.hibernate.validator.constraints.NotEmpty;

public class UserReq {

	@NotEmpty
	private String userId;
	@NotEmpty
	private String password;
	@NotEmpty
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
