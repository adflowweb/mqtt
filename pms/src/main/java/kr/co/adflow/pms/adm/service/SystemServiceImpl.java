package kr.co.adflow.pms.adm.service;

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
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgParams;
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
	
	@Override
	public List<User> listAllUser() {
		
		return userMapper.listAll();
	}

	@Override
	public String createUser(UserReq userReq,String appKey) {
		
		
		String issueId = interceptMapper.selectCashedUserId(appKey);
		
		User user = new User();
		user.setUserId(userReq.getUserId());
		user.setUserName(userReq.getUserName());
		user.setPassword(this.getPassword(userReq));
		user.setRole(userReq.getRole());
		user.setIssueId(issueId);
		
		if (userReq.getIpFilters() == null || userReq.getIpFilters().trim().length() == 0) {
			user.setIpFilters(PmsConfig.INTERCEPTER_IP_FILTER);
		} else {
			user.setIpFilters(userReq.getIpFilters());
		}
		
		user.setMsgCntLimit(userReq.getMsgCntLimit());
		user.setStatus(-1);
		Token token = new Token();
		token.setUserId(userReq.getUserId());
		token.setTokenType(PmsConfig.TOKEN_TYPE_APPLICATION);
		token.setTokenId(this.getTokenId(userReq));

		//
		user.setAction("createUser");
		userMapper.logUserHistory(user);
		
		userMapper.insertUser(user);
		tokenMapper.insertToken(token);
		user.setStatus(PmsConfig.USER_STATUS_NORMAL);
		userMapper.updateUserStatus(user);
		//
		// userMapper udate

		return user.getUserId();
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
		
		User param = userMapper.select(userReq.getUserId());
		
		if (userReq.getUserName() != null)
		param.setUserName(userReq.getUserName());
		
		if (userReq.getMsgCntLimit() != 0)
		param.setMsgCntLimit(userReq.getMsgCntLimit());
		
		if (userReq.getRole() != null)
		param.setRole(userReq.getRole());
		
		//TODO validation 필요?
		if (userReq.getIpFilters() != null)
		param.setIpFilters(userReq.getIpFilters());
		
		
		param.setStatus(PmsConfig.USER_STATUS_NORMAL);
		param.setIssueId(issueId);
		
		param.setAction("updateUser");
		userMapper.logUserHistory(param);

		return userMapper.updateUser(param);
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
	

}
