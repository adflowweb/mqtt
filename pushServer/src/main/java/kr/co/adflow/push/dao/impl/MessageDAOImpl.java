package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDAO;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.service.MqttService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component
public class MessageDAOImpl implements MessageDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageDAOImpl.class);

	@Resource
	MqttService mqttService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#get(java.lang.String)
	 */
	@Override
	public Message get(String messageID) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#post(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public void post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		mqttService.publish(msg);
		logger.debug("post종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public void put(Message msg) throws Exception {
		// return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public void delete(String messageID) throws Exception {
		// return null;
	}
}
