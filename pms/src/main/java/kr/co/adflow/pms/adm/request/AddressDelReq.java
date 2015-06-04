/*
 * 
 */
package kr.co.adflow.pms.adm.request;

import java.util.Arrays;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class UserReq.
 */
public class AddressDelReq {

	/** The ufmi list. */
	@NotEmpty
	private String[] ufmiArray;
	
	

	public String[] getUfmiArray() {
		return ufmiArray;
	}

	public void setUfmiArray(String[] ufmiArray) {
		this.ufmiArray = ufmiArray;
	}

	@Override
	public String toString() {
		return "AddressDelReq [ufmiArray=" + Arrays.toString(ufmiArray) + "]";
	}
	


	
}
