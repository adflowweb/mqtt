package kr.co.adflow.pms.core.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.LoggerFactory;

public class KeyGenerator {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(KeyGenerator.class);

	/**
	 * Generate.
	 * 
	 * @return the string
	 */
	private static String generate() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String generateMsgId() {
		String id = KeyGenerator.generate();

		return KeyGenerator.getYYYYMM() + id;
	}

	public static String getYYYYMM() {

		return KeyGenerator.getDate(new Date(), "YYYYMM");
	}

	private static String getDate(Date date, String pattern) {

		SimpleDateFormat fmt = new SimpleDateFormat(pattern);

		return fmt.format(date);

	}

	public static String generateToken(String str) {

		StringBuffer sb = null;
		String token = null;
		try {
			token = KeyGenerator.generate() + str;
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(token.getBytes());
			byte[] mdbytes = md.digest();

			sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			logger.debug("deviceIDHexString=" + sb);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	public static String createPw(String str1,String str2) {
		String result = null;
		result = generateHash(str2);
		
		//TODO pw 로직 변경
		result = generateHash(str1 + "#" + result);
		
		return result;
	}

	public static String generateHash(String str) {

		StringBuffer sb = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(str.getBytes());
			byte[] mdbytes = md.digest();

			sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			logger.debug("deviceIDHexString=" + sb);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	

}
