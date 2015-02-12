package kr.co.adflow.pms.adm.controller;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;
import kr.co.adflow.pms.adm.service.CommonService;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//
@Controller
public class CommonController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/adm/cmm/auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<Result<AuthRes>> authUser(@RequestBody @Valid AuthReq auth) {

		AuthRes authRes = commonService.authUser(auth);

		Result<AuthRes> result = new Result<AuthRes>();
		result.setSuccess(true);
		result.setData(authRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<AuthRes>> res = new Response(result);
		return res;

	}

}
