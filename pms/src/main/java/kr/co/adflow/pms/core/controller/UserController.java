package kr.co.adflow.pms.core.controller;

import java.util.HashMap;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.UserRunTimeException;
import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.response.UserInfoRes;
import kr.co.adflow.pms.core.response.UserRes;
import kr.co.adflow.pms.core.service.UserService;
import kr.co.adflow.pms.core.util.CheckUtil;
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

@Controller
public class UserController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CheckUtil checkUtil;

	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<UserRes> createUser(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody UserReq userReq) throws Exception {

		logger.debug("createUser");

		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_550);

		String userId = userReq.getUserId();
		String password = userReq.getPassword();
		int role = userReq.getRole();

		if (userId == null || userId.trim().length() == 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_550400,
					"잘못된 접근 : 사용자 아이디가 없습니다");
		}

		if (password == null || password.trim().length() == 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_550400,
					"잘못된 접근: 비밀 번호가 없습니다");
		}

		if (role > 2 || role <= 0) {
			throw new UserRunTimeException(StaticConfig.ERROR_CODE_550400,
					"잘못된 접근: role에 관한 정보가 잘못되었습니다");
		}

		Response<UserRes> res = new Response<UserRes>();
		UserRes userRes = userService.createUser(userReq, requestUserId);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(userRes);
		res.setCode(StaticConfig.SUCCESS_CODE_550);
		res.setMessage("사용자를 생성 하였습니다.");

		return res;
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public Response<UserInfoRes> getUsers(
			@PathVariable("userId") String userId,

			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		logger.debug("getUserInfo");

		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_551);

		UserInfoRes userInfoRes = userService.getUserInfo(userId);
		Response<UserInfoRes> response = new Response<UserInfoRes>();
		response.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		response.setData(userInfoRes);
		response.setCode(StaticConfig.SUCCESS_CODE_551);
		response.setMessage("유저정보를 조회하였습니다");

		return response;
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response updateUser(
			@PathVariable("userId") String userId,
			@RequestBody UserReq userReq,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		logger.debug("updateUser");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_552);
		userReq.setUserId(userId);
		userService.updateUser(userReq, requestUserId);
		Response response = new Response();
		response.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		response.setCode(StaticConfig.SUCCESS_CODE_552);
		response.setMessage("사용자 정보를 수정하였습니다");

		return response;
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response deleteUser(
			@PathVariable("userId") String userId,
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		logger.debug("deleteUser");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_553);

		userService.deleteUser(userId, requestUserId);
		Response response = new Response();
		response.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		response.setCode(StaticConfig.SUCCESS_CODE_553);
		response.setMessage("사용자를 삭제 하였습니다");
		return response;

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof UserRunTimeException) {
			HashMap<String, String> errMap = ((UserRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_559000);
			res.setMessage(e.getMessage());
		}
		e.printStackTrace();
		return res;
	}
}
