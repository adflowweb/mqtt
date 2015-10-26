/*
 * 
 */
package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.AckService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class AckController.
 *
 * @author nadir93
 */
@Controller
public class AckController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AckController.class);

	/** The ack service. */
	@Resource
	private AckService ackService;

	/**
	 * 수신확인데이타 가져오기.
	 *
	 * @param msgID the msg id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "acks/{msgID}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Acknowledge[]> get(@PathVariable int msgID)
			throws Exception {
		logger.debug("msgID=" + msgID);
		Acknowledge[] acks = ackService.get(msgID);
		logger.debug("수신확인자명수=" + acks.length);
		Result<Acknowledge[]> result = new Result<Acknowledge[]>();
		result.setSuccess(true);
		result.setData(acks);
		Response<Acknowledge[]> res = new Response<Acknowledge[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 예외처리.
	 *
	 * @param e the e
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
