package kr.co.adflow.pms.adm.controller;

import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemController.class);

	@RequestMapping(value = "/adm/sys/users", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> getUsers() throws Exception {

		Result<String> result = new Result<String>();
		result.setSuccess(true);

		result.setData("");
		Response<Result<String>> res = new Response(result);
		return res;

	}
}
