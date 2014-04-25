package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.UserService;

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
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Resource
	private UserService userService;

	/**
	 * 유저정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userID}", method = RequestMethod.GET)
	@ResponseBody
	public Response get(@PathVariable String userID) throws Exception {
		logger.debug("userID=" + userID);
		Result result = new Result();
		result.setSuccess(true);
		result.setData(userService.get(userID));
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable String userID) throws Exception {
		logger.debug("userID=" + userID);
		userService.delete(userID);
		Result result = new Result();
		result.setSuccess(true);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저정보 입
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users", method = RequestMethod.POST)
	@ResponseBody
	public Response post(@RequestBody User user) throws Exception {
		logger.debug("메시지=" + user);
		userService.post(user);
		Result result = new Result();
		result.setSuccess(true);
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
}
