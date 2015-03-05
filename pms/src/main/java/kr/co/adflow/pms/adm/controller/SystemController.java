/*
 * 
 */
package kr.co.adflow.pms.adm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SystemService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class SystemController.
 */
@Controller
@RequestMapping(value = "/adm/sys")
public class SystemController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SystemController.class);

	/** The system service. */
	@Autowired
	private SystemService systemService;

	/** The account service. */
	@Autowired
	private AccountService accountService;

	/**
	 * Gets the account.
	 *
	 * @param appKey the app key
	 * @return the account
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getAccount(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		User user = accountService.retrieveAccount(appKey);

		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}

	/**
	 * Modify account.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/account", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifyAccount(@RequestBody AccountReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		int resultCnt = accountService.modifyAccount(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifyAccount SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	/**
	 * Modify password.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/account/sec", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifyPassword(@RequestBody PasswordReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		int resultCnt = accountService.modifyPassword(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifyPassword SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<List<User>>> getUsers() throws Exception {

		List<User> resultList = systemService.listAllUser();

		Result<List<User>> result = new Result<List<User>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<User>>> res = new Response(result);
		return res;

	}

	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> createUser(
			@RequestBody UserReq user,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		logger.debug("addUser");

		String userId = systemService.createUser(user, appKey);

		Result<HashMap<String, String>> result = new Result<HashMap<String, String>>();
		result.setSuccess(true);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userId", userId);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, String>>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the users.
	 *
	 * @param userId the user id
	 * @return the users
	 */
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getUsers(@PathVariable("userId") String userId) {

		User user = systemService.retrieveUser(userId);

		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}

	/**
	 * Update user.
	 *
	 * @param userId the user id
	 * @param user the user
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> updateUser(
			@PathVariable("userId") String userId,
			@RequestBody UserUpdateReq user,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		logger.debug("updateUser");

		// userId force setting
		user.setUserId(userId);
		int resultCnt = systemService.updateUser(user, appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("updateCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> deleteUser(
			@PathVariable("userId") String userId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		int resultCnt = systemService.deleteUser(userId, appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("deleteCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}

	/**
	 * Sets the logger.
	 *
	 * @param level the level
	 * @return the response
	 */
	@RequestMapping(value = "/loglevel/{level}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> setLogger(
			@PathVariable("level") String level) {

		switch (level) {
		case "debug":
			org.apache.log4j.Logger.getLogger("kr.co.adflow.pms.domain")
					.setLevel(Level.TRACE);
			break;
		default:
			org.apache.log4j.Logger.getLogger("kr.co.adflow.pms.domain")
					.setLevel(Level.FATAL);
			break;
		}

		String str = org.apache.log4j.Logger
				.getLogger("kr.co.adflow.pms.domain").getLevel().toString();

		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setData(str);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the message list.
	 *
	 * @param params the params
	 * @param appKey the app key
	 * @return the message list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		MessagesRes messagesRes = systemService.getSysMessageList(params);

		messagesRes.setsEcho(sEcho);

		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the resevation message list.
	 *
	 * @param params the params
	 * @param appKey the app key
	 * @return the resevation message list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/reservations", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getResevationMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		MessagesRes messagesRes = systemService
				.getSysResevationMessageList(params);

		messagesRes.setsEcho(sEcho);

		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the server info.
	 *
	 * @return the server info
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/server", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<ServerInfo>> getServerInfo() throws Exception {
		Result<ServerInfo> result = new Result<ServerInfo>();
		result.setSuccess(true);
		result.setData(systemService.getServerInfo());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<ServerInfo>> res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * Cancel reservation list.
	 *
	 * @param appKey the app key
	 * @param ids the ids
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/cancel", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationList(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody ReservationCancelReq ids) throws Exception {

		Integer delCnt = systemService.cancelReservationList(appKey, ids);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}

	/**
	 * Gets the month summary.
	 *
	 * @param appKey the app key
	 * @param keyMon the key mon
	 * @return the month summary
	 */
	@RequestMapping(value = "/messages/summary/{month}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<List<Map<String, Object>>>> getMonthSummary(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@PathVariable("month") String keyMon) {

		List<Map<String, Object>> resultList = systemService.getMonthSummary(
				appKey, keyMon, null);

		Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String, Object>>>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the month summary.
	 *
	 * @param appKey the app key
	 * @param keyMon the key mon
	 * @param issueId the issue id
	 * @return the month summary
	 */
	@RequestMapping(value = "/messages/summary/{month}/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<List<Map<String, Object>>>> getMonthSummary(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@PathVariable("month") String keyMon,
			@PathVariable("userId") String issueId) {

		List<Map<String, Object>> resultList = systemService.getMonthSummary(
				appKey, keyMon, issueId);

		Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String, Object>>>> res = new Response(result);
		return res;

	}

}
