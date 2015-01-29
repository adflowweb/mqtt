package kr.co.adflow.pms.inf.request;

public class UserReq {
	
	private String userId;
	private String password;
	private int msgCntLimitDay;
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
	public int getMsgCntLimitDay() {
		return msgCntLimitDay;
	}
	public void setMsgCntLimitDay(int msgCntLimitDay) {
		this.msgCntLimitDay = msgCntLimitDay;
	}

}
