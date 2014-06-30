package kr.co.adflow.push.bsbank.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.service.UserService;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.bsbank.User;
import kr.co.adflow.push.service.TokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 6. 30.
 */
@Controller("bsBankUserController")
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Resource
	private UserService userService;

	@Resource
	private TokenService tokenService;

	/**
	 * 유저정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/users/{userID:.+}", method = RequestMethod.GET)
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
	 *  부서소속직원정보 가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/users", method = RequestMethod.GET)
	@ResponseBody
	public Response<User[]> getUsersByDepartment(
			@RequestParam(value = "dept", required = true) String id)
			throws Exception {
		logger.debug("dept=" + id);
		Result<User[]> result = new Result<User[]>();
		result.setSuccess(true);
		User[] users = userService.getUsersByDepartment(id);
		if (users == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("user not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(users);
		}

		Response<User[]> res = new Response<User[]>(result);
		logger.debug("response=" + res);
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
