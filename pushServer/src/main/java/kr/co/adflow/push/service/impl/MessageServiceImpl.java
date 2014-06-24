package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.MessageDao;
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
	MessageDao messageDao;

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
	public int post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		int count = messageDao.post(msg);
		logger.debug("post종료(updates=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.MessageService#put(kr.co.adflow.push.domain
	 * .Message)
	 */
	@Override
	public int put(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		int count = messageDao.put(msg);
		logger.debug("put종료(updates=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.MessageService#delete(int)
	 */
	@Override
	public int delete(int msgID) throws Exception {
		logger.debug("delete시작(msgID=" + msgID + ")");
		int count = messageDao.delete(msgID);
		logger.debug("delete종료(updates=" + count + ")");
		return count;
	}

	@Override
	public Message[] getMsgs() throws Exception {
		return null;
	}
}
