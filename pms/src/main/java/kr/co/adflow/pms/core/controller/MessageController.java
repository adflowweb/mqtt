/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.service.MessageService;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class UserMessageController.
 */
@Controller
public class MessageController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageController.class);

	/** The push message service. */
	@Autowired
	private MessageService messageService;

	/**
	 * Send message.
	 * 
	 * @param appKey
	 *            the app key
	 * @param msg
	 *            the msg
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<String>>> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {

		logger.debug("=== msg::{}",
				"getContentType::" + msg.getContentType() + ",getExpiry::"
						+ msg.getExpiry() + ",getQos::" + msg.getQos()
						+ ",getReceiver::" + msg.getReceiver() + ",getSender::"
						+ msg.getSender());

		if (msg.getSender() == null || msg.getSender().trim().length() == 0) {

			throw new PmsRuntimeException("Sender is empty.");
		}
		if (msg.getReceiver() == null || msg.getReceiver().trim().length() == 0) {

			throw new PmsRuntimeException("Receiver is empty.");
		}
		if (msg.getContentType() == null
				|| msg.getContentType().trim().length() == 0) {

			throw new PmsRuntimeException("ContentType is empty.");
		}
		if (msg.getContent() == null || msg.getContent().trim().length() == 0) {

			throw new PmsRuntimeException("Content is empty.");
		}

		int resultCnt = messageService.sendMessage(msg, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + msg.getReceiver());
		messages.add("content=" + msg.getContent());

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		MessagesRes messagesRes = messageService.getMessageListById(params);

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
	public Response<Result<MessagesRes>> getResevationMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		MessagesRes messagesRes = messageService
				.getResevationMessageList(params);

		messagesRes.setsEcho(sEcho);

		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}

	@RequestMapping(value = "/messages/cancel", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationList(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody ReservationCancelReq ids) throws Exception {

		Integer delCnt = messageService.cancelReservationList(appKey, ids);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}

}
