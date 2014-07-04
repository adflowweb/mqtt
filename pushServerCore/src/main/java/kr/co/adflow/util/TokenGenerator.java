package kr.co.adflow.util;

import java.util.UUID;

import org.slf4j.LoggerFactory;

/**
 * @author nadir93
 * @date 2014. 5. 15.
 * 
 */
public class TokenGenerator {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenGenerator.class);

	/**
	 * @return
	 */
	public static String generate() {
		logger.debug("generate시작()");
		String token = UUID.randomUUID().toString();
		token = token.replaceAll("-", "");
		token = token.substring(0, 23);
		logger.debug("generate종료(토큰=" + token + ")");
		return token;
	}
}
