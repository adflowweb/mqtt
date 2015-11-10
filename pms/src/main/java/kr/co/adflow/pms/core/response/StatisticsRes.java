package kr.co.adflow.pms.core.response;

public class StatisticsRes {

	private int msgTotalCnt;
	private int msgSuccessCnt;
	private int msgFailCnt;
	private int agentAckCnt;
	private int appAckCnt;

	public int getMsgTotalCnt() {
		return msgTotalCnt;
	}

	public void setMsgTotalCnt(int msgTotalCnt) {
		this.msgTotalCnt = msgTotalCnt;
	}

	public int getMsgSuccessCnt() {
		return msgSuccessCnt;
	}

	public void setMsgSuccessCnt(int msgSuccessCnt) {
		this.msgSuccessCnt = msgSuccessCnt;
	}

	public int getMsgFailCnt() {
		return msgFailCnt;
	}

	public void setMsgFailCnt(int msgFailCnt) {
		this.msgFailCnt = msgFailCnt;
	}

	public int getAgentAckCnt() {
		return agentAckCnt;
	}

	public void setAgentAckCnt(int agentAckCnt) {
		this.agentAckCnt = agentAckCnt;
	}

	public int getAppAckCnt() {
		return appAckCnt;
	}

	public void setAppAckCnt(int appAckCnt) {
		this.appAckCnt = appAckCnt;
	}

	@Override
	public String toString() {
		return "StatisticsRes [msgTotalCnt=" + msgTotalCnt + ", msgSuccessCnt="
				+ msgSuccessCnt + ", msgFailCnt=" + msgFailCnt
				+ ", agentAckCnt=" + agentAckCnt + ", appAckCnt=" + appAckCnt
				+ "]";
	}

}
