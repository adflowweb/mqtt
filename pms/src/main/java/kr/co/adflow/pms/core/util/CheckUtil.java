package kr.co.adflow.pms.core.util;

import kr.co.adflow.pms.core.config.PmsConfig;

public class CheckUtil {
	
	public static int getMessageSizeLimit(int msgSize) {
		
		if (msgSize == -1) {
			return PmsConfig.MESSAGE_SIZE_LIMIT_DEFAULT;
		} else {
			return msgSize;
		}
		
	}
	
	public static int getMessageExpiry(int expiry) {
		
		if (expiry == -1) {
			return PmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT;
		} else {
			return expiry;
		}
	}
	
	public static int getMessageQos(int qos) {
		
		if (qos == -1) {
			return PmsConfig.MESSAGE_HEADER_QOS_DEFAULT;
		} else {
			return qos;
		}
	}

}
