package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.PushDAO;
import kr.co.adflow.push.domain.AvailableResponseData;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.service.PushService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@Service
public class PushServiceImpl implements PushService {
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PushServiceImpl.class);

	@Resource
	PushDAO pushDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#isAvailable()
	 */
	@Override
	public AvailableResponseData isAvailable() throws Exception {
		// if (true) {
		// throw new Exception("이것은예외테스트입니다.");
		// }
		return pushDao.isAvailable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.PushService#shutdown()
	 */
	@Override
	public void shutdown() throws Exception {
		pushDao.shutdown();
	}

	@Override
	public boolean sendMessage(Message msg) throws Exception {
		return pushDao.sendMessage(msg);
	}
}
