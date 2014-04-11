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
	private String tocken;

	public AuthResponseData(boolean auth, String userID) {
		super();
		this.auth = auth;
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public String getTocken() {
		return tocken;
	}

	public void setTocken(String tocken) {
		this.tocken = tocken;
	}

	@Override
	public String toString() {
		return "AuthResponseData [auth=" + auth + ", userID=" + userID
				+ ", tocken=" + tocken + "]";
	}
}
