package kr.co.adflow.push.dao.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.PushDAO;
import kr.co.adflow.push.domain.AvailableResponseData;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.service.MqttService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 3. 20.
 */
@Component
public class PushDAOImpl implements PushDAO {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PushDAOImpl.class);

	@Resource
	MqttService mqttService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.PushDAO#isAvailable()
	 */
	@Override
	public AvailableResponseData isAvailable() throws Exception {
		AvailableResponseData res = new AvailableResponseData(
				mqttService.isConnected(), mqttService.getErrorMsg());
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.PushDAO#shutdown()
	 */
	@Override
	public void shutdown() throws Exception {
		mqttService.destroy();
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
		return mqttService.publish(msg);
	}
}
