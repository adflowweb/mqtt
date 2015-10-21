package kr.co.adflow.pms.core.response;

public class UserUpdateRes {

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserUpdateRes [userName=" + userName + "]";
	}

}
