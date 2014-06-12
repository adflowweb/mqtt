package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class MessageController {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageController.class);

	@Resource
	private MessageService msgService;

	/**
	 * 메시지 전송하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.POST)
	@ResponseBody
	public Response post(@RequestBody Message msg) throws Exception {
		logger.debug("메시지=" + msg);
		msgService.post(msg);
		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 메시지 가져오기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages/{msgID}", method = RequestMethod.GET)
	@ResponseBody
	public Response get(@PathVariable int msgID) throws Exception {
		logger.debug("msgID=" + msgID);
		Message msg = msgService.get(msgID);
		logger.debug("메시지=" + msg);
		Result<Message> result = new Result<Message>();
		result.setSuccess(true);
		result.setData(msg);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 예외처리
	 * 
	 * @param e
	 * @return
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

	// /**
	// * @param msg
	// * @param topicName
	// * @return
	// */
	// @RequestMapping(value = "messages/groups/{groupID}", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public Response sendGroupMessage(@RequestBody Message msg,
	// @PathVariable String groupID) throws Exception {
	// msg.setReceiver("groups/" + groupID);
	// logger.debug("그룹메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(messageService.post(msg));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }

	// /**
	// * 푸시서비스동작여부확인
	// *
	// * { result : { success: true, errors : [], - warnings : [], info : [],
	// data
	// * : { available: true, message : "" } } }
	// *
	// * @return
	// *
	// * @throws Exception
	// */
	// @RequestMapping(value = "/", method = RequestMethod.GET)
	// @ResponseBody
	// public Response isAvailable() throws Exception {
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(pushService.isAvailable());
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param topicName
	// * @return
	// */
	// @RequestMapping(value = "groups/{groupID}", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendGroupMessage(@RequestBody Message msg,
	// @PathVariable String groupID) throws Exception {
	// msg.setReceiver("groups/" + groupID);
	// logger.debug("그룹메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param userName
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value = "users/{userID}", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendPersonalMessage(@RequestBody Message msg,
	// @PathVariable String userID) throws Exception {
	// msg.setReceiver("users/" + userID);
	// logger.debug("개인메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param userName
	// * @return
	// */
	// @RequestMapping(value = "users", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendMessageAll(@RequestBody Message msg) throws Exception
	// {
	// msg.setReceiver("users");
	// logger.debug("전체메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
}
