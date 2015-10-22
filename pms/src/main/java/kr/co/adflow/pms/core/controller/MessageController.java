/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.MessageRunTimeException;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.response.AckRes;
import kr.co.adflow.pms.core.response.MessageSendRes;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.core.service.MessageService;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Autowired
	private TokenMapper tokenMapper;

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
	public Response<MessageSendRes> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {

		logger.debug("=== msg::{}", "getContentType::" + msg.getContentType()
				+ ",getExpiry::" + msg.getExpiry() + ",getQos::" + msg.getQos()
				+ ",getReceiver::" + msg.getReceiver());

		if (msg.getReceiver() == null || msg.getReceiver().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530404,
					"Receiver 가 없습니다");
		}
		if (msg.getContentType() == null
				|| msg.getContentType().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530404,
					"Content-Type 이 없습니다");
		}
		if (msg.getContent() == null || msg.getContent().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530404,
					"Content 가 없습니다");
		}
		if (msg.getQos() < 0 || msg.getQos() > 2) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530500,
					"Qos 설정이 잘못 되었습니다");
		}

		Message msgSendData = messageService.sendMessage(msg, appKey);

		MessageSendRes messageSendRes = new MessageSendRes();
		messageSendRes.setContent(msgSendData.getContent());
		messageSendRes.setReceiver(msgSendData.getReceiver());
		messageSendRes.setMsgId(msgSendData.getMsgId());
		Response<MessageSendRes> res = new Response<MessageSendRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(messageSendRes);
		res.setCode(StaticConfig.SUCCESS_CODE_530);
		res.setMessage("메시지를 전송 하였습니다.");

		return res;

	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<MessagesListRes> getMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {
		/* 권한 체크 시작************************** */
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		Token tokenId = tokenMapper.selectUserid(applicationKey);
		String requsetUserId = tokenId.getUserId();

		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_531)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_531 + "에 대한 권한이 없습니다");
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료 되었습니다.");
		/* 권한체크 끝***************************** */

		if (params.get("iDisplayStart") == null
				|| params.get("iDisplayStart").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531404,
					"조회 시작 번호가 없습니다");
		}
		if (params.get("iDisplayLength") == null
				|| params.get("iDisplayLength").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531404,
					"조회 길이가 없습니다");
		}

		if (params.get("cSearchDateStart") == null
				|| params.get("cSearchDateStart").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531404,
					"시작 날짜가 없습니다");
		}

		if (params.get("cSearchDateEnd") == null
				|| params.get("cSearchDateEnd").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531404,
					"끝 날짜가 없습니다");
		}

		if (params.get("cSearchStatus") == null
				|| params.get("cSearchStatus").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_531404,
					"조회 상태코드가 없습니다");
		}
		String sEcho = (String) params.get("sEcho");
		params.put("appKey", applicationKey);

		MessagesListRes messagesListRes = messageService.getMessageList(params);

		messagesListRes.setsEcho(sEcho);
		Response<MessagesListRes> res = new Response<MessagesListRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(messagesListRes);
		res.setCode(StaticConfig.SUCCESS_CODE_530);
		res.setMessage("메시지 내역을 조회 하였습니다");

		return res;

	}

	@RequestMapping(value = "/messages/statistics", method = RequestMethod.GET)
	@ResponseBody
	public Response<StatisticsRes> getMessageStatistics(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {
		/* 권한 체크 시작************************** */
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		Token tokenId = tokenMapper.selectUserid(applicationKey);
		String requsetUserId = tokenId.getUserId();

		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_532)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_532 + "에 대한 권한이없습니다");
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_532401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료 되었습니다.");
		/* 권한체크 끝***************************** */

		if (params.get("cSearchDateStart") == null
				|| params.get("cSearchDateStart").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_532404,
					"시작 날짜가 없습니다");
		}

		if (params.get("cSearchDateEnd") == null
				|| params.get("cSearchDateEnd").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_532404,
					"끝 날짜가 없습니다");
		}

		StatisticsRes statisticsRes = messageService.getStatistics(params);

		Response<StatisticsRes> res = new Response<StatisticsRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(statisticsRes);
		res.setCode(StaticConfig.SUCCESS_CODE_532);
		res.setMessage("메시지 내역을 조회 하였습니다");

		return res;

	}

	@RequestMapping(value = "/messages/ack/{msgId:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<AckRes> getAckMessage(
			@RequestParam Map<String, String> params,
			@PathVariable String msgId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {
		/* 권한 체크 시작************************** */
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		Token tokenId = tokenMapper.selectUserid(applicationKey);
		String requsetUserId = tokenId.getUserId();

		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_533)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_533 + "에 대한 권한이없습니다");
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_533401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료 되었습니다.");
		/* 권한체크 끝***************************** */

		if (params.get("cSearchDateStart") == null
				|| params.get("cSearchDateStart").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_533404,
					"시작 날짜가 없습니다");
		}

		if (params.get("cSearchDateEnd") == null
				|| params.get("cSearchDateEnd").trim().length() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_533404,
					"끝 날짜가 없습니다");
		}

		AckRes ackRes = messageService.getAckMessage(params, msgId);

		Response<AckRes> res = new Response<AckRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(ackRes);
		res.setCode(StaticConfig.SUCCESS_CODE_533);
		res.setMessage("수신확인 내역을 조회 하였습니다");

		return res;

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof MessageRunTimeException) {
			HashMap<String, String> errMap = ((MessageRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_519000);
			res.setMessage(e.getMessage());
		}
		return res;
	}

	// @RequestMapping(value = "/messages/reservations", method =
	// RequestMethod.GET)
	// @ResponseBody
	// public Response<Result<MessagesRes>> getResevationMessageList(
	// @RequestParam Map<String, String> params,
	// @RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey)
	// throws Exception {
	//
	// String sEcho = (String) params.get("sEcho");
	// params.put("appKey", appKey);
	//
	// MessagesRes messagesRes = messageService
	// .getResevationMessageList(params);
	//
	// messagesRes.setsEcho(sEcho);
	//
	// Result<MessagesRes> result = new Result<MessagesRes>();
	// result.setSuccess(true);
	// result.setData(messagesRes);
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// Response<Result<MessagesRes>> res = new Response(result);
	// return res;
	//
	// }
	//
	// @RequestMapping(value = "/messages/cancel", method =
	// RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE,
	// produces = StaticConfig.HEADER_CONTENT_TYPE)
	// @ResponseBody
	// public Response<Result<Integer>> cancelReservationList(
	// @RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
	// @RequestBody ReservationCancelReq ids) throws Exception {
	//
	// Integer delCnt = messageService.cancelReservationList(appKey, ids);
	//
	// Result<Integer> result = new Result<Integer>();
	// result.setSuccess(true);
	//
	// result.setData(delCnt);
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// Response<Result<Integer>> res = new Response(result);
	// return res;
	// }

}
