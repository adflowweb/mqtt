/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Template.
 */
public class Template {

	/** The user Id. */
	private String userId;
	
	/** The template id. */
	private String templateId;
	
	/** The template Name. */
	private String templateName;
	
	/** The template msg. */
	private String templateMsg;
	
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
	 * Gets the template id.
	 *
	 * @return the template id
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * Sets the template Id.
	 *
	 * @param templateId the new template Id
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * Gets the template name.
	 *
	 * @return the template name
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Sets the template name.
	 *
	 * @param templateName the new template name
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * Gets the template msg.
	 *
	 * @return the template msg
	 */
	public String getTemplateMsg() {
		return templateMsg;
	}

	/**
	 * Sets the template msg.
	 *
	 * @param templateMsg the new template msg
	 */
	public void setTemplateMsg(String templateMsg) {
		this.templateMsg = templateMsg;
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
		return "Template [userId=" + userId + ", templateId=" + templateId
				+ ", templateName=" + templateName + ", templateMsg="
				+ templateMsg + ", issueTime=" + issueTime + ", issueId="
				+ issueId + "]";
	}




	
	
	

}
