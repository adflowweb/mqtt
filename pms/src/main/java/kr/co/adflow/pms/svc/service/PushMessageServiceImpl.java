package kr.co.adflow.pms.svc.service;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.svc.request.MessageReq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushMessageServiceImpl implements PushMessageService {

	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageServiceImpl.class);

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private InterceptMapper interceptMapper;

	@Override
	public String[] sendMessage(String appKey, MessageReq message) {

		String[] msgIdArray = null;

		String issueId = interceptMapper.selectCashedUserId(appKey);

		Message msg = new Message();

		msg.setServerId(PmsConfig.EXECUTOR_SERVER_ID);
		msg.setMsgType(PmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
		msg.setExpiry(PmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
		msg.setQos(PmsConfig.MESSAGE_HEADER_QOS_DEFAULT);
		msg.setIssueId(issueId);
		msg.setReservationTime(message.getReservationTime());
		msg.setUpdateId(issueId);

		msg.setServiceId(PmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		msg.setAck(message.isAck());
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());

		String[] receivers = message.getReceivers();

		msgIdArray = new String[receivers.length];

		for (int i = 0; i < receivers.length; i++) {
			msg.setReceiver(receivers[i]);
			msg.setMsgId(this.getMsgId());
			msg.setStatus(PmsConfig.MESSAGE_STATUS_SENDING);
			
			
			messageMapper.insertMessage(msg);
			messageMapper.insertContent(msg);
			msgIdArray[i] = msg.getMsgId();
			logger.info("message id :: {}", msg.getMsgId());

		}

		return msgIdArray;
	}

	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

}
