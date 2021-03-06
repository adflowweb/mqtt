package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.domain.Validation;
import kr.co.adflow.push.service.TokenService;

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
 * @date 2014. 3. 21.
 */
@Controller
public class TokenController {
	private static final Logger logger = LoggerFactory
			.getLogger(TokenController.class);

	@Resource
	private TokenService tokenService;

	/**
	 * 토큰정보가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "tokens/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response get(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Result<Token> result = new Result<Token>();
		result.setSuccess(true);
		result.setData(tokenService.get(token));
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 토큰 발행하기
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "tokens", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response post(@RequestBody Token token) throws Exception {
		logger.debug("token=" + token);
		if (token.getUserID() == null || token.getDeviceID() == null
				|| token.getUserID().equals("")
				|| token.getDeviceID().equals("")) {
			Result<Object> result = new Result<Object>();
			result.setSuccess(true);
			List<String> messages = new ArrayList<String>() {
				{
					add("userID 또는 deviceID 값이 없습니다.");
				}
			};
			result.setErrors(messages);
			return new Response(result);
		}

		User user = new User();
		user.setUserID(token.getUserID());
		user.setDeviceID(token.getDeviceID());
		Token rst = tokenService.post(user);
		Result<Token> result = new Result<Token>();
		result.setSuccess(true);
		result.setData(rst);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 토큰유효성체크
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "validate/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response validate(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Result<Validation> result = new Result<Validation>();
		result.setSuccess(true);
		Validation valid = new Validation(tokenService.validate(token));
		result.setData(valid);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 토큰정보 삭제하기
	 * 
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "tokens/{token}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		final int count = tokenService.delete(token);
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
