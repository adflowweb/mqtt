package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.AuthResponseData;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 3. 21.
 */
@Controller
public class AuthController {
	private static final Logger logger = LoggerFactory
			.getLogger(AuthController.class);

	@Resource
	private AuthService authService;

	/**
	 * mqtt 사용자 인증
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userID}", method = RequestMethod.GET)
	@ResponseBody
	public Response authencate(@PathVariable String userID,
			@RequestParam("clientID") String clientID) throws Exception {
		logger.debug("userID=" + userID);
		logger.debug("clientID=" + clientID);
		Result result = new Result();
		result.setSuccess(true);
		result.setData(new AuthResponseData(authService.authencate(userID,
				clientID), userID, clientID));
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
