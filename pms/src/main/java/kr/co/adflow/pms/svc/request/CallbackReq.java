package kr.co.adflow.pms.svc.request;

public class CallbackReq {
	
	private String callbackmsgid;
	private String ackresult;
	
	public String getCallbackmsgid() {
		return callbackmsgid;
	}
	public void setCallbackmsgid(String callbackmsgid) {
		this.callbackmsgid = callbackmsgid;
	}
	public String getAckresult() {
		return ackresult;
	}
	public void setAckresult(String ackresult) {
		this.ackresult = ackresult;
	}

}
