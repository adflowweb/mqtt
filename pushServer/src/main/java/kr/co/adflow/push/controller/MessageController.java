package kr.co.adflow.push.controller;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
	private MessageService messageService;

	/**
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.POST)
	@ResponseBody
	public Response post(@RequestBody Message msg) throws Exception {
		logger.debug("메시지=" + msg);
		Result result = new Result();
		result.setSuccess(true);
		result.setData(messageService.post(msg));
		Response res = new Response(result);
		logger.debug("response=" + res);
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
}
