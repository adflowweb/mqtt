package kr.co.adflow.pms.domain;

import java.util.Date;

public class AckData {

	private String token;
	private Date ackTime;
	private String ackType;
	private String serverId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getAckTime() {
		return ackTime;
	}

	public void setAckTime(Date ackTime) {
		this.ackTime = ackTime;
	}

	public String getAckType() {
		return ackType;
	}

	public void setAckType(String ackType) {
		this.ackType = ackType;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Override
	public String toString() {
		return "AckRes [token=" + token + ", ackTime=" + ackTime + ", ackType="
				+ ackType + ", serverId=" + serverId + "]";
	}
}
