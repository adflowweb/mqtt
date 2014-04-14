package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.adflow.push.dao.MessageDAO;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ResponseData;
import kr.co.adflow.push.domain.SendMessageResponseData;
import kr.co.adflow.push.service.MessageService;

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
	public ResponseData get(String messageID) throws Exception {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.MessageService#post(kr.co.adflow.push.domain
	 * .Message)
	 */
	@Override
	public ResponseData post(Message msg) throws Exception {
		return new SendMessageResponseData(messageDao.post(msg));
	}

	@Override
	public ResponseData put(Message msg) throws Exception {
		return null;
	}

	@Override
	public ResponseData delete(String messageID) throws Exception {
		return null;
	}
}
