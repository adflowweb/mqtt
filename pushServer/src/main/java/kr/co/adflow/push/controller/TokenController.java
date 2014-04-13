package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.TokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
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
		Result result = new Result();
		result.setSuccess(true);
		result.setData(tokenService.get(token));
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 토큰 유효성 체크
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "validate/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response validate(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Result result = new Result();
		result.setSuccess(true);
		result.setData(tokenService.validate(token));
		Response res = new Response(result);
		logger.debug("response=" + res);
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
}
