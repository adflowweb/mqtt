package kr.co.adflow.pms.adm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;

@Service
public class SvcAdmServiceImpl implements SvcAdmService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SvcAdmServiceImpl.class);

	
	@Autowired
	private InterceptMapper interceptMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private CheckUtil checkUtil;
	
	@Autowired
	private PmsConfig pmsConfig;


	@Override
	public List<Map<String, String>> sendMessage(String appKey, MessageReq message) {
		//String[] msgIdArray = null;
		
				List<Map<String,String>> resultList = null;

				//1. get userId by appKey 
				String issueId = interceptMapper.selectCashedUserId(appKey);
				
				if (message.getContent().getBytes().length > this.getMessageSizeLimit(issueId)) {
					throw new RuntimeException(" message body size limit over :" + this.getMessageSizeLimit(issueId));
				}
				//2. get max count by userId
				//3. check max count
//				if (userMapper.getMsgCntLimit(issueId) < message.getReceivers().length) {
//			       throw new RuntimeException("send message count is message count limit over ");
//				}
				//svcadm svc 갯수 제한 없음
				

				Message msg = new Message();
				msg.setKeyMon(DateUtil.getYYYYMM());
				
				msg.setServerId(pmsConfig.EXECUTOR_SERVER_ID);
				
				if (message.getMsgType() == 0) {
					msg.setMsgType(pmsConfig.MESSAGE_HEADER_TYPE_DEFAULT);
				} else {
					msg.setMsgType(message.getMsgType());
				}
				
				if (message.getExpiry() == 0) {
					msg.setExpiry(this.getMessageExpiry(issueId));
				} else {
					msg.setExpiry(message.getExpiry());
				}
				
				if (message.getQos() == 0) {
					msg.setQos(this.getMessageQos(issueId));
				} else {
					msg.setQos(message.getQos());
				}
				
				msg.setIssueId(issueId);
				msg.setUpdateId(issueId);

				if (message.getServiceId() == null || message.getServiceId().trim().length() == 0) {
					msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
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
				String groupId = null;
				for (int i = 0; i < receivers.length; i++) {

					msgMap = new HashMap<String,String>();
					msg.setReceiver(receivers[i]);
					msg.setReceiverTopic(receivers[i]);
					msg.setMsgId(this.getMsgId());
					
					//MEMO 여러 건일때 0 번째 msgId 를 대표로 groupId에 추가
					if (i == 0) {
						groupId = msg.getMsgId();
					}
					msg.setGroupId(groupId);
					//MEMO resend msg 인 경우 resendId 에 msgId 추가
					if (msg.getResendCount() > 0) {
						msg.setResendId(msg.getMsgId());
					} else {
						msg.setResendId(null);
					}
			
					msg.setStatus(StaticConfig.MESSAGE_STATUS_SENDING);
					messageMapper.insertMessage(msg);
					messageMapper.insertContent(msg);
					msgMap.put("msgId", msg.getMsgId());
					msgMap.put("receiver", msg.getReceiver());
					
					resultList.add(msgMap);
					logger.info("message id :: {}", msg.getMsgId());
				}

				return resultList;
	}
	
	private int getMessageSizeLimit(String userId) {
		return checkUtil.getMessageSizeLimit(interceptMapper.getCashedMessageSizeLimit(userId));
	}
	
	private int getMessageExpiry(String userId) {
		return checkUtil.getMessageExpiry(interceptMapper.getCashedMessageExpiry(userId));
	}
	
	private int getMessageQos(String userId) {
		return checkUtil.getMessageQos(interceptMapper.getCashedMessageQos(userId));
	}

	
	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

}
