package kr.co.adflow.sqlite;

/**
 * @author nadir93
 * @date 2014. 6. 19.
 */
public class User {

	private String userID;
	private String password;
	private String tokenID;
	private boolean currentUser;

	public User() {
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTokenID() {
		return tokenID;
	}

	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}

	public boolean isCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(boolean currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", password=" + password
				+ ", tokenID=" + tokenID + ", currentUser=" + currentUser + "]";
	}

}
