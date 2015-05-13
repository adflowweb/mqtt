/*
 * 
 */
package kr.co.adflow.pms.adm.response;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthRes.
 */
public class AuthRes {

	/** The user id. */
	private String userId;
	
	/** The role. */
	private String role;
	
	/** The token. */
	private String token;
	
	/** The ufmi. */
	private String ufmi;
	
	/** The groupTopic. */
	private String[] groupTopics;
	
	
	

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
	 * Gets the groupTopics.
	 *
	 * @return the groupTopics
	 */
	public String[] getGroupTopics() {
		return groupTopics;
	}

	/**
	 * Sets the groupTopics.
	 *
	 * @param groupTopics the new groupTopics
	 */
	public void setGroupTopics(String[] groupTopics) {
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
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
