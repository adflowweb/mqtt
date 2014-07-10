package kr.co.adflow.push.bsbank.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.service.PollService;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.bsbank.Poll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
@Controller
public class PollController {

	private static final Logger logger = LoggerFactory
			.getLogger(PollController.class);

	@Resource
	private PollService pollService;

	// 설문조사입력

	@RequestMapping(value = "/bsbank/poll", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response post(@RequestBody Poll poll) throws Exception {
		logger.debug("설문조사=" + poll);
		final int count = pollService.post(poll);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	// 설문조사삭제
	// 설문조사수정(일단제외)
	// 설문조사조회

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
}
