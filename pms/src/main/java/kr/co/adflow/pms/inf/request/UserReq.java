/*
 * 
 */
package kr.co.adflow.pms.inf.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class UserReq {

	/** The user id. */
	@NotEmpty
	private String userId;
	
	/** The password. */
	@NotEmpty
	private String password;
	
	/** The msg cnt limit. */
	@NotEmpty
	private int msgCntLimit;

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the msg cnt limit.
	 *
	 * @return the msg cnt limit
	 */
	public int getMsgCntLimit() {
		return msgCntLimit;
	}

	/**
	 * Sets the msg cnt limit.
	 *
	 * @param msgCntLimit the new msg cnt limit
	 */
	public void setMsgCntLimit(int msgCntLimit) {
		this.msgCntLimit = msgCntLimit;
	}

}
