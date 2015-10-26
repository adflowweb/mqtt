/*
 * 
 */
package kr.co.adflow.pms.domain;


// TODO: Auto-generated Javadoc
/**
 * The Class Ack.
 */
public class AddressParams {

	/** The user Id. */
	private String userId;
	
	/** The ufmi. */
	private String ufmi;
	
	
	/** The ufmi list */
	private String[] ufmiArray;

	
	

	public String[] getUfmiArray() {
		return ufmiArray;
	}

	public void setUfmiArray(String[] ufmiArray) {
		this.ufmiArray = ufmiArray;
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

	

}
