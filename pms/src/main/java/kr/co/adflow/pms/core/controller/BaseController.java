/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseController.
 */
@Controller
public class BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	/**
	 * 예외처리.
	 * 
	 * @param e
	 *            the e
	 * @return the response
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		logger.error("런타임에러발생::", e);
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		res.setCode("900000");
		res.setMessage(e.getMessage());

		return res;
	}
}
