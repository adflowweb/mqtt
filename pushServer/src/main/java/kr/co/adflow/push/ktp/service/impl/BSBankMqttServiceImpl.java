package kr.co.adflow.push.ktp.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kr.co.adflow.push.ktp.mapper.GroupMapper;
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
		super.initialize();
		logger.info("BSBankMqttServiceImpl초기화시작()");
		grpMapper = bsBanksqlSession.getMapper(GroupMapper.class);
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

	@Override
	protected void receiveGroup(String topic, MqttMessage message) {
		logger.debug("receiveGroup시작(topic=" + topic + ", message=" + message
				+ ")");
		try {
			// db select group info
			// convert json string to object
			User user = objectMapper
					.readValue(message.getPayload(), User.class);
			kr.co.adflow.push.domain.ktp.User bsbankUser = grpMapper
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
		logger.debug("receiveGroup종료()");
	}

//	@Override
//	protected void receivePoll(String topic, MqttMessage message) {
//		logger.debug("receivePoll시작(topic=" + topic + ", message=" + message
//				+ ")");
//		try {
//			// db insert ack
//			// convert json string to object
//			PollResponse response = objectMapper.readValue(
//					message.getPayload(), PollResponse.class);
//
//			PollMapper poolMapper = sqlSession.getMapper(PollMapper.class);
//			poolMapper.postResponse(response.getId(), response.getAnswerid(),
//					response.getUserid());
//			poolMapper.putResponseCount(response.getId());
//			logger.debug("설문조사결과메시지를등록하였습니다.");
//
//		} catch (Exception e) {
//			logger.error("에러발생", e);
//		}
//		logger.debug("receivePoll종료()");
//	}

	@Override
	protected void receiveBadge(String topic, MqttMessage message) {
		logger.debug("receiveBadge시작(topic=" + topic + ", message=" + message
				+ ")");
		try {
			// db insert ack
			// convert json string to object
			Device device = objectMapper.readValue(message.getPayload(),
					Device.class);

			DeviceMapper deviceMapper = sqlSession
					.getMapper(DeviceMapper.class);
			deviceMapper.putUnread(device.getUnRead(), device.getUserID(),
					device.getDeviceID());

			logger.debug("unRead카운트를 업데이트하였습니다. device=" + device);
		} catch (Exception e) {
			logger.error("에러발생", e);
		}
		logger.debug("receiveBadge종료()");
	}
}
