/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import javax.validation.Valid;

import kr.co.adflow.pms.core.request.AuthReq;
import kr.co.adflow.pms.core.response.AuthRes;
import kr.co.adflow.pms.core.service.AuthService;
import kr.co.adflow.pms.domain.Validation;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
//
/**
 * The Class CommonController.
 */
@Controller
public class AuthController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthController.class);

	/** The common service. */
	@Autowired
	private AuthService authService;

	/**
	 * 유저 인증후 토큰 발행.
	 * 
	 * @param auth
	 *            the auth
	 * @return the response
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<Result<AuthRes>> authUser(@RequestBody @Valid AuthReq auth)
			throws Exception {

		AuthRes authRes = authService.authUser(auth);

		Result<AuthRes> result = new Result<AuthRes>();
		result.setSuccess(true);
		result.setData(authRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<AuthRes>> res = new Response(result);
		return res;

	}

	@RequestMapping(value = "auth/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response validate(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Result<Validation> result = new Result<Validation>();
		result.setSuccess(true);
		Validation valid = new Validation(authService.authToken(token));
		result.setData(valid);
		Response res = new Response(result);
		logger.debug("response:" + res);
		return res;
	}
}
