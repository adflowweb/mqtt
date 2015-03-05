/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.dao.ServerDao;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgIdsParams;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.SummaryMapper;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemServiceImpl.
 */
@Service
public class SystemServiceImpl implements SystemService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SystemServiceImpl.class);

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;
	
	/** The token mapper. */
	@Autowired
	private TokenMapper tokenMapper;
	
	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The server dao. */
	@Autowired
	private ServerDao serverDao;

	/** The check util. */
	@Autowired
	private CheckUtil checkUtil;

	/** The summary mapper. */
	@Autowired
	private SummaryMapper summaryMapper;

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#listAllUser()
	 */
	@Override
	public List<User> listAllUser() {

		List<User> resultUsers = null;
		resultUsers = userMapper.listAll();

		for (User resultUser : resultUsers) {
			resultUser.setDefaultExpiry(checkUtil.getMessageExpiry(resultUser
					.getDefaultExpiry()));
			resultUser.setDefaultQos(checkUtil.getMessageQos(resultUser
					.getDefaultQos()));
			resultUser.setMsgSizeLimit(checkUtil.getMessageSizeLimit(resultUser
					.getMsgSizeLimit()));
		}

		return resultUsers;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#createUser(kr.co.adflow.pms.adm.request.UserReq, java.lang.String)
	 */
	@Override
	public String createUser(UserReq userReq, String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = new User();
		paramUser.setUserId(userReq.getUserId());
		paramUser.setUserName(userReq.getUserName());
		paramUser.setPassword(this.getPassword(userReq));
		paramUser.setRole(userReq.getRole());
		paramUser.setIssueId(issueId);

		if (userReq.getIpFilters() == null
				|| userReq.getIpFilters().trim().length() == 0) {
			paramUser.setIpFilters(StaticConfig.INTERCEPTER_IP_FILTER);
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
		token.setTokenType(StaticConfig.TOKEN_TYPE_APPLICATION);
		token.setTokenId(this.getTokenId(userReq));

		//
		paramUser.setAction("createUser");
		userMapper.logUserHistory(paramUser);

		userMapper.insertUser(paramUser);
		tokenMapper.insertToken(token);
		paramUser.setStatus(StaticConfig.USER_STATUS_NORMAL);
		userMapper.updateUserStatus(paramUser);
		//
		// userMapper udate

		return paramUser.getUserId();
	}

	/**
	 * Gets the password.
	 *
	 * @param req the req
	 * @return the password
	 */
	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	/**
	 * Gets the token id.
	 *
	 * @param req the req
	 * @return the token id
	 */
	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#retrieveUser(java.lang.String)
	 */
	@Override
	public User retrieveUser(String userId) {
		User resultUser = null;
		resultUser = userMapper.select(userId);

		resultUser.setDefaultExpiry(checkUtil.getMessageExpiry(resultUser
				.getDefaultExpiry()));
		resultUser.setDefaultQos(checkUtil.getMessageQos(resultUser
				.getDefaultQos()));
		resultUser.setMsgSizeLimit(checkUtil.getMessageSizeLimit(resultUser
				.getMsgSizeLimit()));

		return resultUser;

	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#updateUser(kr.co.adflow.pms.adm.request.UserUpdateReq, java.lang.String)
	 */
	@Override
	public int updateUser(UserUpdateReq userReq, String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = userMapper.select(userReq.getUserId());

		if (userReq.getUserName() != null)
			paramUser.setUserName(userReq.getUserName());

		if (userReq.getMsgCntLimit() != 0)
			paramUser.setMsgCntLimit(userReq.getMsgCntLimit());

		if (userReq.getRole() != null)
			paramUser.setRole(userReq.getRole());

		// TODO validation 필요?
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

		paramUser.setStatus(StaticConfig.USER_STATUS_NORMAL);
		paramUser.setIssueId(issueId);

		paramUser.setAction("updateUser");
		userMapper.logUserHistory(paramUser);

		return userMapper.updateUser(paramUser);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#deleteUser(java.lang.String, java.lang.String)
	 */
	@Override
	public int deleteUser(String userId, String appKey) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User param = userMapper.select(userId);
		param.setIssueId(issueId);
		param.setAction("deleteUser");
		userMapper.logUserHistory(param);
		tokenMapper.deleteUserToken(userId);
		return userMapper.deleteUser(userId);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#getSysMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSysMessageList(Map<String, String> params) {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId(params
				.get("appKey"));

		if (params.get("cSearchDate") == null) {
			// error
		}

		MsgParams msgParams = new MsgParams();

		msgParams.setKeyMon(params.get("cSearchDate"));

		logger.info("msgParams :::::::{}", issueId);
		msgParams.setIssueId(null);

		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		msgParams.setStatusArray(this.getStringArray(params
				.get("cSearchStatus")));

		if (params.get("userId") != null
				&& params.get("userId").trim().length() > 0) {
			msgParams.setIssueId(params.get("userId"));
		}

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
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper.getSvcMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}

	/**
	 * Gets the int.
	 *
	 * @param string the string
	 * @return the int
	 */
	private int getInt(String string) {

		return Integer.parseInt(string);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#getSysResevationMessageList(java.util.Map)
	 */
	@Override
	public MessagesRes getSysResevationMessageList(Map<String, String> params) {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId(params
				.get("appKey"));

		if (params.get("cSearchDate") == null) {
			// error
			throw new RuntimeException("");
		}

		MsgParams msgParams = new MsgParams();

		msgParams.setKeyMon(params.get("cSearchDate"));

		logger.info("msgParams :::::::{}", issueId);
		msgParams.setIssueId(null);

		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		// 예약 발송 예정만 처리?
		// msgParams.setStatusArray(this.getStringArray(params.get("cSearchStatus")));

		if (params.get("userId") != null
				&& params.get("userId").trim().length() > 0) {
			msgParams.setIssueId(params.get("userId"));
		}

		String filter = params.get("cSearchFilter");
		logger.info("filter :::::::{}", filter);
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}

		int cnt = messageMapper.getSvcResevationMessageListCnt(msgParams);
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper
				.getSvcResevationMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#getServerInfo()
	 */
	@Override
	public ServerInfo getServerInfo() {
		// TODO Auto-generated method stub
		try {
			return serverDao.get();
		} catch (Exception e) {
			throw new RuntimeException("getServerInfo error");
		}
	}

	/**
	 * Gets the string array.
	 *
	 * @param string the string
	 * @return the string array
	 */
	private String[] getStringArray(String string) {
		if ("ALL".equals(string)) {
			return null;
		}
		return string.split(",");
	}

	/**
	 * Gets the date.
	 *
	 * @param string the string
	 * @return the date
	 */
	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#cancelReservationList(java.lang.String, kr.co.adflow.pms.adm.request.ReservationCancelReq)
	 */
	@Override
	public int cancelReservationList(String appKey, ReservationCancelReq reqIds) {

		String issueId = interceptMapper.selectCashedUserId(appKey);

		MsgIdsParams params = new MsgIdsParams();

		params.setKeyMon(this.getKeyMon(reqIds.getMsgIds()));
		params.setMsgIds(reqIds.getMsgIds());
		params.setIssueId(issueId);
		params.setUpdateId(issueId);

		int cnt = messageMapper.cancelReservationList(params);

		return cnt;
	}

	/**
	 * Gets the key mon.
	 *
	 * @param msgIds the msg ids
	 * @return the key mon
	 */
	private String getKeyMon(String[] msgIds) {
		if (msgIds.length < 1) {
			throw new RuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.pms.adm.service.SystemService#getMonthSummary(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getMonthSummary(String appKey,
			String keyMon, String issueId) {

		// String issueId = interceptMapper.selectCashedUserId(appKey);

		Map<String, String> params = new HashMap<String, String>();

		params.put("issueId", issueId);
		params.put("keyMon", keyMon);

		return summaryMapper.getMonthSummary(params);
	}

}
