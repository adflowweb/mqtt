/*
 * 
 */
package kr.co.adflow.push.domain;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class HA.
 *
 * @author nadir93
 * @date 2014. 7. 24.
 */
public class HA {

	/** The hostname. */
	private String hostname;
	
	/** The lastime. */
	private Date lastime;
	
	/** The status. */
	private int status;

	/**
	 * Gets the hostname.
	 *
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the hostname.
	 *
	 * @param hostname the new hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Gets the lastime.
	 *
	 * @return the lastime
	 */
	public Date getLastime() {
		return lastime;
	}

	/**
	 * Sets the lastime.
	 *
	 * @param lastime the new lastime
	 */
	public void setLastime(Date lastime) {
		this.lastime = lastime;
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
		return "HA [hostname=" + hostname + ", lastime=" + lastime
				+ ", status=" + status + "]";
	}

}
