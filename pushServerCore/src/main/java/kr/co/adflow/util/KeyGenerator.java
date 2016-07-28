/*
 * 
 */
package kr.co.adflow.util;

import java.security.SecureRandom;
import java.util.UUID;

import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyGenerator.
 */
public class KeyGenerator {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KeyGenerator.class);

	/**
	 * Generate.
	 * 
	 * @return the string
	 */
	private static String generate() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * Generate msg id.
	 *
	 * @return the string
	 */
	public static String generateMsgId() {
		String id = KeyGenerator.generate();

		return DateUtil.getYYYYMM() + id;
	}

	public static void main(String[] args) {

		SecureRandom random = new SecureRandom();
		System.out.println(System.currentTimeMillis());
	}
}
