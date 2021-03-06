package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.RoleService;
import kr.co.adflow.push.service.TokenService;
import kr.co.adflow.push.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@Resource
	private TokenService tokenService;

	@Resource
	private RoleService roleService;

	/**
	 * 유저정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userID:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<User> get(@PathVariable String userID) throws Exception {
		logger.debug("userID=" + userID);
		Result<User> result = new Result<User>();
		result.setSuccess(true);
		User user = userService.get(userID);
		if (user == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("user not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(user);
		}

		Response<User> res = new Response<User>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 어드민 유저정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users", params = "type=admin", method = RequestMethod.GET)
	@ResponseBody
	public Response<User[]> getAdmin() throws Exception {
		Result<User[]> result = new Result<User[]>();
		result.setSuccess(true);
		User[] user = userService.getAdmin();
		if (user == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("admin not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(user);
		}

		Response<User[]> res = new Response<User[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저정보 삭제하기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users/{userID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable String userID) throws Exception {
		logger.debug("userID=" + userID);
		final int count = userService.delete(userID);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저정보 입력
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response post(@RequestBody User user) throws Exception {
		logger.debug("유저=" + user);
		final int count = userService.post(user);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저정보 수정하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "users", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response put(@RequestBody User user) throws Exception {
		logger.debug("유저=" + user);
		final int count = userService.put(user);
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>() {
			{
				add("updates=" + count);
			}
		};
		result.setInfo(messages);
		Response res = new Response(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 유저인증
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response<Token> auth(@RequestBody User user,
			HttpServletRequest request,
			@RequestHeader("User-Agent") String userAgent) throws Exception {
		logger.debug("유저=" + user);
		Token token = userService.auth(user);
		Result<Token> result = new Result<Token>();
		result.setSuccess(true);
		if (token == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("user not found or invalid password");
				}
			};
			result.setErrors(messages);
		} else {
			token.setRole(roleService.getByUser(user.getUserID()));
			result.setData(token);
		}

		Response<Token> res = new Response<Token>(result);
		logger.debug("response=" + res);
		return res;
	}

	// /**
	// * 유저인증
	// *
	// * @param msg
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value = "adminAuth", method = RequestMethod.POST,
	// consumes = "application/json", produces = "application/json")
	// @ResponseBody
	// public Response<Token> adminAuth(@RequestBody User user,
	// HttpServletRequest request,
	// @RequestHeader("User-Agent") String userAgent) throws Exception {
	// logger.debug("유저=" + user);
	// Token token = userService.adminAuth(user);
	// Result<Token> result = new Result<Token>();
	// result.setSuccess(true);
	// if (token == null) {
	// List<String> messages = new ArrayList<String>() {
	// {
	// add("user not found or invalid password");
	// }
	// };
	// result.setErrors(messages);
	// } else {
	// result.setData(token);
	// }
	//
	// Response<Token> res = new Response<Token>(result);
	// logger.debug("response=" + res);
	// return res;
	// }

	/**
	 * 유저인증
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "auth", method = RequestMethod.GET)
	@ResponseBody
	public Response<Token> auth(HttpServletRequest request,
			@RequestParam(value = "userid") String userID,
			@RequestParam(value = "pw") String password,
			@RequestHeader("User-Agent") String userAgent) throws Exception {
		logger.debug("LDAP테스트시작");
		User user = new User();
		user.setUserID(userID);
		user.setPassword(password);
		Token token = userService.auth(user);
		Result<Token> result = new Result<Token>();
		if (token == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("user not found or invalid password");
				}
			};
			result.setErrors(messages);
		} else {
			result.setData(token);
		}

		Response<Token> res = new Response<Token>(result);
		logger.debug("response=" + res);
		logger.debug("LDAP테스트종료");
		return res;
	}

	/**
	 * 예외처리
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		logger.error("예외발생", e);
		Result result = new Result();
		result.setSuccess(false);
		List<String> messages = new ArrayList<String>() {
			{
				add(e.toString());
				// add(e.getMessage());
				// add("are u.");
			}
		};
		result.setErrors(messages);
		Response res = new Response(result);
		return res;
	}
}
