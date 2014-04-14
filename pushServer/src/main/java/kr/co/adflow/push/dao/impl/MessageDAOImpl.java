package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDAO;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ResponseData;
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
	public ResponseData get(String messageID) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#post(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public boolean post(Message msg) throws Exception {
		return mqttService.publish(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public ResponseData put(Message msg) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public ResponseData delete(String messageID) throws Exception {
		return null;
	}
}
