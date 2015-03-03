package kr.co.adflow.pms.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.adflow.pms.core.config.PmsConfig;

@Component
public class CheckUtil {
	
	@Autowired
	private PmsConfig pmsConfig;
	
	public  int getMessageSizeLimit(int msgSize) {
				
		if (msgSize == -1) {
			return pmsConfig.MESSAGE_SIZE_LIMIT_DEFAULT;
		} else {
			return msgSize;
		}
		
	}
	
	public int getMessageExpiry(int expiry) {
		
		if (expiry == -1) {
			return pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT;
		} else {
			return expiry;
		}
	}
	
	public int getMessageQos(int qos) {
		
		if (qos == -1) {
			return pmsConfig.MESSAGE_HEADER_QOS_DEFAULT;
		} else {
			return qos;
		}
	}

}
