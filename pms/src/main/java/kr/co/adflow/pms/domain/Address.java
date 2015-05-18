/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Ack.
 */
public class Address {

	/** The user Id. */
	private String userId;
	
	/** The ufmi. */
	private String ufmi;
	
	/** The ufmi Name. */
	private String ufmiName;
	
	/** The item 1. */
	private String item1;
	
	/** The item 2. */
	private String item2;
	
	/** The item 3. */
	private String item3;
	
	/** The issue time. */
	private Date issueTime;
	
	/** The issue id. */
	private String issueId;

	
	

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user Id.
	 *
	 * @param userId the new user Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * Gets the ufmiName.
	 *
	 * @return the ufmiName
	 */
	public String getUfmiName() {
		return ufmiName;
	}

	/**
	 * Sets the ufmiName.
	 *
	 * @param ufmiName the new ufmiName
	 */
	public void setUfmiName(String ufmiName) {
		this.ufmiName = ufmiName;
	}

	/**
	 * Gets the item1.
	 *
	 * @return the item1
	 */
	public String getItem1() {
		return item1;
	}

	/**
	 * Sets the item1.
	 *
	 * @param item1 the new item1
	 */
	public void setItem1(String item1) {
		this.item1 = item1;
	}

	/**
	 * Gets the item2.
	 *
	 * @return the item2
	 */
	public String getItem2() {
		return item2;
	}

	/**
	 * Sets the item2.
	 *
	 * @param item2 the new item2
	 */
	public void setItem2(String item2) {
		this.item2 = item2;
	}

	/**
	 * Gets the item3.
	 *
	 * @return the item3
	 */
	public String getItem3() {
		return item3;
	}

	/**
	 * Sets the item3.
	 *
	 * @param item3 the new item3
	 */
	public void setItem3(String item3) {
		this.item3 = item3;
	}

	/**
	 * Gets the issue time.
	 *
	 * @return the issue time
	 */
	public Date getIssueTime() {
		return issueTime;
	}

	/**
	 * Sets the issue time.
	 *
	 * @param issueTime the new issue time
	 */
	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}

	/**
	 * Gets the issue id.
	 *
	 * @return the issue id
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * Sets the issue id.
	 *
	 * @param issueId the new issue id
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	@Override
	public String toString() {
		return "Address [userId=" + userId + ", ufmi=" + ufmi + ", ufmiName="
				+ ufmiName + ", item1=" + item1 + ", item2=" + item2
				+ ", item3=" + item3 + ", issueTime=" + issueTime
				+ ", issueId=" + issueId + "]";
	}


	
	
	

}
