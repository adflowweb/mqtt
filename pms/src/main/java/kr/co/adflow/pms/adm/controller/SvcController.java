/*
 * 
 */
package kr.co.adflow.pms.adm.controller;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import kr.co.adflow.pms.adm.request.AccountReq;
import kr.co.adflow.pms.adm.request.AddressDelReq;
import kr.co.adflow.pms.domain.AddressMessage;
import kr.co.adflow.pms.adm.request.AddressMessageReq;
import kr.co.adflow.pms.adm.request.AddressReq;
import kr.co.adflow.pms.adm.request.PasswordReq;
import kr.co.adflow.pms.adm.request.ReservationCancelReq;
import kr.co.adflow.pms.adm.request.TemplateReq;
import kr.co.adflow.pms.adm.request.UserReq;
import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.Address;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.Template;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageReq;
import kr.co.adflow.pms.svc.service.PushMessageService;
import kr.co.adflow.pms.users.service.UserMessageService;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
 * The Class SvcController.
 */
@Controller
@RequestMapping(value = "/adm/svc")
public class SvcController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(SvcController.class);

	/** The svc service. */
	@Autowired
	private SvcService svcService;

	/** The account service. */
	@Autowired
	private AccountService accountService;

	/** The push message service. */
	@Autowired
	private PushMessageService pushMessageService;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;
	
	/** The svc service. */
	@Autowired
	private UserMessageService userMessageService;
	
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
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)throws Exception{

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
	@RequestMapping(value = "/messages2", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList2(
			@RequestParam Map<String, String> params,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey)
			throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);

		MessagesRes messagesRes = svcService.getSvcMessageList2(params);

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
        
        response.setContentType("text/csv;");
 
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
		String content = new String(decode);
		String topic = msg.getReceiverTopic();
		String pTalkVer="";
		String topicTemp="";
		StringBuffer receiver = new StringBuffer();
		String ufmi = "";
		String sendType = "개인";
		if (topic == null) {

			
		} else {
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
			@RequestBody MessageReq msg) throws Exception {
		logger.debug("sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
//			throw new RuntimeException("getReceivers is null");
			throw new PmsRuntimeException("getReceivers is null");
		} else {
			String[] receivers = msg.getReceivers();
			for (int i = 0; i < receivers.length; i++) {
				
				//group topic check
				if (!(receivers[i].subSequence(0, 5).equals("mms/P")&&receivers[i].indexOf("g") > 0)) {

					if (!isValid(receivers[i])) {
//						throw new RuntimeException("getReceivers not valid"	+ receivers[i]);
						throw new PmsRuntimeException("getReceivers not valid"	+ receivers[i]);
					}

				}
				
				
			}

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
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/address/messages", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Map<String, String>>>> sendAddressMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody AddressMessageReq addressMsg) throws Exception {
		logger.debug("sendMessage");
		
		if (addressMsg.getAddressMessageArray() == null || addressMsg.getAddressMessageArray().length == 0) {
			//
			throw new PmsRuntimeException("getAddressMessageArray is null");
		} else {

			for (AddressMessage addressMessage : addressMsg.getAddressMessageArray()) {
				//group topic check
				if (!(addressMessage.getReceiver().subSequence(0, 5).equals("mms/P")&&addressMessage.getReceiver().indexOf("g") > 0)) {

					if (!isValid(addressMessage.getReceiver())) {
						throw new PmsRuntimeException("getReceivers not valid :"	+ addressMessage.getReceiver());
					}

				}
			}
		}
		
		
		List<Map<String, String>> resultList = pushMessageService.sendAddressMessage(
				appKey, addressMsg);

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
		return userValidator.validRequestValue(receiver);
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
	
	/**
	 * Modify user name.
	 *
	 * @param req the req
	 * @param appKey the app key
	 * @return the response
	 */
	@RequestMapping(value = "/users/name", method = RequestMethod.PUT)
	@ResponseBody
	public Response<Result<List<String>>> modifyUserName(@RequestBody UserReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception{

		int resultCnt = accountService.modifyUserName(req, appKey);

		List<String> messages = new ArrayList<String>();
		messages.add("modifyUserName SUCCESS :" + resultCnt);

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}
	
	
	
	/**
	 * add address.
	 *
	 * @param appKey the app key
	 * @param AddressReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/address", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> addAdress(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody AddressReq req) throws Exception {

		logger.debug("=== AdressReq ::{}", req.toString());
		
		int insertCnt = 0;
		try {
			insertCnt = svcService.addAdress(appKey, req);
		}  catch (DuplicateKeyException e) {
			throw new PmsRuntimeException("DuplicateKeyException");
		}
		

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(insertCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	/**
	 * update address.
	 *
	 * @param appKey the app key
	 * @param AddressReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/address", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> updateAdress(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody AddressReq req) throws Exception {

		logger.debug("=== AdressReq ::{}", req.toString());

		int insertCnt = svcService.updateAdress(appKey, req);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(insertCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	/**
	 * get address list.
	 *
	 * @param appKey the app key
	 * @param AddressReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/address", method = RequestMethod.GET, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Address>>> getAdress(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("=== appKey ::{}", appKey);

		List<Address> resultList = svcService.getAddressList(appKey);

		Result<List<Address>> result = new Result<List<Address>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Address>>> res = new Response(result);
		return res;

	}
	
	/**
	 * delete address.
	 *
	 * @param appKey the app key
	 * @param String the ufmi
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/address/delete", method = RequestMethod.POST, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> deleteAdress(
			@RequestBody AddressDelReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("=== appKey ::"+appKey);
		
		String[] ufmiArray = req.getUfmiArray();

		int delCnt = svcService.deleteAddress(appKey, ufmiArray);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	
	
	
	/**
	 * add Template.
	 *
	 * @param appKey the app key
	 * @param TemplateReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/template", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> addTemplate(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody TemplateReq req) throws Exception {

		logger.debug("=== TemplateReq ::{}", req.toString());
		


		int insertCnt = svcService.addTemplate(appKey, req);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(insertCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	/**
	 * update Template.
	 *
	 * @param appKey the app key
	 * @param TemplateReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/template", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> updateTemplate(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey,
			@RequestBody TemplateReq req) throws Exception {

		logger.debug("=== TemplateReq ::{}", req.toString());

		int insertCnt = svcService.updateTemplate(appKey, req);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(insertCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	/**
	 * get Template list.
	 *
	 * @param appKey the app key
	 * @param TemplateReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/template", method = RequestMethod.GET, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Template>>> getTemplate(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("=== appKey ::{}", appKey);

		List<Template> resultList = svcService.getTemplateList(appKey);

		Result<List<Template>> result = new Result<List<Template>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Template>>> res = new Response(result);
		return res;

	}
	
	
	/**
	 * get Template search. 
	 *
	 * @param appKey the app key
	 * @param TemplateReq the req
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/template/searchTemplateName", method = RequestMethod.POST, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<Template>>> searchTemplateName(
			@RequestBody TemplateReq req,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("=== appKey ::{}", appKey);

		List<Template> resultList = svcService.serchTemplateName(req.getTemplateName(), appKey);

		Result<List<Template>> result = new Result<List<Template>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<Template>>> res = new Response(result);
		return res;

	}
	
	/**
	 * delete Template.
	 *
	 * @param appKey the app key
	 * @param String the Template Id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/template/{templateId}", method = RequestMethod.DELETE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> deleteTemplate(
			@PathVariable("templateId") String templateId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		logger.debug("=== appKey ::{}, templateId::{}", appKey, templateId);

		int delCnt = svcService.deleteTemplate(appKey, templateId);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(delCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		return res;

	}
	
	
	/**
	 * group topic subscribe list count.
	 *
	 * @param toipc the topic
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/subscribe/count", method = RequestMethod.GET, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> getgroupsListCnt(
			@RequestParam("topic") String topic) throws Exception {

		logger.debug("=== topic ::"+ topic);

		Integer resultCnt = userMessageService.groupListCnt (topic);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(resultCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);
		
		logger.debug("=== resultCnt :{}", resultCnt);
		return res;

	}
	

}
