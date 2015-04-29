/*
 * 
 */
package kr.co.adflow.pms.domain.push.mapper;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Interface PushMapper.
 */
public interface PushMapper {

	
	/**
	 * get Ufmi.
	 *
	 * @param Token(PUSH)
	 * @return ufmi
	 */
	String getUfmi(String token);
	
	/**
	 * get Token.
	 *
	 * @param Token(PUSH)
	 * @return ufmi
	 */
	String getToken(String ufmi);

}
