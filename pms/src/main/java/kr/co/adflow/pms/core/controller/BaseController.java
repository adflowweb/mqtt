/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

		if (e instanceof PmsRuntimeException) {
			// System.out.println("=== PmsRuntimeException 맞어 ");

		} else {
			logger.error("런타임에러발생::", e);
		}

		String errorMessage = null;
		// org.springframework.web.bind.MethodArgumentNotValidException

		if (e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
			BindingResult br = me.getBindingResult();

			errorMessage = br.getFieldError().getField();
			for (ObjectError error : br.getAllErrors()) {
				errorMessage += " : " + error.getCode() + " : "
						+ error.getDefaultMessage();
			}

		} else {
			errorMessage = e.getMessage();
		}
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		res.setCode(StaticConfig.RESPONSE_RUNTIME_EXCEPTION_CODE);
		res.setMessage(errorMessage);
	

		return res;
	}
}
