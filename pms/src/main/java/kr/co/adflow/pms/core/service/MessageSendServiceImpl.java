package kr.co.adflow.pms.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.handler.DirectMsgHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSendServiceImpl implements MessageSendService {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendServiceImpl.class);

	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public int sendMessageArray(String serverId, int limit) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("keyMon", DateUtil.getYYYYMM());
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectList(param);

		int updateCnt = 0;
		for (Message msg : list) {
			
			msg.setKeyMon(this.getKeyMon(msg.getMsgId()));
			
			if (userMapper.getMsgCntLimit(msg.getIssueId()) < 1) {
				msg.setStatus(-2);
				messageMapper.updateStatus(msg);
				continue;
			}
			
			// msg.getReceiverTopic(); 처리
			if (!this.validReceiverUserId(msg.getReceiver())) {
				msg.setStatus(-2);
				messageMapper.updateStatus(msg);
				continue;
			}
			
			msg.setReceiverTopic(ReceiverTopic(msg.getReceiver()));

			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(msg));

			
			
			// 재전송 로직 추가
			String msgId = msg.getMsgId();
			if (msg.getResendCount() > 0) {
				long reservationTime = System.currentTimeMillis();
				for (int i = 0; i < msg.getResendCount(); i++) {
					int intervalM = msg.getResendInterval();
					msg.setReservation(true);
					long interval = intervalM * 1000 * 60;
					reservationTime =  reservationTime + interval;
					msg.setReservationTime(new Date(reservationTime));
					msg.setMsgId(this.getMsgId());
					msg.setResendId(msgId);
					messageMapper.insertMessage(msg);
					messageMapper.insertContent(msg);
					
				}
			}
			msg.setMsgId(msgId);
			//msg.getReceiverTopic();
			msg.setStatus(1);

			int resultCnt = messageMapper.updateStatus(msg);
			logger.info("update count is {}", resultCnt);
			// message_cnt - result_count
			User user = new User();
			user.setUserId(msg.getIssueId());
			user.setMsgCntLimit(resultCnt);
			userMapper.discountMsgCntLimit(user);
			updateCnt++;
		}

		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}
	
	private boolean validReceiverUserId(String receiver) {
		// TODO Auto-generated method stub
		// type 에 따라 push.user 에서 조회
		return true;
	}

	private String getKeyMon(String string) {
		String result = string.substring(0, 6);
		logger.debug("getKeyMon is {}",result);
		return result;
	}
	
	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}
	
	private String ReceiverTopic(String receiver) {
		//TODO
		// 1. 01012341234
		// 2. 82*1234*1234
		// 3. 00*1234*1234
		//return "mms/"+ receiver;
		return receiver;
	}

	@Override
	public int sendReservationMessageArray(String serverId, int limit) {
		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("keyMon", DateUtil.getYYYYMM());
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectReservationList(param);

		int updateCnt = 0;
		for (Message msg : list) {
			
			msg.setKeyMon(this.getKeyMon(msg.getMsgId()));
			
			if (userMapper.getMsgCntLimit(msg.getIssueId()) < 1) {
				msg.setStatus(-3);
				messageMapper.updateStatus(msg);
				continue;
			}
			
			// msg.getReceiverTopic(); 처리
			if (!this.validReceiverUserId(msg.getReceiver())) {
				msg.setStatus(-2);
				messageMapper.updateStatus(msg);
				continue;
			}
			
			msg.setReceiverTopic(ReceiverTopic(msg.getReceiver()));

			jmsTemplate.execute(msg.getReceiverTopic(), new DirectMsgHandler(msg));

			
			msg.setStatus(1);

			int resultCnt = messageMapper.updateStatus(msg);
			logger.info("update count is {}", resultCnt);
			// message_cnt - result_count
			User user = new User();
			user.setUserId(msg.getIssueId());
			user.setMsgCntLimit(resultCnt);
			userMapper.discountMsgCntLimit(user);
			updateCnt++;
		}

		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}

}
