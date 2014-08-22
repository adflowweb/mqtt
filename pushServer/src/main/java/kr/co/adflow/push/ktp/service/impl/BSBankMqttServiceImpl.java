package kr.co.adflow.push.ktp.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//import kr.co.adflow.push.ktp.mapper.GroupMapper;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.domain.ktp.PollResponse;
import kr.co.adflow.push.mapper.DeviceMapper;
import kr.co.adflow.push.service.impl.AbstractMqttServiceImpl;

import org.apache.ibatis.session.SqlSession;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Service
public class BSBankMqttServiceImpl extends AbstractMqttServiceImpl {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(BSBankMqttServiceImpl.class);

//	@Autowired
//	@Qualifier("bsBanksqlSession")
//	private SqlSession bsBanksqlSession;

//	private GroupMapper grpMapper;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		super.initialize();
		logger.info("BSBankMqttServiceImpl초기화시작()");
//		grpMapper = bsBanksqlSession.getMapper(GroupMapper.class);
		logger.info("BSBankMqttServiceImpl초기화종료()");
	}

	/**
	 * 모든리소스정리
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	public void cleanUp() throws Exception {
		logger.info("cleanUp시작()");
		destroy();
		logger.info("cleanUp종료()");
	}

	@Override
	protected void receiveAck(String topic, MqttMessage message) {
		logger.debug("receiveAck시작(topic=" + topic + ", message=" + message
				+ ")");
		try {
			// db insert ack
			// convert json string to object
			Acknowledge ack = objectMapper.readValue(message.getPayload(),
					Acknowledge.class);
			msgMapper.postAck(ack);
			logger.debug("ack메시지를등록하였습니다.");
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
		logger.debug("receiveAck종료()");
	}


}
