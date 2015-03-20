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
	private String callbackmsgid;
	
	
	private String acktype;
	
	private String acktime;
	
	/** The ackresult. */
	private boolean ackresult;

	/**
	 * Gets the callbackmsgid.
	 *
	 * @return the callbackmsgid
	 */
	public String getCallbackmsgid() {
		return callbackmsgid;
	}

	/**
	 * Sets the callbackmsgid.
	 *
	 * @param callbackmsgid the new callbackmsgid
	 */
	public void setCallbackmsgid(String callbackmsgid) {
		this.callbackmsgid = callbackmsgid;
	}

	public boolean isAckresult() {
		return ackresult;
	}

	public void setAckresult(boolean ackresult) {
		this.ackresult = ackresult;
	}

	public String getAcktype() {
		return acktype;
	}

	public void setAcktype(String acktype) {
		this.acktype = acktype;
	}

	public String getAcktime() {
		return acktime;
	}

	public void setAcktime(String acktime) {
		this.acktime = acktime;
	}



}
