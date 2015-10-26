/*
 * 
 */
package kr.co.adflow.push.domain.ktp;

import java.util.Arrays;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class MsgParams.
 */
public class MsgParams {

	/** The i display start. */
	private int iDisplayStart;
	
	/** The i display length. */
	private int iDisplayLength;

	/** The date start. */
	private Date dateStart;

	/** The date end. */
	private Date dateEnd;

	/** The receiver. */
	private String receiver;
	
	/** The type. */
	private int type;
	


	/**
	 * Gets the i display start.
	 *
	 * @return the i display start
	 */
	public int getiDisplayStart() {
		return iDisplayStart;
	}

	/**
	 * Sets the i display start.
	 *
	 * @param iDisplayStart the new i display start
	 */
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	/**
	 * Gets the i display length.
	 *
	 * @return the i display length
	 */
	public int getiDisplayLength() {
		return iDisplayLength;
	}

	/**
	 * Sets the i display length.
	 *
	 * @param iDisplayLength the new i display length
	 */
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	/**
	 * Gets the date start.
	 *
	 * @return the date start
	 */
	public Date getDateStart() {
		return dateStart;
	}

	/**
	 * Sets the date start.
	 *
	 * @param dateStart the new date start
	 */
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	/**
	 * Gets the date end.
	 *
	 * @return the date end
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * Sets the date end.
	 *
	 * @param dateEnd the new date end
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}


	/**
	 * Gets the receiver.
	 *
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Sets the receiver.
	 *
	 * @param receiver the new receiver
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "MsgParams [iDisplayStart=" + iDisplayStart
				+ ", iDisplayLength=" + iDisplayLength + ", dateStart="
				+ dateStart + ", dateEnd=" + dateEnd + ", receiver=" + receiver
				+ ", type=" + type + "]";
	}


	
	

}
