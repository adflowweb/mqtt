package kr.co.adflow.pms.inf.controller;

import java.util.ArrayList;
import java.util.HashMap;


import java.util.List;



import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.service.PCBSService;
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
@RequestMapping(value = "/inf")
public class PCBSController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PCBSController.class);

	@Autowired
	private PCBSService pcbsService;


	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> addUser(
			@RequestBody UserReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) throws Exception {

		logger.debug("addUser");

		String userId = pcbsService.addUser(user,appKey);

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

		
	  User user = pcbsService.retrieveUser(userId);
		
		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/users/{userId}/sec", method = RequestMethod.PUT, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response modifyPassword(@RequestBody PasswordReq req
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) {

		
	  int resultCnt = pcbsService.modifyPassword(req, appKey);
		
	  
	  List<String> messages = new ArrayList<String>();
	  messages.add("modifyPassword SUCCESS :"+resultCnt);
	  
	  
		Result result = new Result();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/users/{userId}/msgCntLimit", method = RequestMethod.PUT, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, Integer>>> updateUser(
			@PathVariable("userId") String userId
			,@RequestBody UserUpdateReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) {

		logger.debug("updateUser");

		//userId force setting
		user.setUserId(userId);
		int resultCnt = pcbsService.updateUser(user,appKey);

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
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) {

		int resultCnt = pcbsService.deleteUser(userId,appKey);

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
