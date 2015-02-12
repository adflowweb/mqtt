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
import kr.co.adflow.pms.domain.mapper.ValidationMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;
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
	
	@Autowired
	private ValidationMapper validationMapper;
	
	@Autowired
	private UserValidator userValidator;

	@Override
	public List<Map<String,String>> sendMessage(String appKey, MessageReq message) {

		//String[] msgIdArray = null;
		
		List<Map<String,String>> resultList = null;

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
		msg.setAck(PmsConfig.MESSAGE_ACK_DEFAULT);
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		
		if (message.getReservationTime() != null) {
			msg.setReservationTime(message.getReservationTime());
			msg.setReservation(true);
		}
		
		msg.setResendMaxCount(message.getResendMaxCount());
		msg.setResendInterval(message.getResendInterval());

		String[] receivers = message.getReceivers();

		//msgIdArray = new String[receivers.length];
		
		
		
		for (int i = 0; i < receivers.length; i++) {
			if (!userValidator.validRequestValue(receivers[i])) {
				throw new RuntimeException("receivers formatting error count : " + i);
			}
		}
		
		resultList = new ArrayList<Map<String,String>>(receivers.length);
		
		Map<String,String> msgMap = null;
		for (int i = 0; i < receivers.length; i++) {

			msgMap = new HashMap<String,String>();
			msg.setReceiver(receivers[i]);
			msg.setMsgId(this.getMsgId());
			msg.setStatus(PmsConfig.MESSAGE_STATUS_SENDING);
			messageMapper.insertMessage(msg);
			messageMapper.insertContent(msg);
			msgMap.put("msgId", msg.getMsgId());
			msgMap.put("receiver", msg.getReceiver());
			
			resultList.add(msgMap);
			logger.info("message id :: {}", msg.getMsgId());
		}

		return resultList;
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

	@Override
	public Boolean validPhoneNo(String phoneNo) {
		return validationMapper.validPhoneNo(this.getPushUserId(phoneNo));
	}

	private String getPushUserId(String phoneNo) {
		return userValidator.getRegstPhoneNo(phoneNo);
	}

	@Override
	public Boolean validUfmiNo(String ufmiNo) {
		return validationMapper.validUfmiNo(this.getPushUfmi(ufmiNo));
	}

	private String getPushUfmi(String ufmiNo) {
		return userValidator.getRegstUfmiNo(ufmiNo);
	}

	@Override
	public Integer cancelMessage(String appKey, String msgId) {
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		Message msg = new Message();
		msg.setKeyMon(this.getKeyMon(msgId));
		msg.setMsgId(msgId);
		msg.setUpdateId(issueId);
		
		return messageMapper.cancelMessage(msg);
	}

	private String getKeyMon(String msgId) {
		// TODO Auto-generated method stub
		return msgId.substring(0, 6);
	}

}
