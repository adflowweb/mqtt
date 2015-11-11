/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.MessageRunTimeException;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.response.AckRes;
import kr.co.adflow.pms.core.response.MessageSendRes;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.core.service.MessageService;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.response.Response;

import org.json.JSONException;
import org.json.JSONObject;
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
	private PmsConfig pmsConfig;

	@Autowired
	private CheckUtil checkUtil;

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

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530400,
					"Qos 설정이 잘못 되었습니다");
		}

		if (msg.getMsgType() != 0) {
			msg.setMsgType(msg.getMsgType());
		} else {
			msg.setMsgType(StaticConfig.MESSAGE_TYPE_USER);
		}

		if (msg.getServiceId() == null
				|| msg.getServiceId().trim().length() == 0) {

			msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		}

		if (msg.getExpiry() == 0) {
			logger.debug("expiry 시간이 입력되지 않아 기본 시간으로 세팅툅니다!"
					+ pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);

			msg.setExpiry(pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
		} else {
			logger.debug("expiry 시간이 입력되었습니다!" + msg.getExpiry());
			long expiryTime = msg.getExpiry() * 1000;
			msg.setExpiry(expiryTime);
		}

		if (msg.getContentType().equals("application/json")) {

			try {
				JSONObject jsonObject = new JSONObject(msg.getContent());
			} catch (JSONException e) {
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_530400,
						"JSON Object로 변경할수 없습니다. ContentType과 Content를 확인해주세요");
			}
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

	@RequestMapping(value = "/system/messages/{msgType}", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<MessageSendRes> sendSystemMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody @Valid MessageReq msg, @PathVariable int msgType)
			throws Exception {

		logger.debug("=== msg::{}", "getContentType::" + msg.getContentType()
				+ ",getExpiry::" + msg.getExpiry() + ",getQos::" + msg.getQos()
				+ ",getReceiver::" + msg.getReceiver());

		if (msg.getReceiver() == null || msg.getReceiver().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_534404,
					"Receiver 가 없습니다");
		}
		if (msg.getContentType() == null
				|| msg.getContentType().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_534404,
					"Content-Type 이 없습니다");
		}
		if (msg.getContent() == null || msg.getContent().trim().length() == 0) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_534404,
					"Content 가 없습니다");
		}
		if (msg.getQos() < 0 || msg.getQos() > 2) {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530500,
					"Qos 설정이 잘못 되었습니다");
		}

		if (msg.getServiceId() == null
				|| msg.getServiceId().trim().length() == 0) {

			msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		}

		if (msg.getExpiry() == 0) {
			logger.debug("expiry 시간이 입력되지 않아 기본 시간으로 세팅툅니다!"
					+ pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);

			msg.setExpiry(pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
		} else {
			logger.debug("expiry 시간이 입력되었습니다!" + msg.getExpiry());
			long expiryTime = msg.getExpiry() * 1000;
			msg.setExpiry(expiryTime);
		}

		msg.setMsgType(msgType);
		if (msgType == 200) {
			logger.debug("KeepAlive Time 변경");

			String keepAliveTime = msg.getContent();
			if (!msg.getContentType().equals("application/json")) {
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_534400,
						"잘못된 요청 입니다(contentType의 값을 application/json 으로 입력해주세요)");
			}

			try {
				msg.setContent("{\"keepAliveTime\":"
						+ Integer.parseInt(keepAliveTime) + "}");
			} catch (Exception e) {
				e.printStackTrace();
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_534400,
						"잘못된 요청 입니다(content의 입력값을 확인해주세요)");
			}

		} else if (msgType == 201) {
			logger.debug("Trace Log 업로드 요청 API");
			String hostName = msg.getContent();
			if (!msg.getContentType().equals("application/json")) {
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_534400,
						"잘못된 요청 입니다(contentType의 값을 application/json 으로 입력해주세요)");
			}

			try {
				msg.setContent("{\"hostInfo\":\"" + hostName + "\"}");
			} catch (Exception e) {
				e.printStackTrace();
				throw new MessageRunTimeException(
						StaticConfig.ERROR_CODE_534400,
						"잘못된 요청 입니다(content의 입력값을 확인해주세요)");
			}

		}

		else {

			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_530500,
					"시스템 메시지의 타입이 잘못되었습니다");
		}

		Message msgSendData = messageService.sendMessage(msg, applicationKey);

		MessageSendRes messageSendRes = new MessageSendRes();
		messageSendRes.setContent(msgSendData.getContent());
		messageSendRes.setReceiver(msgSendData.getReceiver());
		messageSendRes.setMsgId(msgSendData.getMsgId());
		Response<MessageSendRes> res = new Response<MessageSendRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(messageSendRes);
		res.setCode(StaticConfig.SUCCESS_CODE_534);
		res.setMessage("메시지를 전송 하였습니다.");

		return res;

	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<MessagesListRes> getMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_531);

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
		res.setCode(StaticConfig.SUCCESS_CODE_531);
		res.setMessage("메시지 내역을 조회 하였습니다");

		return res;

	}

	@RequestMapping(value = "/messages/statistics", method = RequestMethod.GET)
	@ResponseBody
	public Response<StatisticsRes> getMessageStatistics(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_532);

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

	@RequestMapping(value = "/messages/ack/{msgId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<AckRes> getAckMessage(
			@RequestParam Map<String, String> params,
			@PathVariable String msgId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_533);

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
			res.setCode(StaticConfig.ERROR_CODE_539000);
			res.setMessage(e.getMessage());
		}

		e.printStackTrace();
		return res;
	}

}
