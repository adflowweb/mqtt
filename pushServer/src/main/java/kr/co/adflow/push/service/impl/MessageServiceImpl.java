package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDAO;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.service.MessageService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class MessageServiceImpl implements MessageService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	@Resource
	MessageDAO messageDao;

	@Override
	public Message get(int messageID) throws Exception {
		logger.debug("get시작(msg=" + messageID + ")");
		Message msg = messageDao.get(messageID);
		logger.debug("get종료(msg=" + msg + ")");
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.MessageService#post(kr.co.adflow.push.domain
	 * .Message)
	 */
	@Override
	public void post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		messageDao.post(msg);
		logger.debug("post종료()");
	}

	@Override
	public void put(Message msg) throws Exception {
		// return null;
	}

	@Override
	public void delete(int messageID) throws Exception {
		// return null;
	}
}
