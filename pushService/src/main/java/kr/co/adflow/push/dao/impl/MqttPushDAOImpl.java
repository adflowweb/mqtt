package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.PushDAO;
import kr.co.adflow.push.domain.IsAvailableResponseData;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.service.ConnectionService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@Component
public class MqttPushDAOImpl implements PushDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MqttPushDAOImpl.class);

	@Resource
	ConnectionService connService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.PushDAO#isAvailable()
	 */
	@Override
	public IsAvailableResponseData isAvailable() throws Exception {
		IsAvailableResponseData res = new IsAvailableResponseData(
				connService.isConnected(), connService.getErrorMsg());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.PushDAO#shutdown()
	 */
	@Override
	public void shutdown() throws Exception {
		connService.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.PushDAO#sendMessage(kr.co.adflow.push.domain.Message
	 * )
	 */
	@Override
	public boolean sendMessage(Message msg) throws Exception {
		return connService.publish(msg);
	}
}
