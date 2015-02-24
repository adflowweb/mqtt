package kr.co.adflow.pms.adm.service;

import java.util.ArrayList;
import java.util.Date;
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
import kr.co.adflow.pms.core.util.CheckUtil;
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
		
		String issueId = interceptMapper.selectCashedUserId((String)params.get("appKey"));
		
		if (params.get("cSearchDate") == null) {
			//error
			throw new RuntimeException("");
		} 
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setKeyMon(params.get("cSearchDate"));
		msgParams.setIssueId(issueId);
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));
		
		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
		
		msgParams.setStatusArray(this.getStringArray(params.get("cSearchStatus")));
		
		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}
				
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

	private String[] getStringArray(String string) {
		if ("ALL".equals(string)) {
			return null;
		} 
		return string.split(",");
	}

	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

	private int getInt(String string) {
		System.out.println(string);
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
		msgParams.setIssueId(issueId);
		
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
		
		// 예약 발송 예정만 처리?
		//msgParams.setStatusArray(this.getStringArray(params.get("cSearchStatus")));
		
		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}
		
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
	
	private int getMessageSizeLimit(String userId) {
		return CheckUtil.getMessageSizeLimit(interceptMapper.getCashedMessageSizeLimit(userId));
	}
	
	private int getMessageExpiry(String userId) {
		return CheckUtil.getMessageExpiry(interceptMapper.getCashedMessageExpiry(userId));
	}
	
	private int getMessageQos(String userId) {
		return CheckUtil.getMessageQos(interceptMapper.getCashedMessageQos(userId));
	}

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
