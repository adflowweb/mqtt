package kr.co.adflow.pms.svc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.svc.request.MessageReq;

@Service
public class PushMessageServiceImpl implements PushMessageService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageServiceImpl.class);
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private InterceptMapper interceptMapper;
	
	private static final String SERVER_ID = "S01";
	private static final int DEFAULT_MESSAGE_TYPE = 20;
	private static final int DEFAULT_EXPIRY = 0;
	private static final int DEFAULT_QOS = 2;
	private static final String DEFAULT_SERVICE_ID = "kr.co.ktpowertel.push.mms";
	
	private static final int SENDING_MESSAGE_STATUS = 0;
	private static final int SEND_MESSAGE_STATUS = 1;
	

	@Override
	public String[] sendMessage(String appKey,MessageReq message) {
		
		String[] msgIdArray = null;
		
		String senderId = interceptMapper.selectCashedUserId(appKey);
		
		Message msg = new Message();
		
		msg.setServerId(SERVER_ID);
		msg.setMsgType(DEFAULT_MESSAGE_TYPE);
		msg.setExpiry(DEFAULT_EXPIRY);
		msg.setQos(DEFAULT_QOS);
		msg.setSenderId(senderId);
		msg.setReservationTime(message.getReservationTime());
		
		msg.setServiceId(DEFAULT_SERVICE_ID);
		msg.setAck(message.isAck());
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		
		String[] receivers = message.getReceivers();
		
		msgIdArray = new String[receivers.length];

		for (int i = 0; i < receivers.length; i++) {
			msg.setReceiver(receivers[i]);
			msg.setMsgId(this.getMsgId());
			msg.setStatus(SENDING_MESSAGE_STATUS);
			int cnt = messageMapper.insertMessage(msg);
			cnt += messageMapper.insertContent(msg);
			msgIdArray[i] = msg.getMsgId();
			logger.info("message id :: {}",msg.getMsgId());
			
		}
		
		return msgIdArray;
	}

	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

}
