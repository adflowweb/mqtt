/*
 * 
 */
package kr.co.adflow.pms.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class TemplateParams.
 */
public class TemplateParams {

	/** The user Id. */
	private String userId;
	
	/** The template id. */
	private String templateId;
	
	/** The template name. */
	private String templateName;
	

	
	

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	@Override
	public String toString() {
		return "TemplateParams [userId=" + userId + ", templateId="
				+ templateId + ", templateName=" + templateName + "]";
	}






	
	
	

}
