package kr.co.adflow.pms.adm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.request.UserUpdateReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SystemService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
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

@Controller
@RequestMapping(value = "/adm/sys")
public class SystemController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemController.class);
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getAccount(@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
	  User user = accountService.retrieveAccount(appKey);
		
		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}
	
	
	@RequestMapping(value = "/account", method = RequestMethod.PUT)
	@ResponseBody
	public Response modifyAccount(@RequestBody AccountReq req
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
	  int resultCnt = accountService.modifyAccount(req, appKey);
		
	  
	  List<String> messages = new ArrayList<String>();
	  messages.add("modifyAccount SUCCESS :"+resultCnt);
	  
	  
		Result result = new Result();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/account/sec", method = RequestMethod.PUT)
	@ResponseBody
	public Response modifyPassword(@RequestBody PasswordReq req
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
	  int resultCnt = accountService.modifyPassword(req, appKey);
		
	  
	  List<String> messages = new ArrayList<String>();
	  messages.add("modifyPassword SUCCESS :"+resultCnt);
	  
	  
		Result result = new Result();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response res = new Response(result);
		return res;

	}

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
	
	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> createUser(
			@RequestBody UserReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

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
	
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> updateUser(
			@PathVariable("userId") String userId
			,@RequestBody UserUpdateReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

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
	
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> deleteUser(
			@PathVariable("userId") String userId
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) {

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
	
	
	
	@RequestMapping(value = "/loglevel/{level}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> setLogger(@PathVariable("level") String level) {

		switch (level) {
		case "debug":
		org.apache.log4j.Logger.getLogger("kr.co.adflow.pms.domain").setLevel(Level.TRACE);
			break;
		default:
			org.apache.log4j.Logger.getLogger("kr.co.adflow.pms.domain").setLevel(Level.FATAL);
			break;
		}
		
		String str = org.apache.log4j.Logger.getLogger("kr.co.adflow.pms.domain").getLevel().toString();

		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setData(str);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(@RequestParam Map<String,String> params
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

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
	
	@RequestMapping(value = "/messages/reservations", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getResevationMessageList(@RequestParam Map<String,String> params
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);
		
		MessagesRes messagesRes = systemService.getSysResevationMessageList(params);
		
		messagesRes.setsEcho(sEcho);
		
		
		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}
	
	
	@RequestMapping(value = "/server", method = RequestMethod.GET)
	@ResponseBody
	public Response getServerInfo() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		result.setData(systemService.getServerInfo());
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}
}
