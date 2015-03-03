package kr.co.adflow.pms.adm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageReq;
import kr.co.adflow.pms.svc.service.PushMessageService;

@Controller
@RequestMapping(value = "/adm/svc")
public class SvcController extends BaseController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SvcController.class);
	
	@Autowired
	private SvcService svcService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PushMessageService pushMessageService;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getAccount(@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
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
			,@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
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
			,@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) {

		
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
	
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(@RequestParam Map<String,String> params
			,@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);
		
		MessagesRes messagesRes = svcService.getSvcMessageList(params);
		
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
			,@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);
		
		MessagesRes messagesRes = svcService.getSvcResevationMessageList(params);
		
		messagesRes.setsEcho(sEcho);
		
		
		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Map<String,String>>>> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody MessageReq msg) throws Exception {
		logger.debug("sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
			throw new RuntimeException("getReceivers is null");
		} else {
			String[] receivers = msg.getReceivers();
			for (int i = 0; i < receivers.length; i++) {
				if (!isValid(receivers[i])) {
					throw new RuntimeException("getReceivers not valid" + receivers[i]);
				}
			}
			
		}

		List<Map<String,String>> resultList = pushMessageService.sendMessage(appKey, msg);

		Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
		result.setSuccess(true);

		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String,String>>>> res = new Response(result);
		return res;

	}
	
	private boolean isValid(String receiver) {
		return userValidator.validRequestValue(receiver);
	}
	
	@RequestMapping(value = "/messages/cancel", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationList(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey
			,@RequestBody ReservationCancelReq ids) throws Exception {
		
		Integer delCnt = svcService.cancelReservationList(appKey,ids);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}


}
