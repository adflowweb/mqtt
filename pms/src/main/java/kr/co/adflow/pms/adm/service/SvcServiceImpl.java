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
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgIdsParams;
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

	@Override
	public int cancelReservationList(String appKey, ReservationCancelReq reqIds) {
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		MsgIdsParams params = new MsgIdsParams();
		
		params.setKeyMon(this.getKeyMon(reqIds.getMsgIds()));
		params.setMsgIds(reqIds.getMsgIds());
		params.setIssueId(null);
		params.setUpdateId(issueId);
		
		int cnt = messageMapper.cancelReservationList(params);
		
		return cnt;
	}
	
	private String getKeyMon(String[] msgIds) {
		if (msgIds.length < 1) {
			throw new RuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}
	


}
