package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.ServerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class ServerController {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerControllerTest.class);

	@Resource
	private ServerService serverService;

	/**
	 * 푸시서버정보 가져오기
	 * 
	 * { result : { success: true, errors : [], - warnings : [], info : [], data
	 * : { available: true, message : "" } } }
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "server", method = RequestMethod.GET)
	@ResponseBody
	public Response get() throws Exception {
		Result result = new Result();
		result.setSuccess(true);
		result.setData(serverService.get());
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
