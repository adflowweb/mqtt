package kr.co.adflow.pms.inf.controller;


import java.util.List;



import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.adm.service.SvcAdmService;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.service.PCBSService;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageIdsReq;
import kr.co.adflow.pms.svc.service.PushMessageService;

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
@RequestMapping(value = "/svcadm")
public class DisptcherController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(DisptcherController.class);
	
	@Autowired
	private SvcAdmService svcAdmService;
	
	@Autowired
	private PushMessageService pushMessageService;



	@RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Map<String,String>>>> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {
		logger.info("SvcAdmController.sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
			throw new RuntimeException("getReceivers is null");
		} else {
			String[] receivers = msg.getReceivers();
			for (int i = 0; i < receivers.length; i++) {
				if (!isValid(receivers[i])) {
					throw new RuntimeException("getReceivers not valid :" + receivers[i]);
				}
			}
			
		}

		List<Map<String,String>> resultList = svcAdmService.sendMessage(appKey, msg);

		Result<List<Map<String,String>>> result = new Result<List<Map<String,String>>>();
		result.setSuccess(true);

		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String,String>>>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/messages/result", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<MessageResult>>> getMessageResult(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey
			,@RequestBody MessageIdsReq msgIds) throws Exception {
		
		//1. msgId 의 앞의5자 YYYYMM 이 같아야 함
		
		
		List<MessageResult> list = pushMessageService.getMessageResult(msgIds, appKey);
		
		Result<List<MessageResult>> result = new Result<List<MessageResult>>();
		result.setSuccess(true);

		result.setData(list);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<MessageResult>>> res = new Response(result);
		return res;
	}
	
	@RequestMapping(value = "/messages/{msgId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey
			,@PathVariable("msgId") String msgId) throws Exception {
		
		Integer delCnt = pushMessageService.cancelMessage(appKey,msgId);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}
	
	private boolean isValid(String receiver) {
		return true; //admin message 는 check 안함
		//return userValidator.validRequestValue(receiver);
	}

}
