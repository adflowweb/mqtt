/*
 * 
 */
package kr.co.adflow.pms.core.response;

import java.util.List;

import kr.co.adflow.pms.domain.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class MessagesRes.
 */
public class MessagesListRes {

	/** The s echo. */
	private String sEcho;
	
	/** The records filtered. */
	private int recordsFiltered;
	
	/** The records total. */
	private int recordsTotal;
	
	
	/** The data. */
	private List<Message> data;

	/**
	 * Gets the s echo.
	 *
	 * @return the s echo
	 */
	public String getsEcho() {
		return sEcho;
	}

	/**
	 * Sets the s echo.
	 *
	 * @param sEcho the new s echo
	 */
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	/**
	 * Gets the records filtered.
	 *
	 * @return the records filtered
	 */
	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	/**
	 * Sets the records filtered.
	 *
	 * @param recordsFiltered the new records filtered
	 */
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	/**
	 * Gets the records total.
	 *
	 * @return the records total
	 */
	public int getRecordsTotal() {
		return recordsTotal;
	}

	/**
	 * Sets the records total.
	 *
	 * @param recordsTotal the new records total
	 */
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public List<Message> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(List<Message> data) {
		this.data = data;
	}

}
