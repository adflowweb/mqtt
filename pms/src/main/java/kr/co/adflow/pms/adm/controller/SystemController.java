package kr.co.adflow.pms.adm.controller;

import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.service.SystemService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
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
public class SystemController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemController.class);
	
	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/adm/sys/users", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/adm/sys/users", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> createUser(
			@RequestBody UserReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("addUser");

		String userId = systemService.createUser(user,appKey);

		Result<HashMap<String, String>> result = new Result<HashMap<String, String>>();
		result.setSuccess(true);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userId", userId);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, String>>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/adm/sys/users/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getUsers(@PathVariable("userId") String userId) throws Exception {

		
	  User user = systemService.retrieveUser(userId);
		
		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/adm/sys/users/{userId}", method = RequestMethod.PUT, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> updateUser(
			@PathVariable("userId") String userId
			,@RequestBody UserReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("updateUser");

		//userId force setting
		user.setUserId(userId);
		int resultCnt = systemService.updateUser(user,appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("updateCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/adm/sys/users/{userId}", method = RequestMethod.DELETE, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> deleteUser(
			@PathVariable("userId") String userId
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		int resultCnt = systemService.deleteUser(userId,appKey);

		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
		result.setSuccess(true);
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("deleteCnt", resultCnt);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, Integer>>> res = new Response(result);
		return res;

	}
	
	
	
	@RequestMapping(value = "/adm/sys/loglevel/{level}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> setLogger(@PathVariable("level") String level) throws Exception {

//		switch (level) {
//		case "debug":
//		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
//			break;
//		case "info":
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
//			break;
//		case "error":
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.ERROR);
//			break;
//		default:
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.FATAL);
//			break;
//		}
//		
//		String str = org.apache.log4j.Logger.getRootLogger().getLevel().toString();

		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setData("");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String>> res = new Response(result);
		return res;

	}
}
