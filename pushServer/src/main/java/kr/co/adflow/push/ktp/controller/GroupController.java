package kr.co.adflow.push.ktp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import kr.co.adflow.push.ktp.service.GroupService;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.ktp.Affiliate;
import kr.co.adflow.push.domain.ktp.Department;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 6. 30.
 */
@Controller("bsBankGroupController")
public class GroupController {

	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	@Resource
	private GroupService groupService;

	/**
	 *  계열사정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/groups", method = RequestMethod.GET)
	@ResponseBody
	public Response<Affiliate[]> getAffiliate() throws Exception {
		Result<Affiliate[]> result = new Result<Affiliate[]>();
		result.setSuccess(true);
		Affiliate[] group = groupService.get();
		if (group == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("group not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(group);
		}
		Response<Affiliate[]> res = new Response<Affiliate[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 *  계열사부서정보 가져오기
	 * 
	 * @param userID
	 * @param clientID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/groups/{groupID:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Department[]> getDept(@PathVariable String groupID)
			throws Exception {
		logger.debug("userID=" + groupID);
		Result<Department[]> result = new Result<Department[]>();
		result.setSuccess(true);
		Department[] group = groupService.getDept(groupID);
		if (group == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("group not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(group);
		}

		Response<Department[]> res = new Response<Department[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 모든부서정보 가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bsbank/groups", params = "affiliate=all", method = RequestMethod.GET)
	@ResponseBody
	public Response<Department[]> getAllDept() throws Exception {
		Result<Department[]> result = new Result<Department[]>();
		result.setSuccess(true);
		Department[] group = groupService.getAllDept();
		if (group == null) {
			List<String> messages = new ArrayList<String>() {
				{
					add("group not found");
				}
			};
			result.setInfo(messages);
		} else {
			result.setData(group);
		}

		Response<Department[]> res = new Response<Department[]>(result);
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
