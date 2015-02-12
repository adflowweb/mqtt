package kr.co.adflow.pms.adm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

















import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.controller.SystemController;
import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;
import kr.co.adflow.pms.domain.mapper.ValidationMapper;
import kr.co.adflow.pms.domain.validator.UserValidator;

@Service
public class SvcServiceImpl implements SvcService {

	private static final Logger logger = LoggerFactory
			.getLogger(SvcServiceImpl.class);
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private InterceptMapper interceptMapper;
	
	@Autowired
	private ValidationMapper validationMapper;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public MessagesRes getSvcMessageList(Map<String, String> params) {
		
		MessagesRes res = null;
		
		String issueId = interceptMapper.selectCashedUserId(params.get("appKey"));
		
		if (params.get("cSearchDate") == null) {
			//error
		} 
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setKeyMon(params.get("cSearchDate"));
		
		logger.info("msgParams :::::::{}",issueId);
		msgParams.setIssueId(issueId);
		
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		logger.info("msgParams :::::::{}",msgParams.getIssueId());
		
		int cnt = messageMapper.getSvcMessageListCnt(msgParams);
		logger.info("cnt :::::::{}",cnt);
		
		List<Message> list = messageMapper.getSvcMessageList(msgParams);
		logger.info("list size :::::::{}",list.size());
		
		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);
		
		return res;
	}

	private int getInt(String string) {
		
		return Integer.parseInt(string);
	}

	@Override
	public MessagesRes getSvcResevationMessageList(Map<String, String> params) {
		
		MessagesRes res = null;
		
		String issueId = interceptMapper.selectCashedUserId(params.get("appKey"));
		
		if (params.get("cSearchDate") == null) {
			//error
			throw new RuntimeException("");
		} 
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setKeyMon(params.get("cSearchDate"));
		
		logger.info("msgParams :::::::{}",issueId);
		msgParams.setIssueId(issueId);
		
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		logger.info("msgParams :::::::{}",msgParams.getIssueId());
		
		int cnt = messageMapper.getSvcResevationMessageListCnt(msgParams);
		logger.info("cnt :::::::{}",cnt);
		
		List<Message> list = messageMapper.getSvcResevationMessageList(msgParams);
		logger.info("list size :::::::{}",list.size());
		
		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);
		
		return res;
	}

	@Override
	public List<Map<String, String>> sendMessage(String appKey, MessageReq message) {
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
				
				if (message.getMsgType() == 0) {
					msg.setMsgType(PmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
				} else {
					msg.setMsgType(message.getMsgType());
				}
				
				if (message.getExpiry() == 0) {
					msg.setExpiry(PmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
				} else {
					msg.setExpiry(message.getExpiry());
				}
				
				if (message.getQos() == 0) {
					msg.setQos(PmsConfig.MESSAGE_HEADER_QOS_DEFAULT);
				} else {
					msg.setQos(message.getQos());
				}
				
				msg.setIssueId(issueId);
				msg.setUpdateId(issueId);

				if (message.getServiceId() == null || message.getServiceId().trim().length() == 0) {
					msg.setServiceId(PmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
				} else {
					msg.setServiceId(message.getServiceId());
				}
				
				msg.setAck(message.isAck());
				
				msg.setContentType(message.getContentType());
				msg.setContent(message.getContent());
				
				if (message.getReservationTime() != null) {
					msg.setReservationTime(message.getReservationTime());
					msg.setReservation(true);
				} else {
					msg.setReservation(false);
				}
				
				msg.setResendMaxCount(message.getResendMaxCount());
				msg.setResendInterval(message.getResendInterval());

				String[] receivers = message.getReceivers();

// admin message 				
//				for (int i = 0; i < receivers.length; i++) {
//					if (!userValidator.validRequestValue(receivers[i])) {
//						throw new RuntimeException("receivers formatting error count : " + i);
//					}
//				}
				
				resultList = new ArrayList<Map<String,String>>(receivers.length);
				
				Map<String,String> msgMap = null;
				for (int i = 0; i < receivers.length; i++) {

					msgMap = new HashMap<String,String>();
					msg.setReceiver(receivers[i]);
					msg.setReceiverTopic(receivers[i]);
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

}
