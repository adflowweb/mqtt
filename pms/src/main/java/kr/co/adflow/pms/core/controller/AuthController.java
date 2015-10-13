/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import javax.validation.Valid;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.request.AuthReq;
import kr.co.adflow.pms.core.response.AuthRes;
import kr.co.adflow.pms.core.service.AuthService;
import kr.co.adflow.pms.domain.Validation;
import kr.co.adflow.pms.response.Response;

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

	/* Interface Code */
	private final static String S_501_SUCCESS_CODE = "501200";
	private final static String S_502_SUCCESS_CODE = "502200";

	/**
	 * 유저 인증후 토큰 발행.
	 * 
	 * @param auth
	 *            the auth
	 * @return the response
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<AuthRes> authUser(@RequestBody @Valid AuthReq auth)
			throws Exception {

		AuthRes authRes = authService.authUser(auth);

		Response<AuthRes> res = new Response<AuthRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(authRes);
		res.setCode(S_501_SUCCESS_CODE);
		res.setMessage("사용자 인증에 성공 하였습니다.");
		
	

		return res;

	}

	@RequestMapping(value = "/auth/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Validation> validate(@PathVariable String token)
			throws Exception {
		logger.debug("token=" + token);
		Response<Validation> res = new Response<Validation>();
		Validation valid = new Validation(authService.authToken(token));
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(valid);
		res.setCode(S_502_SUCCESS_CODE);
		res.setMessage("토큰을 인증 하였습니다.");
		logger.debug("response:" + res);
		return res;
	}
}
