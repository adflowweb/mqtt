package kr.co.adflow.push.domain;

import java.util.Date;

/**
 * @author nadir93
 * @date 2014. 7. 24.
 */
public class HA {

	private String hostname;
	private Date lastime;
	private int status;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Date getLastime() {
		return lastime;
	}

	public void setLastime(Date lastime) {
		this.lastime = lastime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "HA [hostname=" + hostname + ", lastime=" + lastime
				+ ", status=" + status + "]";
	}

}
