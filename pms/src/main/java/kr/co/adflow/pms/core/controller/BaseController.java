package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController {

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

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(false);
		@SuppressWarnings("serial")
		List<String> messages = new ArrayList<String>() {
			{
				add(e.toString());
				// add(e.getMessage());
				// add("are u.");
			}
		};
		result.setErrors(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;
	}

}
