/*
 * 
 */
package kr.co.adflow.util;

import java.security.MessageDigest;
import java.util.UUID;

import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenGenerator.
 *
 * @author nadir93
 * @date 2014. 5. 15.
 */
public class TokenGenerator {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenGenerator.class);

	/**
	 * Generate.
	 *
	 * @return the string
	 */
	public static String generate() {
		logger.debug("generate시작()");
		String token = UUID.randomUUID().toString();
		token = token.replaceAll("-", "");
		token = token.substring(0, 23);
		logger.debug("generate종료(토큰=" + token + ")");
		return token;
	}

	
	
	
	//140912 <kicho> userid로 토큰생성 -start
	/**
	 * Generate.
	 *
	 * @param userID the user id
	 * @return the string
	 */
	public static String generate(String userID) {
		
		String token=null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			logger.debug("userID=" + userID);
			md.update(userID.getBytes());
			byte[] mdbytes = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			logger.debug("mdbytes.length=" + mdbytes.length);
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			logger.debug("deviceIDHexString=" + sb);
			token = sb.toString().substring(0, 23);
//			logger.debug("clientID=" + CLIENTID);
			logger.debug("generate종료(토큰=" + token + ")");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return token;
	}
	//140912 <kicho> userid로 토큰생성 -end
}
