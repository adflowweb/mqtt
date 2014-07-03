package kr.co.adflow.push.domain;

import java.util.Date;

/**
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

	public static final int SMS_SENT = 1;
	public static final int STATUS_PHONENUMBER_NOT_FOUND = 2;
	public static final int STATUS_USER_NOT_FOUND = 3;
	private int id;
	private String userID;
	private Date issue;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getIssue() {
		return issue;
	}

	public void setIssue(Date issue) {
		this.issue = issue;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", userID=" + userID + ", issue=" + issue
				+ ", status=" + status + "]";
	}

}
