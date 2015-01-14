/*
 * 
 */
package kr.co.adflow.push.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Sms.
 *
 * @author nadir93
 * @date 2014. 7. 2.
 */
public class Sms {

	// +--------+------------------+------+-----+---------+-------+
	// | Field | Type | Null | Key | Default | Extra |
	// +--------+------------------+------+-----+---------+-------+
	// | id | int(10) unsigned | NO | MUL | NULL | |
	// | userid | varchar(20) | YES | | NULL | |
	// | issue | datetime | YES | | NULL | |
	// | status | int(10) unsigned | YES | | NULL | |
	// +--------+------------------+------+-----+---------+-------+

	/** The Constant SMS_SENT. */
	public static final int SMS_SENT = 1;

	/** The Constant STATUS_PHONENUMBER_NOT_FOUND. */
	public static final int STATUS_PHONENUMBER_NOT_FOUND = 100;
	
	/** The Constant STATUS_USER_NOT_FOUND. */
	public static final int STATUS_USER_NOT_FOUND = 200;
	
	/** The id. */
	private int id;
	
	/** The user id. */
	private String userID;
	
	/** The issue. */
	private Date issue;
	
	/** The status. */
	private int status;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userID the new user id
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Gets the issue.
	 *
	 * @return the issue
	 */
	public Date getIssue() {
		return issue;
	}

	/**
	 * Sets the issue.
	 *
	 * @param issue the new issue
	 */
	public void setIssue(Date issue) {
		this.issue = issue;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sms [id=" + id + ", userID=" + userID + ", issue=" + issue
				+ ", status=" + status + "]";
	}

}
