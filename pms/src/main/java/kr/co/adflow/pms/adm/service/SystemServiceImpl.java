package kr.co.adflow.pms.adm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.adm.controller.SystemController;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.dao.ServerDao;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

@Service
public class SystemServiceImpl implements SystemService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SystemServiceImpl.class);

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TokenMapper tokenMapper;
	@Autowired
	private InterceptMapper interceptMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private ServerDao serverDao;
	
	@Override
	public List<User> listAllUser() {
		
		return userMapper.listAll();
	}

	@Override
	public String createUser(UserReq userReq,String appKey) {
		
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User paramUser = new User();
		paramUser.setUserId(userReq.getUserId());
		paramUser.setUserName(userReq.getUserName());
		paramUser.setPassword(this.getPassword(userReq));
		paramUser.setRole(userReq.getRole());
		paramUser.setIssueId(issueId);
		
		if (userReq.getIpFilters() == null || userReq.getIpFilters().trim().length() == 0) {
			paramUser.setIpFilters(PmsConfig.INTERCEPTER_IP_FILTER);
		} else {
			paramUser.setIpFilters(userReq.getIpFilters());
		}
		
		paramUser.setMsgCntLimit(userReq.getMsgCntLimit());
		// 추가 옵션
		if (userReq.isOptions()) {
			paramUser.setDefaultExpiry(userReq.getDefaultExpiry());
			paramUser.setDefaultQos(userReq.getDefaultQos());
			paramUser.setMsgSizeLimit(userReq.getMsgSizeLimit());
			paramUser.setCallbackUrl(userReq.getCallbackUrl());
			paramUser.setCallbackMethod(userReq.getCallbackMethod());
			paramUser.setCallbackCntLimit(userReq.getCallbackCntLimit());			
		} else {
			paramUser.setDefaultExpiry(-1);
			paramUser.setDefaultQos(-1);
			paramUser.setMsgSizeLimit(-1);
			paramUser.setCallbackUrl(null);
			paramUser.setCallbackMethod(null);
			paramUser.setCallbackCntLimit(-1);
		}

		
		paramUser.setStatus(-1);
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(PmsConfig.TOKEN_TYPE_APPLICATION);
		token.setTokenId(this.getTokenId(userReq));

		//
		paramUser.setAction("createUser");
		userMapper.logUserHistory(paramUser);
		
		userMapper.insertUser(paramUser);
		tokenMapper.insertToken(token);
		paramUser.setStatus(PmsConfig.USER_STATUS_NORMAL);
		userMapper.updateUserStatus(paramUser);
		//
		// userMapper udate

		return paramUser.getUserId();
	}
	
	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(),req.getPassword());
	}
	
	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

	@Override
	public User retrieveUser(String userId) {
		return userMapper.select(userId);
	}

	@Override
	public int updateUser(UserUpdateReq userReq,String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User paramUser = userMapper.select(userReq.getUserId());
		
		if (userReq.getUserName() != null)
			paramUser.setUserName(userReq.getUserName());
		
		if (userReq.getMsgCntLimit() != 0)
			paramUser.setMsgCntLimit(userReq.getMsgCntLimit());
		
		if (userReq.getRole() != null)
			paramUser.setRole(userReq.getRole());
		
		//TODO validation 필요?
		if (userReq.getIpFilters() != null)
			paramUser.setIpFilters(userReq.getIpFilters());
		
		// 추가 옵션
		if (userReq.isOptions()) {
			paramUser.setDefaultExpiry(userReq.getDefaultExpiry());
			paramUser.setDefaultQos(userReq.getDefaultQos());
			paramUser.setMsgSizeLimit(userReq.getMsgSizeLimit());
			paramUser.setCallbackUrl(userReq.getCallbackUrl());
			paramUser.setCallbackMethod(userReq.getCallbackMethod());
			paramUser.setCallbackCntLimit(userReq.getCallbackCntLimit());			
		} else {
			paramUser.setDefaultExpiry(-1);
			paramUser.setDefaultQos(-1);
			paramUser.setMsgSizeLimit(-1);
			paramUser.setCallbackUrl(null);
			paramUser.setCallbackMethod(null);
			paramUser.setCallbackCntLimit(-1);
		}

		
		
		paramUser.setStatus(PmsConfig.USER_STATUS_NORMAL);
		paramUser.setIssueId(issueId);
		
		paramUser.setAction("updateUser");
		userMapper.logUserHistory(paramUser);

		return userMapper.updateUser(paramUser);
	}

	@Override
	public int deleteUser(String userId,String appKey) {
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User param = userMapper.select(userId);
		param.setIssueId(issueId);
		param.setAction("deleteUser");
		userMapper.logUserHistory(param);
		tokenMapper.deleteUserToken(userId);
		return userMapper.deleteUser(userId);
	}

	@Override
	public MessagesRes getSysMessageList(Map<String, String> params) {
		
		MessagesRes res = null;
		
		String issueId = interceptMapper.selectCashedUserId(params.get("appKey"));
		
		if (params.get("cSearchDate") == null) {
			//error
		} 
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setKeyMon(params.get("cSearchDate"));
		
		logger.info("msgParams :::::::{}",issueId);
		msgParams.setIssueId(null);
		
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

	private int getInt(String string) {
		
		return Integer.parseInt(string);
	}

	@Override
	public MessagesRes getSysResevationMessageList(Map<String, String> params) {
		
		MessagesRes res = null;
		
		String issueId = interceptMapper.selectCashedUserId(params.get("appKey"));
		
		if (params.get("cSearchDate") == null) {
			//error
			throw new RuntimeException("");
		} 
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setKeyMon(params.get("cSearchDate"));
		
		logger.info("msgParams :::::::{}",issueId);
		msgParams.setIssueId(null);
		
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
		
		// 예약 발송 예정만 처리?
		//msgParams.setStatusArray(this.getStringArray(params.get("cSearchStatus")));
		
		String filter = params.get("cSearchFilter");
		logger.info("filter :::::::{}",filter);
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
	public ServerInfo getServerInfo() {
		// TODO Auto-generated method stub
		try {
			return serverDao.get();
		} catch (Exception e) {
			throw new RuntimeException("getServerInfo error");
		}
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

}
