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
import org.springframework.web.bind.annotation.PathVariable;
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

	/**
	 * 설문조사입력
	 * 
	 * @param poll
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/polls", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
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

	/**
	 * 설문조사삭제
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/polls/{pollID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable int pollID) throws Exception {
		logger.debug("pollID=" + pollID);
		final int count = pollService.delete(pollID);
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

	// 설문조사수정(일단제외)

	/**
	 * 설문조사가져오기(detail)
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/polls/{pollID}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Poll> get(@PathVariable int pollID) throws Exception {
		Result<Poll> result = new Result<Poll>();
		result.setSuccess(true);
		Poll poll = pollService.get(pollID);
		if (poll == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("poll not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(poll);
		}

		Response<Poll> res = new Response<Poll>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 모든설문조사가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/polls", method = RequestMethod.GET)
	@ResponseBody
	public Response<Poll[]> getPolls() throws Exception {
		Result<Poll[]> result = new Result<Poll[]>();
		result.setSuccess(true);
		Poll[] poll = pollService.getPolls();
		if (poll == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("polls not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(poll);
		}

		Response<Poll[]> res = new Response<Poll[]>(result);
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
}
