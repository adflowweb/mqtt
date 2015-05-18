/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class AddressReq {

	/** The ufmi. */
	@NotEmpty
	private String ufmi;
	
	/** The ufmi Name. */
	private String ufmiName;
	
	/** The item 1. */
	private String item1;
	
	/** The item 2. */
	private String item2;
	
	/** The item 3. */
	private String item3;
	
	

	/**
	 * Gets the ufmi.
	 *
	 * @return the ufmi
	 */
	public String getUfmi() {
		return ufmi;
	}

	/**
	 * Sets the ufmi.
	 *
	 * @param ufmi the new ufmi
	 */
	public void setUfmi(String ufmi) {
		this.ufmi = ufmi;
	}

	/**
	 * Gets the ufmi Name.
	 *
	 * @return the ufmiName
	 */
	public String getUfmiName() {
		return ufmiName;
	}

	/**
	 * Sets the ufmi Name.
	 *
	 * @param ufmiName the new ufmiName
	 */
	public void setUfmiName(String ufmiName) {
		this.ufmiName = ufmiName;
	}

	/**
	 * Gets the item1.
	 *
	 * @return the item1
	 */
	public String getItem1() {
		return item1;
	}

	/**
	 * Sets the item1.
	 *
	 * @param item1 the new item1
	 */
	public void setItem1(String item1) {
		this.item1 = item1;
	}

	/**
	 * Gets the item2.
	 *
	 * @return the item2
	 */
	public String getItem2() {
		return item2;
	}

	/**
	 * Sets the item2.
	 *
	 * @param item2 the new item2
	 */
	public void setItem2(String item2) {
		this.item2 = item2;
	}

	/**
	 * Gets the item3.
	 *
	 * @return the item3
	 */
	public String getItem3() {
		return item3;
	}

	/**
	 * Sets the item3.
	 *
	 * @param item3 the new item3
	 */
	public void setItem3(String item3) {
		this.item3 = item3;
	}

	@Override
	public String toString() {
		return "AdressReq [ufmi=" + ufmi + ", ufmiName=" + ufmiName
				+ ", item1=" + item1 + ", item2=" + item2 + ", item3=" + item3
				+ "]";
	}


	
}
