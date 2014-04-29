package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.DeviceService;

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
 * @date 2014. 4. 28.
 * 
 */
@Controller
public class DeviceController {

	private static final Logger logger = LoggerFactory
			.getLogger(DeviceController.class);

	@Resource
	private DeviceService deviceService;

	/**
	 * 디바이스정보 삭제하기
	 * 
	 * 
	 * @param deviceID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "devices/{deviceID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable String deviceID) throws Exception {
		logger.debug("deviceID=" + deviceID);
		final int count = deviceService.delete(deviceID);
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
