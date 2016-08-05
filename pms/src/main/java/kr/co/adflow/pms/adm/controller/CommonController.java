/*
 * 
 */
package kr.co.adflow.pms.adm.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.co.adflow.pms.adm.request.AuthReq;
import kr.co.adflow.pms.adm.response.AuthRes;
import kr.co.adflow.pms.adm.service.CommonService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.domain.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
//
/**
 * The Class CommonController.
 */
@Controller
public class CommonController extends BaseController {

	/** The common service. */
	@Autowired
	private CommonService commonService;

	/**
	 * Auth user.
	 *
	 * @param auth
	 *            the auth
	 * @return the response
	 */
	@RequestMapping(value = "/adm/cmm/auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<Result<AuthRes>> authUser(@RequestBody @Valid AuthReq auth) throws Exception {

		AuthRes authRes = commonService.authUser(auth);

		Result<AuthRes> result = new Result<AuthRes>();
		result.setSuccess(true);
		result.setData(authRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<AuthRes>> res = new Response(result);
		return res;

	}

	/**
	 * Auth token.
	 *
	 * @param String
	 *            the token
	 * @return the response
	 */
	@RequestMapping(value = "/adm/cmm/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Response authToken(@RequestHeader(StaticConfig.HEADER_APPLICATION_TOKEN) String token) throws Exception {

		// if (token == null || token.trim().length() == 0) {
		//
		// throw new PmsRuntimeException("invalid token:"+token);
		// }

		boolean auth = commonService.authToken(token);

		Result<Validation> result = new Result<Validation>();
		result.setSuccess(true);
		Validation valid = new Validation(auth);
		result.setData(valid);
		Response res = new Response(result);
		return res;

	}

	/**
	 * Auth appKey.
	 *
	 * @param String
	 *            the appKey
	 * @return the response
	 */
	@RequestMapping(value = "/adm/cmm/authkey", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Response authKey(@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String key) throws Exception {

		// if (token == null || token.trim().length() == 0) {
		//
		// throw new PmsRuntimeException("invalid token:"+token);
		// }

		boolean auth = commonService.authKey(key);

		Result<Validation> result = new Result<Validation>();
		result.setSuccess(true);
		Validation valid = new Validation(auth);
		result.setData(valid);
		Response res = new Response(result);
		return res;

	}

}
