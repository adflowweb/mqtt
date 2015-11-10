/*
 * 
 */
package kr.co.adflow.pms.core.util;

import java.util.List;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.GroupController;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class CheckUtil.
 */
@Component
public class CheckUtil {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(CheckUtil.class);

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	@Autowired
	private InterceptMapper interceptMapper;
	@Autowired
	private TokenMapper tokenMapper;

	/**
	 * Gets the message size limit.
	 * 
	 * @param msgSize
	 *            the msg size
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
	 * @param expiry
	 *            the expiry
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
	 * @param qos
	 *            the qos
	 * @return the message qos
	 */
	public int getMessageQos(int qos) {

		if (qos == -1) {
			return pmsConfig.MESSAGE_HEADER_QOS_DEFAULT;
		} else {
			return qos;
		}
	}

	public String checkAuth(String applicationKey, String staticApiCode)
			throws Exception {

		String requsetUserId = interceptMapper
				.selectCashedUserId(applicationKey);
		logger.debug(requsetUserId + "에 대한 권한체크를 시작합니다!");
		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(staticApiCode)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(staticApiCode + "에 대한권한이 없습니다");

			throw new Exception("API접근 권한이 없습니다");

		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");

		return requsetUserId;

	}

}
