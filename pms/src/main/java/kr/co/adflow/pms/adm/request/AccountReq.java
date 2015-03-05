/*
 * 
 */
package kr.co.adflow.pms.adm.request;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountReq.
 */
public class AccountReq {

	/** The user name. */
	private String userName;
	
	/** The ip filters. */
	private String ipFilters;

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the ip filters.
	 *
	 * @return the ip filters
	 */
	public String getIpFilters() {
		return ipFilters;
	}

	/**
	 * Sets the ip filters.
	 *
	 * @param ipFilters the new ip filters
	 */
	public void setIpFilters(String ipFilters) {
		this.ipFilters = ipFilters;
	}

}
