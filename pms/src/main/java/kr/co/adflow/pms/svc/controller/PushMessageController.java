/*
 * 
 */
package kr.co.adflow.pms.svc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.CallbackReq;
import kr.co.adflow.pms.svc.request.MessageIdsReq;
import kr.co.adflow.pms.svc.request.MessageReq;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class PushMessageController.
 */
@Controller
@RequestMapping(value = "/svc")
public class PushMessageController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageController.class);

	/** The push message service. */
	@Autowired
	private PushMessageService pushMessageService;
	
	/** The svc service. */
	@Autowired
	private SvcService svcService;

	/** The account service. */
	@Autowired
	private AccountService accountService;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;

	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Map<String, String>>>> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {
		logger.debug("sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
			throw new RuntimeException("getReceivers is null");
		} else {
			String[] receivers = msg.getReceivers();
			for (int i = 0; i < receivers.length; i++) {
				
//				if (!isValid(receivers[i])) {
////					throw new RuntimeException("Receiver format is invalid : "+ receivers[i]);
//					throw new PmsRuntimeException("Receiver format is invalid : "+ receivers[i]);
//				}
				
				//group topic check
				if (!(receivers[i].subSequence(0, 5).equals("mms/P")&&receivers[i].indexOf("g") > 0)) {

					if (!isValid(receivers[i])) {
//						throw new RuntimeException("getReceivers not valid"	+ receivers[i]);
						throw new PmsRuntimeException("getReceivers not valid"	+ receivers[i]);
					}

				}

			}

		}

		if (msg.getReservationTime() != null
				&& msg.getReservationTime().getTime() < System
						.currentTimeMillis()) {
//			throw new RuntimeException("ReservationTime is the past time");
			throw new PmsRuntimeException("ReservationTime is the past time");
		}

		List<Map<String, String>> resultList = pushMessageService.sendMessage(
				appKey, msg);

		Result<List<Map<String, String>>> result = new Result<List<Map<String, String>>>();
		result.setSuccess(true);

		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String, String>>>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the msg cnt limit.
	 *
	 * @param appKey the app key
	 * @return the msg cnt limit
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/msgCntLimit", method = RequestMethod.GET, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> getMsgCntLimit(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {
		logger.debug("getMsgCntLimit");

		User user = accountService.retrieveAccount(appKey);

		if (user == null) {
//			throw new RuntimeException("not found");
			throw new PmsRuntimeException("not found");
		}

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(user.getMsgCntLimit());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}

	/**
	 * Gets the message result.
	 *
	 * @param appKey the app key
	 * @param msgIds the msg ids
	 * @return the message result
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/result", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<MessageResult>>> getMessageResult(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody MessageIdsReq msgIds) throws Exception {

		// 1. msgId 의 앞의5자 YYYYMM 이 같아야 함

		List<MessageResult> list = pushMessageService.getMessageResult(msgIds,
				appKey);

		Result<List<MessageResult>> result = new Result<List<MessageResult>>();
		result.setSuccess(true);

		result.setData(list);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<MessageResult>>> res = new Response(result);
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
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);
		params.put("iDisplayLength", "100");

		MessagesRes messagesRes = svcService
				.getSvcResevationMessageList(params);

		messagesRes.setsEcho(sEcho);

		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}
	
	/**
	 * Gets the message detail list.
	 *
	 * @param params the params
	 * @param appKey the app key
	 * @return the message list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/{msgId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageDetailList(
			@RequestParam Map<String, String> params,
			@PathVariable("msgId") String msgId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		String keyMon = (String) params.get("keyMon");
//		params.put("appKey", appKey);

		MessagesRes messagesRes = svcService.getSvcMessageDetailList(msgId, keyMon);

//		messagesRes.setsEcho(sEcho);

		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}

	/**
	 * Cancel reservation message.
	 *
	 * @param appKey the app key
	 * @param msgId the msg id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/{msgId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@PathVariable("msgId") String msgId) throws Exception {

		Integer delCnt = pushMessageService.cancelMessage(appKey, msgId);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}

	/**
	 * Valid phone no.
	 *
	 * @param phoneNo the phone no
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/validation", method = RequestMethod.GET, params = "phoneNo", consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Boolean>> validPhoneNo(
			@RequestParam("phoneNo") String phoneNo) throws Exception {

		logger.info("validPhoneNo :{}", phoneNo);

		Boolean isValid = pushMessageService.validPhoneNo(phoneNo);

		Result<Boolean> result = new Result<Boolean>();
		result.setSuccess(true);

		result.setData(isValid);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Boolean>> res = new Response(result);
		return res;
	}

	/**
	 * Valid ufmi no.
	 *
	 * @param ufmiNo the ufmi no
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/validation", method = RequestMethod.GET, params = "ufmiNo", consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Boolean>> validUfmiNo(
			@RequestParam("ufmiNo") String ufmiNo) throws Exception {

		logger.info("validUfmiNo :{}", ufmiNo);

		Boolean isValid = pushMessageService.validUfmiNo(ufmiNo);

		Result<Boolean> result = new Result<Boolean>();
		result.setSuccess(true);

		result.setData(isValid);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Boolean>> res = new Response(result);
		return res;
	}

	/**
	 * Checks if is valid.
	 *
	 * @param receiver the receiver
	 * @return true, if is valid
	 */
	private boolean isValid(String receiver) {
		return userValidator.validRequestValue(receiver);
	}

	/**
	 * Callback post. for TEST
	 *
	 * @param req the req
	 * @return the response
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public Response<Result<CallbackReq>> callbackPost(@RequestBody CallbackReq req) {

		logger.info("callback test msgId : {}", req.getCallbackMsgId());

		Result<CallbackReq> result = new Result<CallbackReq>();
		result.setSuccess(true);
		result.setData(req);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<CallbackReq>> res = new Response(result);
		return res;

	}

	/**
	 * Callback.for TEST
	 *
	 * @param msgId the msg id
	 * @return the response
	 */
	@RequestMapping(value = "/callback/{msgId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> callback(@PathVariable("msgId") String msgId) {

		logger.info("callback test msgId : {}", msgId);

		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setData(msgId);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String>> res = new Response(result);
		return res;

	}
	
	
	/**
	 * Modify user name.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifySvcUser(@RequestBody UserReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) throws Exception{

		int resultCnt = accountService.modifySvcUser(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifySvcUser SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

}
