package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.GroupMessage;
import kr.co.adflow.push.domain.PersonalMessage;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.PushService;

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
 * @date 2014. 3. 20.
 */
@Controller
public class PushController {

	@Resource
	private PushService pushService;

	private static final Logger logger = LoggerFactory
			.getLogger(PushController.class);

	/**
	 * 푸시서비스동작여부확인
	 * 
	 * { result : { success: true, errors : [], - warnings : [], info : [], data
	 * : { available: true, message : "" } } }
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public Response isAvailable() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		result.setData(pushService.isAvailable());
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * @param msg
	 * @param topicName
	 * @return
	 */
	@RequestMapping(value = "topics/{topicName}", method = RequestMethod.POST)
	@ResponseBody
	public Response sendTopic(@RequestBody GroupMessage msg,
			@PathVariable String topicName) {
		logger.debug("그룹메시지=" + msg);
		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * @param msg
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userName}", method = RequestMethod.POST)
	@ResponseBody
	public Response sendMessage(@RequestBody PersonalMessage msg,
			@PathVariable String userName) throws Exception {
		logger.debug("개인메시지=" + msg);
		pushService.sendMessage(msg);
		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * @param msg
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "users", method = RequestMethod.POST)
	@ResponseBody
	public Response sendMessageAll(@RequestBody PersonalMessage msg) {
		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		return res;
	}

	/**
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

	public void shutdown() throws Exception {
		pushService.shutdown();
	}
}
