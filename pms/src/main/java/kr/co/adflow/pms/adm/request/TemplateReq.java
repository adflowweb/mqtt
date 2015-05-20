/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class TemplateReq {

	
	/** The template id. */
	@NotEmpty
	private String templateId;
	
	/** The template Name. */
	private String templateName;
	
	/** The template msg. */
	private String templateMsg;
	

	
	

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

	@Override
	public String toString() {
		return "TemplateReq [templateId=" + templateId + ", templateName="
				+ templateName + ", templateMsg=" + templateMsg + "]";
	}

	
	
}
