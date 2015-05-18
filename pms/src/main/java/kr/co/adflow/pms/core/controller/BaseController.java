/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.pms.adm.service.CommonServiceImpl;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

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
	public Response<Result<List<String>>> handleAllException(final Exception e) {

		

		if (e instanceof PmsRuntimeException) {
//			System.out.println("=== PmsRuntimeException 맞어 ");
			
		} else {
			logger.error("런타임에러발생::",e);
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
		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(false);
		List<String> messages = new ArrayList<String>();
		messages.add(errorMessage);
		result.setErrors(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;
	}

}
