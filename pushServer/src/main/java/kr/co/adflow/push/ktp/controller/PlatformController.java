/*
 * 
 */
package kr.co.adflow.push.ktp.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.domain.Validation;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.domain.ktp.request.Precheck;
import kr.co.adflow.push.domain.ktp.request.Ufmi;
import kr.co.adflow.push.domain.ktp.request.UpdateUfmi;
import kr.co.adflow.push.domain.ktp.request.UserID;
import kr.co.adflow.push.domain.ktp.request.UserMessage;
import kr.co.adflow.push.exception.PushException;
import kr.co.adflow.push.executor.TpsExceutor;
import kr.co.adflow.push.ktp.service.PlatformService;
import kr.co.adflow.push.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class PlatformController.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Controller
public class PlatformController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(PlatformController.class);

	/** The platform service. */
	@Autowired
	private PlatformService platformService;

	@Autowired
	private UserService userService;

	/**
	 * Send precheck.
	 *
	 * @param precheck
	 *            the precheck
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "precheck", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void sendPrecheck(@RequestBody Precheck precheck) throws Exception {
		platformService.sendPrecheck(precheck.getReceiver());

		TpsExceutor.preCnt++;
		logger.debug("preCnt ++::" + TpsExceutor.preCnt);

	}

	/**
	 * Modify fw info.
	 *
	 * @param fwInfo
	 *            the fw info
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "devices/fwInfo", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyFwInfo(@RequestBody FwInfo fwInfo) throws Exception {

		if (fwInfo.getSender() == null || fwInfo.getSender().trim().length() == 0) {
			throw new PushException("Sender is empty.");
		}
		if (fwInfo.getReceiver() == null || fwInfo.getReceiver().trim().length() == 0) {
			throw new PushException("Receiver is empty.");
		}
		if (fwInfo.getContentType() == null || fwInfo.getContentType().trim().length() == 0) {
			throw new PushException("ContentType is empty.");
		}
		if (fwInfo.getContent() == null || fwInfo.getContent().trim().length() == 0) {
			throw new PushException("Content is empty.");
		}

		platformService.modifyFwInfo(fwInfo);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + fwInfo.getReceiver());
		messages.add("content=" + fwInfo.getContent());

		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	/**
	 * Modify keep alive time.
	 *
	 * @param keepAliveTime
	 *            the keep alive time
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "devices/keepAliveTime", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyKeepAliveTime(@RequestBody KeepAliveTime keepAliveTime) throws Exception {

		platformService.modifyKeepAliveTime(keepAliveTime);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + keepAliveTime.getReceiver());
		messages.add("content=" + keepAliveTime.getContent());

		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	/**
	 * Modify dig info.
	 *
	 * @param digInfo
	 *            the dig info
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "users/digAccountInfo", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyDigInfo(@RequestBody DigInfo digInfo) throws Exception {

		if (digInfo.getSender() == null || digInfo.getSender().trim().length() == 0) {
			throw new PushException("Sender is empty.");
		}
		if (digInfo.getReceiver() == null || digInfo.getReceiver().trim().length() == 0) {
			throw new PushException("Receiver is empty.");
		}
		if (digInfo.getContentType() == null || digInfo.getContentType().trim().length() == 0) {
			throw new PushException("ContentType is empty.");
		}
		if (digInfo.getContent() == null || digInfo.getContent().trim().length() == 0) {
			throw new PushException("Content is empty.");
		}

		platformService.modifyDigInfo(digInfo);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + digInfo.getReceiver());
		messages.add("content=" + digInfo.getContent());

		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response={}", res);

		TpsExceutor.digCnt++;
		logger.debug("digCnt ++::" + TpsExceutor.digCnt);

		return res;
	}

	/**
	 * Send message.
	 *
	 * @param message
	 *            the message
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "admin/message", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response sendMessage(@RequestBody Message message) throws Exception {

		platformService.sendMessage(message);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + message.getReceiver());
		messages.add("content=" + message.getContent());

		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	/**
	 * Send user message.
	 *
	 * @param msg
	 *            the msg
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "users/message", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response sendUserMessage(@RequestBody UserMessage msg) throws Exception {

		if (msg.getSender() == null || msg.getSender().trim().length() == 0) {
			throw new PushException("Sender is empty.");
		}
		if (msg.getReceiver() == null || msg.getReceiver().trim().length() == 0) {
			throw new PushException("Receiver is empty.");
		}
		if (msg.getContentType() == null || msg.getContentType().trim().length() == 0) {
			throw new PushException("ContentType is empty.");
		}
		if (msg.getContent() == null || msg.getContent().trim().length() == 0) {
			throw new PushException("Content is empty.");
		}

		platformService.sendUserMessage(msg);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + msg.getReceiver());
		messages.add("content=" + msg.getContent());

		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	/**
	 * Valid user id.
	 *
	 * @param userID
	 *            the user id
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "users/userID/validation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response validUserID(@RequestBody UserID userID) throws Exception {

		if (userID.getUserID() == null || userID.getUserID().trim().length() == 0) {
			throw new PushException("UserID is empty.");
		}

		Result result = new Result();
		result.setSuccess(true);
		Validation valid = new Validation(platformService.validUserID(userID));
		result.setData(valid);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}

	/**
	 * Valid ufmi.
	 *
	 * @param ufmi
	 *            the ufmi
	 * @return the response
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(value = "users/ufmi/validation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response validUFMI(@RequestBody Ufmi ufmi) throws Exception {

		if (ufmi.getUfmi() == null || ufmi.getUfmi().trim().length() == 0) {
			throw new PushException("ufmi is empty.");
		}

		Result result = new Result();
		result.setSuccess(true);
		Validation valid = new Validation(platformService.validUFMI(ufmi));
		result.setData(valid);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	@RequestMapping(value = "users/ufmi", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response updateUFMI(@RequestBody UpdateUfmi ufmi) throws Exception {

		if (ufmi.getPhoneNum() == null || ufmi.getPhoneNum().trim().length() == 0) {
			throw new PushException("PhoneNum is empty.");
		}
		if (ufmi.getUfmi() == null || ufmi.getUfmi().trim().length() == 0) {
			throw new PushException("Ufmi is empty.");
		}

		User user = new User();
		// user.setUserID(ufmi.getUserID());
		user.setUserID(ufmi.getPhoneNum());
		user.setUfmi(ufmi.getUfmi());

		userService.updateUFMI(user);

		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		logger.info("response={}", res);
		return res;
	}

	/**
	 * 예외처리.
	 *
	 * @param e
	 *            the e
	 * @return the response
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		logger.error("예외발생", e);
		Result result = new Result();
		result.setSuccess(false);
		List<String> messages = new ArrayList<String>() {
			{
				add(e.toString());
				// add(e.getMessage());
				// add("are u.");
			}
		};
		result.setErrors(messages);
		Response res = new Response(result);
		return res;
	}

}
