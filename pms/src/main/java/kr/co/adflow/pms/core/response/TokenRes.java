/*
 * 
 */
package kr.co.adflow.pms.core.response;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenRes.
 */
public class TokenRes {

	private String userId;

	private String token;

	private String serverId;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

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
		return "TokenRes [userId=" + userId + ", token=" + token
				+ ", serverId=" + serverId + "]";
	}

}
