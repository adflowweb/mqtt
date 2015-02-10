package kr.co.adflow.pms.svc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.svc.request.MessageIdsReq;
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
	private UserMapper userMapper;
	@Autowired
	private InterceptMapper interceptMapper;

	@Override
	public String[] sendMessage(String appKey, MessageReq message) {

		String[] msgIdArray = null;

		//1. get userId by appKey 
		String issueId = interceptMapper.selectCashedUserId(appKey);
		//2. get max count by userId
		//3. check max count
		if (userMapper.getMsgCntLimit(issueId) < message.getReceivers().length) {
	       throw new RuntimeException("send message count is message count limit over ");
		}
		

		Message msg = new Message();
		msg.setKeyMon(DateUtil.getYYYYMM());
		
		msg.setServerId(PmsConfig.EXECUTOR_SERVER_ID);
		msg.setMsgType(PmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
		msg.setExpiry(PmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
		msg.setQos(PmsConfig.MESSAGE_HEADER_QOS_DEFAULT);
		msg.setIssueId(issueId);
		msg.setUpdateId(issueId);

		msg.setServiceId(PmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		msg.setAck(message.isAck());
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		
		if (message.getReservationTime() != null) {
			msg.setReservationTime(message.getReservationTime());
			msg.setReservation(true);
		}
		
		msg.setResendCount(message.getResendCount());
		msg.setResendInterval(message.getResendInterval());

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

	@Override
	public List<MessageResult> getMessageResult(MessageIdsReq msgIds,
			String appKey) {
		
		List<MessageResult> resultList = null;
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		MsgIdsParams param = new MsgIdsParams();
		
		param.setKeyMon(this.getKeyMon(msgIds.getMsgIds()));
		param.setIssueId(issueId);
		param.setMsgIds(msgIds.getMsgIds());
		
		List<Message> list = messageMapper.getMessageResult(param);
		
		resultList = new ArrayList<MessageResult>();
		MessageResult messageResult = null;
		for (Message msg: list) {
			messageResult = new MessageResult();
			
			messageResult.setMsgId(msg.getMsgId());
			messageResult.setReceiver(msg.getReceiver());
			messageResult.setResendCount(msg.getResendCount());
			messageResult.setReservation(msg.isReservation());
			messageResult.setStatus(msg.getStatus());
			messageResult.setUpdateTime(msg.getUpdateTime());
			messageResult.setAppAckType(msg.getAppAckType());
			messageResult.setAppAckTime(msg.getAppAckTime());
			
			resultList.add(messageResult);
		}
		
		return resultList;
	}

	private String getKeyMon(String[] msgIds) {
		if (msgIds.length < 1) {
			throw new RuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}

}
