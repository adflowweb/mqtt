/*
 * 
 */
package kr.co.adflow.pms.domain.push.mapper;

// TODO: Auto-generated Javadoc
/**
 * The Interface ValidationMapper.
 */
public interface ValidationMapper {

	/**
	 * Valid phone no.
	 *
	 * @param phoneNo the phone no
	 * @return true, if successful
	 */
	boolean validPhoneNo(String phoneNo);

	/**
	 * Valid ufmi no.
	 *
	 * @param ufmiNo the ufmi no
	 * @return true, if successful
	 */
	boolean validUfmiNo(String ufmiNo);

}
