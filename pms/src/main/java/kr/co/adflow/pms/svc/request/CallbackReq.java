/*
 * 
 */
package kr.co.adflow.pms.svc.request;

// TODO: Auto-generated Javadoc
/**
 * The Class CallbackReq.
 */
public class CallbackReq {

	/** The callbackmsgid. */
	private String callbackMsgId;
	
	
	private String ackType;
	
	private String ackTime;
	
	/** The ackresult. */
	private boolean ackResult;
	
	/** The ufmi. */
	private String ufmi;
	
	

	public String getCallbackMsgId() {
		return callbackMsgId;
	}

	public void setCallbackMsgId(String callbackMsgId) {
		this.callbackMsgId = callbackMsgId;
	}

	public String getAckType() {
		return ackType;
	}

	public void setAckType(String ackType) {
		this.ackType = ackType;
	}

	public String getAckTime() {
		return ackTime;
	}

	public void setAckTime(String ackTime) {
		this.ackTime = ackTime;
	}

	public boolean isAckResult() {
		return ackResult;
	}

	public void setAckResult(boolean ackResult) {
		this.ackResult = ackResult;
	}

	public String getUfmi() {
		return ufmi;
	}

	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

	
	




}
