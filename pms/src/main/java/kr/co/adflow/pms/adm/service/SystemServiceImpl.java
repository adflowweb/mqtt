/*
 * 
 */
package kr.co.adflow.pms.adm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.io.*;

import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.dao.ServerDao;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.core.util.RecordFomatUtil;
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#createUser(kr.co.adflow.pms
	 * .adm.request.UserReq, java.lang.String)
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

		// 추가 옵션
		if (userReq.isOptions()) {
			paramUser.setDefaultExpiry(userReq.getDefaultExpiry());
			paramUser.setDefaultQos(userReq.getDefaultQos());

		} else {
			paramUser.setDefaultExpiry(-1);
			paramUser.setDefaultQos(-1);

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
	 * @param req
	 *            the req
	 * @return the password
	 */
	private String getPassword(UserReq req) {

		return KeyGenerator.createPw(req.getUserId(), req.getPassword());
	}

	/**
	 * Gets the token id.
	 * 
	 * @param req
	 *            the req
	 * @return the token id
	 */
	private String getTokenId(UserReq req) {

		return KeyGenerator.generateToken(req.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#retrieveUser(java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#updateUser(kr.co.adflow.pms
	 * .adm.request.UserUpdateReq, java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.AccountService#modifyPassword(kr.co.adflow
	 * .pms.adm.request.PasswordReq, java.lang.String)
	 */
	@Override
	public int resetPassword(PasswordReq req, String userId, String appKey)
			throws Exception {

		if (!req.getNewPassword().trim().equals(req.getRePassword().trim())) {
			// throw new RuntimeException("패스워드 변경 실패1");
			throw new PmsRuntimeException("패스워드 리셋 실패1");
		}

		// TODO OLD <> NEW 확인 필요?

		String issueId = interceptMapper.selectCashedUserId(appKey);

		User paramUser = new User();
		paramUser.setUserId(userId);

		String user = userMapper.selectUser(userId);
		if (user == null) {
			throw new PmsRuntimeException("user not valid");
		}

		paramUser.setAction("resetPassword");
		paramUser.setPassword(this.getPassword(userId, req.getNewPassword()));
		paramUser.setIssueId(issueId);

		int cnt = userMapper.logUserHistory(paramUser);

		if (cnt > 0) {
			cnt = userMapper.updatePassword(paramUser);
		} else {
			// throw new RuntimeException("패스워드 변경 실패3");
			throw new PmsRuntimeException("패스워드 리셋 실패3");
		}

		return cnt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#deleteUser(java.lang.String,
	 * java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#getSysMessageList(java.util
	 * .Map)
	 */
	@Override
	public MessagesRes getSysMessageList(Map<String, String> params)
			throws Exception {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId(params
				.get("appKey"));

		if (params.get("cSearchDate") == null) {
			// error
			throw new PmsRuntimeException("SearchDate is null");
		}

		MsgParams msgParams = new MsgParams();

		msgParams.setMsgType(0);

		msgParams.setKeyMon(params.get("cSearchDate"));

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

		logger.debug("[getSysMessageList]msgParams :::::::{}",
				msgParams.toString());
		int cnt = messageMapper.getSysMessageListCnt(msgParams);
		logger.debug("[getSysMessageList]cnt :::::::{}", cnt);

		List<Message> list = messageMapper.getSysMessageList(msgParams);
		logger.debug("[getSysMessageList]list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SysService#getSysMessageDetailList(java.
	 * util.Map)
	 */
	@Override
	public MessagesRes getSysMessageDetailList(String msgId, String keyMon)
			throws Exception {

		MessagesRes res = null;
		MsgParams msgParams = new MsgParams();
		msgParams.setMsgId(msgId);
		msgParams.setKeyMon(keyMon);

		List<Message> list = messageMapper.getSvcMessageDetailList(msgParams);
		logger.info("list size :::::::{}", list.size());

		res = new MessagesRes();
		res.setData(list);

		return res;
	}

	/**
	 * Gets the int.
	 * 
	 * @param string
	 *            the string
	 * @return the int
	 */
	private int getInt(String string) {

		return Integer.parseInt(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#getSysResevationMessageList
	 * (java.util.Map)
	 */
	@Override
	public MessagesRes getSysResevationMessageList(Map<String, String> params)
			throws Exception {

		MessagesRes res = null;

		String issueId = interceptMapper.selectCashedUserId(params
				.get("appKey"));

		// if (params.get("cSearchDate") == null) {
		// // error
		// // throw new RuntimeException("");
		// throw new PmsRuntimeException("SearchDate is null");
		// }

		MsgParams msgParams = new MsgParams();

		msgParams.setMsgType(0);

		// msgParams.setKeyMon(params.get("cSearchDate"));

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
		logger.debug("[getSysResevationMessageList]filter :::::::{}", filter);
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchContent"));
		} else if ("ack".equals(filter)) {
			msgParams.setAckType(this.getInt(params.get("cSearchContent")));
		}

		logger.debug("[getSysResevationMessageList]msgParams :::::::{}",
				msgParams.toString());

		int cnt = messageMapper.getSvcResevationMessageListCnt(msgParams);
		logger.debug("[getSysResevationMessageList]cnt :::::::{}", cnt);

		List<Message> list = messageMapper
				.getSvcResevationMessageList(msgParams);
		logger.debug("[getSysResevationMessageList]list size :::::::{}",
				list.size());

		res = new MessagesRes();
		res.setRecordsFiltered(cnt);
		res.setRecordsTotal(cnt);
		res.setData(list);

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.pms.adm.service.SystemService#getServerInfo()
	 */
	@Override
	public ServerInfo getServerInfo() throws Exception {
		// TODO Auto-generated method stub
		try {

			ServerInfo info = serverDao.get();

			info.setTps(this.getTPS(-1));

			return info;
		} catch (Exception e) {
			logger.error("getServerInfo", e);
			// throw new RuntimeException("getServerInfo error");
			throw new PmsRuntimeException("getServerInfo error");
		}
	}

	private double getTPS(int preMonth) {
		double result = 0.0;

		for (int j = preMonth; j <= 0; j++) {
			result += summaryMapper.getTPS(DateUtil.getYYYYMM(j));
		}

		return result;
	}

	/**
	 * Gets the string array.
	 * 
	 * @param string
	 *            the string
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
	 * @param string
	 *            the string
	 * @return the date
	 */
	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#cancelReservationList(java
	 * .lang.String, kr.co.adflow.pms.adm.request.ReservationCancelReq)
	 */
	@Override
	public int cancelReservationList(String appKey, ReservationCancelReq reqIds)
			throws Exception {

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
	 * @param msgIds
	 *            the msg ids
	 * @return the key mon
	 */
	private String getKeyMon(String[] msgIds) throws Exception {
		if (msgIds.length < 1) {
			// throw new RuntimeException("msgId not found");
			throw new PmsRuntimeException("msgId not found");
		}
		return msgIds[0].substring(0, 6);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SystemService#getMonthSummary(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getMonthSummary(
			Map<String, String> params, String appKey, String keyMon,
			String issueId) throws Exception {

		MsgParams msgParams = new MsgParams();
		msgParams.setMsgType(0);
		msgParams.setIssueId(issueId);
		msgParams.setKeyMon(keyMon);
		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		return summaryMapper.getMonthSummary(msgParams);
	}

	/**
	 * Gets the key Test.
	 * 
	 * @param
	 * @return
	 */
	public String testRun() {
		String re = null;
		//
		// FileWriter write = null;
		// File file = null;
		// String fileNamePrefix = "EM";
		// String fileNameDate ="";
		// int fileNameSeq = 0;
		//
		// RecordFomatUtil rf = new RecordFomatUtil();

		this.cDRCopyCommand();

		return re;
	}

	/**
	 * Gets the password.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @return the password
	 */
	private String getPassword(String userId, String password) {

		return KeyGenerator.createPw(userId, password);
	}

	public void cDRCopyCommand() {

		// String path = pmsConfig.CDR_FILE_PATH+this.fileDate;
		// logger.info("path ::{}", path);
		//
		// String targetDir = pmsConfig.CDR_TARGETFILE_PATH;
		// String shellDir = pmsConfig.CDR_FILE_PATH;

		// String command =
		// "cat /Users/gwang/Desktop/siteDoc/KTP/150401/localhost_access_log.2015-06-29.txt|grep dig |wc -l";
		String command = "/Users/gwang/Desktop/siteDoc/KTP/150401/test.sh";
		// String command = shellDir + "copyCDRFile.sh"+ " 123 " + path +"/* " +
		// targetDir;

		System.out.println("==== shell command ::{}" + command);

		java.lang.Runtime runTime = java.lang.Runtime.getRuntime();
		java.lang.Process process;
		try {
			System.out.println("==== 11111");
			process = runTime.exec(command);
			System.out.println("==== 22222");

			process.waitFor();
			StringBuffer output = new StringBuffer();
			System.out.println("==== 33333");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			System.out.println("==== 44444");
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
			System.out.println("==== 55555");
			System.out.println("==== command shell result  ::{}"
					+ output.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
