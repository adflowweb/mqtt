package kr.co.adflow.pms.core.controller;

import java.util.HashMap;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.GroupRunTimeException;
import kr.co.adflow.pms.core.request.GroupReq;
import kr.co.adflow.pms.core.response.GroupListRes;
import kr.co.adflow.pms.core.response.GroupRes;
import kr.co.adflow.pms.core.service.GroupService;
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
public class GroupController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	@Autowired
	private GroupService groupService;

	@Autowired
	private CheckUtil checkUtil;

	@RequestMapping(value = "/group", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<GroupRes> createGroup(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody GroupReq groupReq) throws Exception {

		logger.debug("/group(POST)==========");
		logger.debug("그룹생성");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_570);

		int gorupCode = groupReq.getGroupCode();
		String groupName = groupReq.getGroupName();
		String groupDescription = groupReq.getGroupDescription();

		if (groupName == null || groupName.trim().length() == 0) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_570400,
					"잘못된 접근 : groupName이 없습니다");
		}

		if (groupDescription == null || groupDescription.trim().length() == 0) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_570400,
					"잘못된 접근: groupDescription이 없습니다");
		}

		if (gorupCode == 0) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_570400,
					"잘못된 접근: groupCode에 관한 정보가 잘못되었습니다");
		}

		Response<GroupRes> res = new Response<GroupRes>();
		GroupRes groupRes = groupService.createGroup(groupReq);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(groupRes);
		res.setCode(StaticConfig.SUCCESS_CODE_570);
		res.setMessage("새로운 권한그룹를 생성 하였습니다.");

		return res;
	}

	@RequestMapping(value = "/group", method = RequestMethod.GET)
	@ResponseBody
	public Response<GroupListRes> getGroups(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		logger.debug("/group(GET)==========");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_571);
		Response<GroupListRes> res = new Response<GroupListRes>();
		GroupListRes groupListRes = groupService.getGroup();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(groupListRes);
		res.setCode(StaticConfig.SUCCESS_CODE_571);
		res.setMessage("그룹정보를  조회하였습니다.");

		return res;
	}

	@RequestMapping(value = "/group/{groupCode}", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<GroupRes> updateGroup(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@PathVariable int groupCode, @RequestBody GroupReq groupReq)
			throws Exception {

		logger.debug("/group/{groupCode}(PUT)==========");
		logger.debug("그룹수정");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_572);

		int updateGroupCode = groupReq.getGroupCode();

		if (updateGroupCode == 0) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_572400,
					"잘못된 접근: groupCode에 관한 정보가 잘못되었습니다");
		}

		Response res = new Response();
		int updateResult = groupService.updateGroup(groupCode, updateGroupCode);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);

		res.setCode(StaticConfig.SUCCESS_CODE_572);
		res.setMessage("그룹코드를 " + updateGroupCode + "번 으로 변경하였습니다.");

		return res;
	}

	@RequestMapping(value = "/group/{groupCode}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response<GroupRes> deleteGroup(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@PathVariable int groupCode) throws Exception {

		logger.debug("/group/{groupCode}(DELETE)==========");
		logger.debug("그룹수정");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_572);

		if (groupCode == 0) {
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_573400,
					"잘못된 접근: groupCode에 관한 정보가 잘못되었습니다");
		}

		Response res = new Response();
		int deleteResult = groupService.deleteGroup(groupCode);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);

		res.setCode(StaticConfig.SUCCESS_CODE_572);
		res.setMessage("그룹코드를 " + groupCode + "를 삭제하였습니다.");

		return res;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof GroupRunTimeException) {
			HashMap<String, String> errMap = ((GroupRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_579000);
			res.setMessage(e.getMessage());
		}
		e.printStackTrace();
		return res;
	}

}
