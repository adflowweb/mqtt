package kr.co.adflow.push.bsbank.service.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import kr.co.adflow.push.bsbank.mapper.GroupMapper;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.mapper.MessageMapper;
import kr.co.adflow.push.service.impl.MqttServiceImpl;

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
public class BSBankMqttServiceImpl extends MqttServiceImpl {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(BSBankMqttServiceImpl.class);

	@Autowired
	@Qualifier("bsBanksqlSession")
	private SqlSession bsBanksqlSession;

	private GroupMapper grpMapper;

	/**
	 * initialize
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void initIt() throws Exception {
		super.initIt();
		logger.info("BSBankMqttServiceImpl초기화시작()");
		grpMapper = bsBanksqlSession.getMapper(GroupMapper.class);
		logger.info("BSBankMqttServiceImpl초기화종료()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		logger.debug("messageArrived시작(topic=" + topic + ",message=" + message
				+ ",qos=" + message.getQos() + ")");
		// 수신 tps 계산용
		reqCnt++;

		if (topic.equals("/push/ack")) {
			logger.debug("ack메시지가수신되었습니다.");
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
		} else if (topic.equals("/push/group")) {
			logger.debug("그룹정보요청메시지가수신되었습니다.");
			try {
				// db select group info
				// convert json string to object
				User user = objectMapper.readValue(message.getPayload(),
						User.class);
				kr.co.adflow.push.domain.bsbank.User bsbankUser = grpMapper
						.getTopic(user.getUserID());
				logger.debug("user=" + bsbankUser);
				if (bsbankUser != null) {
					// db insert push
					Message msg = new Message();
					msg.setQos(2);
					msg.setReceiver("/users/" + user.getUserID());
					msg.setSender("pushServer");
					StringBuffer content = new StringBuffer();
					content.append("{\"userID\":");
					content.append("\"");
					content.append(user.getUserID());
					content.append("\",\"groups\":[\"");
					content.append(bsbankUser.getGw_sbsd_cdnm());
					content.append("\"");
					content.append(",\"");
					content.append(bsbankUser.getGw_deptmt_cdnm());
					content.append("\"");
					// for (int i = 0; i < grp.length; i++) {
					// content.append("\"");
					// content.append(grp[i].getTopic());
					// content.append("\"");
					// if (i < grp.length - 1) {
					// content.append(",");
					// }
					// }
					content.append("]}");
					msg.setType(Message.COMMAND_SUBSCRIBE);
					msg.setContent(content.toString());
					logger.debug("msg=" + msg);
					messageDao.post(msg);
					logger.debug("그룹정보메시지를등록하였습니다.");
				} else {
					logger.error("사용자정보가없습니다.");
				}
			} catch (Exception e) {
				logger.error("에러발생", e);
			}
		} else if (topic.equals("/push/poll")) {
			// 설문조사용
		} else {
			logger.error("적절한토픽처리자가없습니다.");
		}
		logger.debug("messageArrived종료()");
	}
}
