package kr.co.adflow.pms.core.response;

public class UserRes {

	private String userId;
	private String token;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserRes [userId=" + userId + ", token=" + token + "]";
	}

}
