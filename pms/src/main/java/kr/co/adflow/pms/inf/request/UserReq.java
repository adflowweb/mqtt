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
	
	/** The ufmi. */
	private String ufmi;
	
	/** The saId. */
	private String saId;
	
	/** The groupTopics. */
	private String groupTopics;
	
	/** The status. */
	private int status;
	
	/** The user Name. */
	private String userName;
	
	

	/**
	 * Gets the user Name.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user Name.
	 *
	 * @param userName the new userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the ufmi.
	 *
	 * @return the ufmi
	 */
	public String getUfmi() {
		return ufmi;
	}

	/**
	 * Sets the ufmi.
	 *
	 * @param ufmi the new ufmi
	 */
	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

	/**
	 * Gets the saId.
	 *
	 * @return the saId
	 */
	public String getSaId() {
		return saId;
	}

	/**
	 * Sets the saId.
	 *
	 * @param saId the new saId
	 */
	public void setSaId(String saId) {
		this.saId = saId;
	}

	/**
	 * Gets the groupTopics.
	 *
	 * @return the groupTopics
	 */
	public String getGroupTopics() {
		return groupTopics;
	}

	/**
	 * Sets the groupTopics.
	 *
	 * @param groupTopics the new groupTopics
	 */
	public void setGroupTopics(String groupTopics) {
		this.groupTopics = groupTopics;
	}

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

	@Override
	public String toString() {
		return "UserReq [userId=" + userId + ", password=" + password
				+ ", msgCntLimit=" + msgCntLimit + ", ufmi=" + ufmi + ", saId="
				+ saId + ", groupTopics=" + groupTopics + ", status=" + status
				+ "]";
	}
	
	

}
