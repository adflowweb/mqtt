package kr.co.adflow.push.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author nadir93
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AuthResponseData extends ResponseData {

	private boolean auth;
	private String userID;
	private String clientID;

	public AuthResponseData(boolean auth, String userID, String clientID) {
		super();
		this.auth = auth;
		this.userID = userID;
		this.clientID = clientID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "AuthResponseData [auth=" + auth + ", userID=" + userID
				+ ", clientID=" + clientID + "]";
	}
}
