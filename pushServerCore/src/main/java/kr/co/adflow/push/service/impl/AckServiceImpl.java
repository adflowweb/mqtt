/*
 * 
 */
package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.AckDao;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.service.AckService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class AckServiceImpl.
 *
 * @author nadir93
 */
@Service
public class AckServiceImpl implements AckService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AckServiceImpl.class);

	/** The ack dao. */
	@Resource
	AckDao ackDao;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.AckService#get(int)
	 */
	@Override
	public Acknowledge[] get(int msgID) throws Exception {
		logger.debug("get시작(msgID=" + msgID + ")");
		Acknowledge[] acks = ackDao.get(msgID);
		logger.debug("get종료(acks=" + acks + ")");
		return acks;
	}

}
