package kr.co.adflow.pms.svc.controller;

import java.util.List;

import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageIdsReq;
import kr.co.adflow.pms.svc.request.MessageReq;
import kr.co.adflow.pms.svc.service.PushMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/svc")
public class PushMessageController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageController.class);

	@Autowired
	private PushMessageService pushMessageService;
	
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<String[]>> sendMessage(
			@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey,
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

		String[] msgIdArray = pushMessageService.sendMessage(appKey, msg);

		Result<String[]> result = new Result<String[]>();
		result.setSuccess(true);

		result.setData(msgIdArray);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String[]>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/messages/msgCntLimit", method = RequestMethod.GET, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> getMsgCntLimit(
			@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) throws Exception {
		logger.debug("getMsgCntLimit");
		
		User user = accountService.retrieveAccount(appKey);
		
		if (user == null) {
			throw new RuntimeException("not found");
		} 
		
		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(user.getMsgCntLimit());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}
	
	
	@RequestMapping(value = "/messages/result", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<MessageResult>>> getMessageResult(
			@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey
			,@RequestBody MessageIdsReq msgIds) throws Exception {
		
		//1. msgId 의 앞의5자 YYYYMM 이 같아야 함
		
		
		List<MessageResult> list = pushMessageService.getMessageResult(msgIds, appKey);
		
		//User user = accountService.retrieveAccount(appKey);
//		
//		if (user == null) {
//			throw new RuntimeException("not found");
//		} 
		
		Result<List<MessageResult>> result = new Result<List<MessageResult>>();
		result.setSuccess(true);

		result.setData(list);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<MessageResult>>> res = new Response(result);
		return res;
	}
	
	private boolean isValid(String receiver) {
		//1. 01012341234
		//2. 082*1234*1234
		//3. 00*1234*1234
		
//		String str = receiver.trim();
//		
//		if (11 > str.length() || str.length() > 13) {
//			return false;
//		}
		
		// 1. 010 일때는 모두 숫자
		// 2. 아닐 때는 * 2개
		
		return true;
	}

}
