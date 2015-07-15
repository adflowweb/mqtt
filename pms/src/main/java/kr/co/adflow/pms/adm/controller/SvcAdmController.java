/*
 * 
 */
package kr.co.adflow.pms.adm.controller;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.MessageReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SvcAdmService;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.apache.commons.codec.binary.Base64;
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
 * The Class SvcAdmController.
 */
@Controller
@RequestMapping(value = "/adm/svcadm")
public class SvcAdmController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SvcAdmController.class);

	/** The svc service. */
	@Autowired
	private SvcService svcService;

	/** The svc adm service. */
	@Autowired
	private SvcAdmService svcAdmService;

	/** The account service. */
	@Autowired
	private AccountService accountService;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private PmsConfig pmsConfig;


	/**
	 * Gets the account.
	 *
	 * @param appKey the app key
	 * @return the account
	 */
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<User>> getAccount(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception{

		User user = accountService.retrieveAccount(appKey);

		Result<User> result = new Result<User>();
		result.setSuccess(true);
		result.setData(user);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<User>> res = new Response(result);
		return res;

	}

	/**
	 * Modify account.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/account", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifyAccount(@RequestBody AccountReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception{

		int resultCnt = accountService.modifyAccount(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifyAccount SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	/**
	 * Modify password.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/account/sec", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifyPassword(@RequestBody PasswordReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception{

		int resultCnt = accountService.modifyPassword(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifyPassword SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	/**
	 * Gets the message list.
	 *
	 * @param params the params
	 * @param appKey the app key
	 * @return the message list
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
			throws Exception {

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
	
	/**
	 * Gets the message list.
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
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
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
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

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
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {
		logger.info("SvcAdmController.sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
//			throw new RuntimeException("getReceivers is null");
			throw new PmsRuntimeException("getReceivers is null");
		} else {
			String[] receivers = msg.getReceivers();
			for (int i = 0; i < receivers.length; i++) {
				if (!isValid(receivers[i])) {
//					throw new RuntimeException("getReceivers not valid :"+ receivers[i]);
					throw new PmsRuntimeException("getReceivers not valid :"+ receivers[i]);
				}
			}

		}

		List<Map<String, String>> resultList = svcAdmService.sendMessage(
				appKey, msg);

		Result<List<Map<String, String>>> result = new Result<List<Map<String, String>>>();
		result.setSuccess(true);

		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String, String>>>> res = new Response(result);
		return res;

	}

	/**
	 * Checks if is valid.
	 *
	 * @param receiver the receiver
	 * @return true, if is valid
	 */
	private boolean isValid(String receiver) {
		return true; // admin message 는 check 안함
		// return userValidator.validRequestValue(receiver);
	}

	/**
	 * Cancel reservation list.
	 *
	 * @param appKey the app key
	 * @param ids the ids
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/messages/cancel", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> cancelReservationList(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody ReservationCancelReq ids) throws Exception {

		Integer delCnt = svcService.cancelReservationList(appKey, ids);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;
	}

	/**
	 * Gets the month summary.
	 *
	 * @param appKey the app key
	 * @param keyMon the key mon
	 * @return the month summary
	 */
	@RequestMapping(value = "/messages/summary/{month}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<List<Map<String, Object>>>> getMonthSummary(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@PathVariable("month") String keyMon) {

		List<Map<String, Object>> resultList = svcService.getMonthSummary(params,
				appKey, keyMon);

		Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Map<String, Object>>>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/messages/csv", method = RequestMethod.GET)
	@ResponseBody
	public void getMessageCSV(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			HttpServletResponse response)
			throws Exception {
		
		BufferedWriter writer = new BufferedWriter(response.getWriter());
		
		try {
        String csvFileName = "messages.csv";
        
        response.setContentType("text/csv");
 
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		//download cnt limit
		params.put("iDisplayStart", "0");
		params.put("iDisplayLength", pmsConfig.MESSAGE_CSV_LIMIT_DEFAULT+"");
		MessagesRes messagesRes = svcService.getSvcMessageListCvs(params);

		messagesRes.setsEcho(sEcho);
		
		writer.write(this.getCSVHeader());
		int len = messagesRes.getData().size();
		for (int i = 0; i < len; i++) {
			writer.write(this.getCSVData(messagesRes.getData().get(i)));
			
		}
		
		writer.flush();
		
		} catch(Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}


	}

	private String getCSVHeader() {
		StringBuffer result = new StringBuffer();
		result.append("발송시간").append(",")
		.append("Ptalk유형").append(",")
		.append("수신번호").append(",")
		.append("상세수신번호").append(",")
		.append("발송상태").append(",")
		.append("메세지확인").append(",")
		.append("메세지확인시간").append(",")
		.append("수신확인").append(",")
		.append("수신확인시간").append(",")
		.append("반복횟수").append(",")
		.append("반복시간").append(",")
		.append("내용").append(",")
		.append("발송형태").append("\n");

		return result.toString();
	}

	private String getCSVData(Message msg) {
		
		byte[] decode = Base64.decodeBase64(msg.getContent());
		String content = new String(decode).replaceAll(",", " ").replaceAll("\n", " ");
		String topic = msg.getReceiverTopic();
		String pTalkVer="";
		String topicTemp="";
		StringBuffer receiver = new StringBuffer();
		String ufmi = "";
		String sendType = "개인";
		if (topic == null) {

			
		} else {
			try {
				pTalkVer = "Ptalk"+topic.substring(5, 6)+".0";
				topicTemp = topic.substring(7, topic.length());
				topicTemp = topicTemp.replace("p", "");
				
				int firstT = topicTemp.indexOf("/");
				int lastT = topicTemp.lastIndexOf("/");
				int groupT = topicTemp.lastIndexOf("g");
				
				if (groupT > 0) {
					sendType = "그룹";
					receiver.append("그룹")
					.append(topicTemp.substring(groupT+1, topicTemp.length()))
					.append("(")
					.append(topicTemp.substring(firstT+1, lastT) )
					.append( ")");
					
				} else {
					receiver.append(topicTemp.substring(firstT+1, lastT))
					.append("*")
					.append(topicTemp.substring(lastT+1, topicTemp.length()));

				}
				
			} catch (StringIndexOutOfBoundsException e) {
				receiver.append("잘못된번호:")
				.append(topic);
			}


			String ufmiTemp = msg.getUfmi();
			
			if (ufmiTemp != null) {
				int firstU = ufmiTemp.indexOf("*");
				ufmi = ufmiTemp.substring(firstU+1, ufmiTemp.length());
			} 

		}
		String status;
		switch (msg.getStatus()) {
		case -99:
			status = "발송오류";
			break;
		case -2:
			status = "수신자없음";
			break;
		case 0:
			status = "발송중";
			break;
		case 1:
			status = "발송됨";
			break;
		case 2:
			status = "예약취소됨";
			break;

		default:
			status = "기타";
			break;
		}
		
		String appAck;
		String appAckTime = "";
		if (msg.getAppAckType() == null) {
			appAck = "응답없음";
		} else {
			appAck = "수신확인";
			appAckTime = DateUtil.getDateTimeSvc(msg.getAppAckTime());
		}
		
		String pmaAck;
		String pmaAckTime = "";
		if (msg.getPmaAckType() == null) {
			pmaAck = "응답없음";
		} else {
			pmaAck = "수신확인";
			pmaAckTime = DateUtil.getDateTimeSvc(msg.getPmaAckTime());
		}
		
		
		StringBuffer result = new StringBuffer();
		result.append(DateUtil.getDateTimeSvc(msg.getUpdateTime())).append(",")
		.append(pTalkVer).append(",")
		.append(receiver.toString()).append(",")
		.append(ufmi).append(",")
		.append(status).append(",")
		.append(appAck).append(",")
		.append(appAckTime).append(",")
		.append(pmaAck).append(",")
		.append(pmaAckTime).append(",")
		.append(msg.getResendCount()).append(",")
		.append(msg.getResendInterval()).append(",")
		.append(content).append(",")
		.append(sendType).append("\n");
		
		return result.toString();
	}

}
