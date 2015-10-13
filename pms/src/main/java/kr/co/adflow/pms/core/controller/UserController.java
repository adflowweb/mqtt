package kr.co.adflow.pms.core.controller;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.request.UserReq;
import kr.co.adflow.pms.core.response.UserRes;
import kr.co.adflow.pms.core.service.UserService;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

	private final static String S_510_SUCCESS_CODE = "510200";

	@RequestMapping(value = "/users", method = RequestMethod.POST, params = "type=admin", consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<UserRes> createUser(@RequestBody UserReq user)
			throws Exception {

		logger.debug("createUser");
		Response<UserRes> res = new Response<UserRes>();
		String userId = userService.createUser(user);
		UserRes userRes = new UserRes();
		userRes.setUserId(userId);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(userRes);
		res.setCode(S_510_SUCCESS_CODE);
		res.setMessage("어드민 계정을 생성 하였습니다.");

		return res;
	}

	/*
	 * @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	 * 
	 * @ResponseBody public Response<Result<User>> getUsers(
	 * 
	 * @PathVariable("userId") String userId,
	 * 
	 * @RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) throws
	 * Exception {
	 * 
	 * User user = userService.retrieveUser(userId);
	 * 
	 * Result<User> result = new Result<User>(); result.setSuccess(true);
	 * result.setData(user);
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" }) Response<Result<User>> res
	 * = new Response(result); return res;
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT,
	 * consumes = StaticConfig.HEADER_CONTENT_TYPE, produces =
	 * StaticConfig.HEADER_CONTENT_TYPE)
	 * 
	 * @ResponseBody public Response<Result<HashMap<String, Integer>>>
	 * updateUser(
	 * 
	 * @PathVariable("userId") String userId,
	 * 
	 * @RequestBody UserUpdateReq user,
	 * 
	 * @RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) throws
	 * Exception {
	 * 
	 * logger.debug("updateUser");
	 * 
	 * // userId force setting user.setUserId(userId); int resultCnt =
	 * userService.updateUser(user, appKey);
	 * 
	 * Result<HashMap<String, Integer>> result = new Result<HashMap<String,
	 * Integer>>(); result.setSuccess(true); HashMap<String, Integer> hash = new
	 * HashMap<String, Integer>(); hash.put("updateCnt", resultCnt);
	 * 
	 * result.setData(hash);
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * Response<Result<HashMap<String, Integer>>> res = new Response(result);
	 * return res;
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE,
	 * consumes = StaticConfig.HEADER_CONTENT_TYPE, produces =
	 * StaticConfig.HEADER_CONTENT_TYPE)
	 * 
	 * @ResponseBody public Response<Result<HashMap<String, Integer>>>
	 * deleteUser(
	 * 
	 * @PathVariable("userId") String userId,
	 * 
	 * @RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) throws
	 * Exception {
	 * 
	 * int resultCnt = userService.deleteUser(userId, appKey);
	 * 
	 * Result<HashMap<String, Integer>> result = new Result<HashMap<String,
	 * Integer>>(); result.setSuccess(true); HashMap<String, Integer> hash = new
	 * HashMap<String, Integer>(); hash.put("deleteCnt", resultCnt);
	 * 
	 * result.setData(hash);
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * Response<Result<HashMap<String, Integer>>> res = new Response(result);
	 * return res;
	 * 
	 * }
	 */

}
