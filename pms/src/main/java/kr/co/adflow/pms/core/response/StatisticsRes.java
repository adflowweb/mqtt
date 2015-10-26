package kr.co.adflow.pms.core.response;

public class StatisticsRes {

	private int msgTotalCnt;
	private int msgSuccessCnt;
	private int msgFailCnt;
	private int pmaAckCnt;
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

	public int getPmaAckCnt() {
		return pmaAckCnt;
	}

	public void setPmaAckCnt(int pmaAckCnt) {
		this.pmaAckCnt = pmaAckCnt;
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
				+ msgSuccessCnt + ", msgFailCnt=" + msgFailCnt + ", pmaAckCnt="
				+ pmaAckCnt + ", appAckCnt=" + appAckCnt + "]";
	}

}
