/*
 * 
 */
package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.TokenRunTimeException;
import kr.co.adflow.pms.core.request.TokenReq;
import kr.co.adflow.pms.core.response.TokenRes;
import kr.co.adflow.pms.core.service.TokenService;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.mapper.TokenMapper;
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
	private TokenMapper tokenMapper;

	/**
	 * 유저 인증후 토큰 발행.
	 * 
	 * @param TokenReq
	 * 
	 * @return the response
	 */
	@RequestMapping(value = "/token", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<TokenRes> createToken(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody @Valid TokenReq userInfo) throws TokenRunTimeException {
		/* 권한 체크 시작************************** */
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		Token tokenId = tokenMapper.selectUserid(applicationKey);
		String requsetUserId = tokenId.getUserId();
		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_510)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_510 + "에 대한 권한이 없습니다");
			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료 되었습니다.");
		/* 권한체크 끝***************************** */

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
		try {
			tokenRes = tokenService.createToken(userInfo, requsetUserId);
		} catch (Exception e) {
			throw new TokenRunTimeException(StaticConfig.ERROR_CODE_510500,
					"토큰 생성에 실패 하였습니다");
		}

		Response<TokenRes> res = new Response<TokenRes>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(tokenRes);
		res.setCode(StaticConfig.SUCCESS_CODE_510);
		res.setMessage("토큰을 발급 하였습니다.");

		return res;

	}

	@ExceptionHandler(TokenRunTimeException.class)
	@ResponseBody
	public Response handleAllException(final TokenRunTimeException e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		HashMap<String, String> errMap = e.getErrorMsg();
		String errCode = errMap.get("errCode");
		String errMsg = errMap.get("errMsg");
		logger.error(errCode);
		logger.error(errMsg);
		res.setCode(errCode);
		res.setMessage(errMsg);
		return res;
	}

	@RequestMapping(value = "/validate/{token}", method = RequestMethod.GET)
	@ResponseBody
	public Response validate(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Response res = new Response();
		boolean validateToken = tokenService.authToken(token);
		if (validateToken == true) {
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setCode(StaticConfig.SUCCESS_CODE_511);
			res.setMessage("토큰을 인증 하였습니다.");
		} else {
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(StaticConfig.ERROR_CODE_511400);
			res.setMessage("토큰 정보를 찾을 수 없습니다.");
		}

		logger.debug("response:" + res);
		return res;
	}

	public Response getToken(@PathVariable String token) throws Exception {
		logger.debug("token=" + token);
		Response res = new Response();

		return res;
	}

}
