/*
 * 
 */
package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.Role;
import kr.co.adflow.push.service.RoleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class RoleController.
 *
 * @author nadir93
 * @date 2014. 7. 21.
 */
@Controller
public class RoleController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(RoleController.class);

	/** The role service. */
	@Resource
	private RoleService roleService;

	/**
	 * role 가져오기.
	 *
	 * @param roleID the role id
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "roles/{roleID}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Role[]> get(@PathVariable String roleID) throws Exception {
		logger.debug("roleID=" + roleID);
		Result<Role[]> result = new Result<Role[]>();
		result.setSuccess(true);
		Role[] roles = roleService.getByRole(roleID);
		if (roles == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("role not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(roles);
		}

		Response<Role[]> res = new Response<Role[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 모든 role 가져오기.
	 *
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "roles", method = RequestMethod.GET)
	@ResponseBody
	public Response<Role[]> get() throws Exception {
		Result<Role[]> result = new Result<Role[]>();
		result.setSuccess(true);
		Role[] roles = roleService.get();
		if (roles == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("roles not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(roles);
		}

		Response<Role[]> res = new Response<Role[]>(result);
		logger.debug("response=" + res);
		return res;
	}
}
