/*
 * 
 */
package kr.co.adflow.pms.core.util;

import kr.co.adflow.pms.core.config.PmsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class CheckUtil.
 */
@Component
public class CheckUtil {

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/**
	 * Gets the message size limit.
	 *
	 * @param msgSize the msg size
	 * @return the message size limit
	 */
	// public int getMessageSizeLimit(int msgSize) {
	//
	// if (msgSize == -1) {
	// return pmsConfig.MESSAGE_SIZE_LIMIT_DEFAULT;
	// } else {
	// return msgSize;
	// }
	//
	// }

	/**
	 * Gets the message expiry.
	 *
	 * @param expiry the expiry
	 * @return the message expiry
	 */
	public int getMessageExpiry(int expiry) {

		if (expiry == -1) {
			return pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT;
		} else {
			return expiry;
		}
	}

	/**
	 * Gets the message qos.
	 *
	 * @param qos the qos
	 * @return the message qos
	 */
	public int getMessageQos(int qos) {

		if (qos == -1) {
			return pmsConfig.MESSAGE_HEADER_QOS_DEFAULT;
		} else {
			return qos;
		}
	}

}
