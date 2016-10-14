package kr.co.adflow.pms.domain;

public class Leader {

	private String serverId;
	private boolean leaderCheck;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public boolean isLeaderCheck() {
		return leaderCheck;
	}

	public void setLeaderCheck(boolean leaderCheck) {
		this.leaderCheck = leaderCheck;
	}

	@Override
	public String toString() {
		return "Leader [serverId=" + serverId + ", leaderCheck=" + leaderCheck + "]";
	}

}
