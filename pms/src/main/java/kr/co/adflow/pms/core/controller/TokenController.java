/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.HashMap;

import javax.validation.Valid;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.SubscriptionsRes;
import kr.co.adflow.pms.core.response.TokenInfoRes;
import kr.co.adflow.pms.core.response.TokenRes;
import kr.co.adflow.pms.core.service.PCFService;
import kr.co.adflow.pms.core.service.TokenService;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class TokenController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(TokenController.class);

	/** The common service. */
	@Autowired
	private TokenService tokenService;

	@Autowired
	private PCFService pcfService;

	@Autowired
	private CheckUtil checkUtil;

	/**
	 * 유저 인증후 토큰 발행.
	 * 
	 * @param TokenReq
	 * 
	 * @return the response
	 * @throws Exception
	 */
	@RequestMapping(value = "/token", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<TokenRes> createToken(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody @Valid TokenReq userInfo) throws Exception {
		logger.debug("/token(POST)==========");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_510);

		String userId = userInfo.getUserId();
		String deivceId = userInfo.getDeviceId();

		if (userId == null || userId.trim().length() == 0) {
			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510400,
					"잘못된 접근 : 사용자 아이디가 없습니다");
		}

		if (deivceId == null || deivceId.trim().length() == 0) {
			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510400,
					"잘못된 접근: 디바이스 아이디가 없습니다");
		}

		TokenRes tokenRes = null;

		tokenRes = tokenService.createToken(userInfo, requestUserId);

		Response<TokenRes> res = new Response<TokenRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(tokenRes);
		res.setCode(StaticConfig.SUCCESS_CODE_510);
		res.setMessage("토큰을 발급 하였습니다.");
		logger.debug("토큰발급결과:"+res.toString());
		return res;

	}

	@ResponseBody
	@RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
	public Response<TokenInfoRes> getToken(@PathVariable String token)
			throws Exception {
		logger.debug("/token/{token}(GET)==========");
		logger.debug("token=" + token);
		Response<TokenInfoRes> res = new Response<TokenInfoRes>();
		Token tokenData = tokenService.getTokenInfo(token);
		if (tokenData == null) {
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(StaticConfig.ERROR_CODE_513404);
			res.setMessage("해당 토큰에 관한 정보를 찾을 수 없습니다");

		} else {
			TokenInfoRes infoRes = new TokenInfoRes();
			infoRes.setDeviceId(tokenData.getDeviceId());
			infoRes.setIssueTime(tokenData.getIssueTime());
			infoRes.setUserId(tokenData.getUserId());
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setData(infoRes);
			res.setCode(StaticConfig.SUCCESS_CODE_513);
			res.setMessage("토큰 정보를 조회하였습니다");

		}

		return res;
	}

	@RequestMapping(value = "/validate/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response validate(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@PathVariable String token) throws Exception {
		logger.debug("/validate/{token}(GET)==========");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_511);
		Response res = new Response();
		boolean validateToken = tokenService.authToken(token);
		if (validateToken == true) {
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setCode(StaticConfig.SUCCESS_CODE_511);
			res.setMessage("토큰을 인증 하였습니다.");
		} else {
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(StaticConfig.ERROR_CODE_511404);
			res.setMessage("토큰 정보를 찾을 수 없습니다.");
		}

		logger.debug("response:" + res);
		return res;
	}

	@RequestMapping(value = "/token/connection/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response getTokenStatus(@PathVariable String token) throws Exception {
		logger.debug("/token/connection/{token}(GET)==========");
		logger.debug("token:" + token);
		Response res = new Response();
		String tokenStatus = pcfService.getStatus(token);
		if (tokenStatus.equals("MQTT Connected")) {
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setCode(StaticConfig.SUCCESS_CODE_512);
			res.setMessage("MQTT 에 연결된 상태 입니다");
		} else {
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(StaticConfig.ERROR_CODE_512500);
			res.setMessage("MQTT 연결이 끊겼습니다");

		}

		logger.debug("response=" + res);

		return res;
	}

	@RequestMapping(value = "/token/subscriptions/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response<SubscriptionsRes> getTopic(@PathVariable String token)

	throws Exception {
		logger.debug("/token/subscriptions/{token}(GET)==========");
		logger.debug("token=" + token);

		Response<SubscriptionsRes> res = new Response<SubscriptionsRes>();
		SubscriptionsRes subscriptionsRes = new SubscriptionsRes();
		subscriptionsRes.setTopic(pcfService.getTopics(token));
		if (subscriptionsRes.getTopic() == null) {
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(StaticConfig.ERROR_CODE_514404);
			res.setMessage("Subscriptions 목록을 찾을 수 없습니다");
			res.setExplaination("해당 토큰의 Subscriptions 목록을 조회 하였으나 그 값이 없습니다");

		} else {
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setData(subscriptionsRes);
			res.setCode(StaticConfig.SUCCESS_CODE_514);
			res.setMessage("Subscriptions 목록을 조회하였습니다");

		}

		logger.debug("response:" + res);
		return res;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof TokenRunTimeException) {
			HashMap<String, String> errMap = ((TokenRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_519000);
			res.setMessage(e.getMessage());
		}
		e.printStackTrace();
		return res;
	}

}
