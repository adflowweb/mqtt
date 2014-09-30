package kr.co.adflow.push.ktp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.ktp.Status;
import kr.co.adflow.push.domain.ktp.Subscribe;
import kr.co.adflow.push.ktp.service.PCFService;

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
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class PCFController {

	private static final Logger logger = LoggerFactory
			.getLogger(PCFController.class);

	@Resource
	private PCFService pCFService;

	/**
	 * subscription List 가져오기
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "subscriptions/{token:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Subscribe[]> get(@PathVariable String token)
			throws Exception {
		logger.debug("token=" + token);
		Result<Subscribe[]> result = new Result<Subscribe[]>();
		result.setSuccess(true);
		Subscribe[] subscribe = pCFService.get(token);
		if (subscribe == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("subscription not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(subscribe);
		}

		Response<Subscribe[]> res = new Response<Subscribe[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * MQTT Client 연결상태 가져오기
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "clients/{token:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Status> getStatus(@PathVariable String token)
			throws Exception {
		logger.debug("token=" + token);
		Status status = pCFService.getStatus(token);

		Result<Status> result = new Result<Status>();
		result.setSuccess(true);
		result.setData(status);
		Response<Status> res = new Response<Status>(result);
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