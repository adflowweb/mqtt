package kr.co.adflow.pms.core.controller;

import java.util.HashMap;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.service.UserService;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> createUser(
			@RequestBody UserReq user) throws Exception {

		logger.debug("createUser");

		String userId = userService.createUser(user);

		Result<HashMap<String, String>> result = new Result<HashMap<String, String>>();
		result.setSuccess(true);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userId", userId);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, String>>> res = new Response(result);
		return res;
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getUsers(
			@PathVariable("userId") String userId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		User user = userService.retrieveUser(userId);

		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> updateUser(
			@PathVariable("userId") String userId,
			@RequestBody UserUpdateReq user,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		logger.debug("updateUser");

		// userId force setting
		user.setUserId(userId);
		int resultCnt = userService.updateUser(user, appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("updateCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> deleteUser(
			@PathVariable("userId") String userId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		int resultCnt = userService.deleteUser(userId, appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("deleteCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}

}
