package kr.co.adflow.pms.core.controller;

import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.GroupRunTimeException;
import kr.co.adflow.pms.core.exception.RoleRunTimeException;
import kr.co.adflow.pms.core.request.RoleReq;
import kr.co.adflow.pms.core.response.RoleListRes;
import kr.co.adflow.pms.core.response.RoleRes;
import kr.co.adflow.pms.core.service.RoleService;
import kr.co.adflow.pms.domain.Token;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
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

@Controller
public class RoleController extends BaseController {

	// /** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(RoleController.class);

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private InterceptMapper interceptMapper;

	@RequestMapping(value = "/role", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<RoleRes> createRole(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@RequestBody RoleReq roleReq) throws Exception {

		logger.debug("createRole");
		logger.debug("권한크도 생성");
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		String requsetUserId = interceptMapper
				.selectCashedUserId(applicationKey);

		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_580)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_580 + "에 대한 권한이 없습니다");
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_580401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");

		int reqApiCode = roleReq.getApiCode();
		int reqGroupCode = roleReq.getGroupCode();
		String reqApiMethod = roleReq.getApiMethod();
		String reqApiName = roleReq.getApiName();
		String reqAPiUrl = roleReq.getApiUrl();

		if (reqApiCode == 0) {
			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_580400,
					"잘못된 접근 : apiCode 가 잘못되었습니다.");
		}

		if (reqGroupCode == 0) {
			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_580400,
					"잘못된 접근: groupCode가 잘못되었습니다.");
		}

		if (reqApiMethod == null || reqApiMethod.trim().length() == 0) {

			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_580404,
					"apiMethod 가 없습니다");
		}

		if (reqApiName == null || reqApiName.trim().length() == 0) {

			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_580404,
					"apiName이 없습니다");
		}

		if (reqAPiUrl == null || reqAPiUrl.trim().length() == 0) {

			throw new RoleRunTimeException(StaticConfig.ERROR_CODE_580404,
					"apiurl이 없습니다");
		}

		Response<RoleRes> res = new Response<RoleRes>();
		RoleRes roleRes = roleService.createRole(roleReq, requsetUserId);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(roleRes);
		res.setCode(StaticConfig.SUCCESS_CODE_580);
		res.setMessage("새로운 권한코드를 생성 하였습니다.");

		return res;

	}

	@RequestMapping(value = "/role", method = RequestMethod.GET)
	@ResponseBody
	public Response<RoleListRes> getRole(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {

		logger.debug("getRole");
		logger.debug("권한크도 조회");
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		String requsetUserId = interceptMapper
				.selectCashedUserId(applicationKey);

		List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < apiCode.size(); i++) {

			if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_581)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_581 + "에 대한 권한이 없습니다");
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_581401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");

		Response<RoleListRes> res = new Response<RoleListRes>();
		RoleListRes roleListRes = roleService.getRole();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(roleListRes);
		res.setCode(StaticConfig.SUCCESS_CODE_581);
		res.setMessage("권한정보를 조회하였습니다.");

		return res;

	}

	@RequestMapping(value = "/role/{groupCode}/{apiCode}", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response updateRole(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@PathVariable int groupCode, @PathVariable int apiCode,
			@RequestBody RoleReq roleReq) throws Exception {

		logger.debug("updateRole");
		logger.debug("권한크도수정");
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		String requsetUserId = interceptMapper
				.selectCashedUserId(applicationKey);

		List<Token> getApiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < getApiCode.size(); i++) {

			if (getApiCode.get(i).getApiCode()
					.equals(StaticConfig.API_CODE_582)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_582 + "에 대한 권한이 없습니다");
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_582401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");

		Response res = new Response();
		int updateResult = roleService.updateRole(groupCode, apiCode,
				roleReq.getApiCode());
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setCode(StaticConfig.SUCCESS_CODE_582);
		res.setMessage(groupCode + "그룹의 " + apiCode + "코드를 "
				+ roleReq.getApiCode() + "로수정하였습니다.");

		return res;

	}

	@RequestMapping(value = "/role/{groupCode}/{apiCode}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response deleteRole(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey,
			@PathVariable int groupCode, @PathVariable int apiCode)
			throws Exception {

		logger.debug("deleteRole");
		logger.debug("권한크도삭제");
		logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
		String requsetUserId = interceptMapper
				.selectCashedUserId(applicationKey);

		List<Token> getApiCode = tokenMapper.getApiCode(requsetUserId);
		boolean tokenAuthCheck = false;
		for (int i = 0; i < getApiCode.size(); i++) {

			if (getApiCode.get(i).getApiCode()
					.equals(StaticConfig.API_CODE_583)) {
				tokenAuthCheck = true;
			}
		}
		if (tokenAuthCheck == false) {
			logger.debug(StaticConfig.API_CODE_583 + "에 대한 권한이 없습니다");
			throw new GroupRunTimeException(StaticConfig.ERROR_CODE_583401,
					"권한이 없습니다");
		}
		logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");

		Response res = new Response();
		int deleteResult = roleService.deleteRole(groupCode, apiCode);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setCode(StaticConfig.SUCCESS_CODE_583);
		res.setMessage(groupCode + "그룹의 " + apiCode + "코드를 삭제하였습니다.");

		return res;

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof RoleRunTimeException) {
			HashMap<String, String> errMap = ((RoleRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_589000);
			res.setMessage(e.getMessage());
		}
		e.printStackTrace();
		return res;
	}

	//
	// @RequestMapping(value = "/group/{groupCode}", method = RequestMethod.PUT,
	// consumes = StaticConfig.HEADER_CONTENT_TYPE, produces =
	// StaticConfig.HEADER_CONTENT_TYPE)
	// @ResponseBody
	// public Response<GroupRes> updateGroup(
	// @RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String
	// applicationKey,
	// @PathVariable int groupCode, @RequestBody GroupReq groupReq)
	// throws Exception {
	//
	// logger.debug("updateGroup");
	// logger.debug("그룹수정");
	// logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
	// Token tokenId = tokenMapper.selectUserid(applicationKey);
	// String requsetUserId = tokenId.getUserId();
	//
	// List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
	// boolean tokenAuthCheck = false;
	// for (int i = 0; i < apiCode.size(); i++) {
	//
	// if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_572)) {
	// tokenAuthCheck = true;
	// }
	// }
	// if (tokenAuthCheck == false) {
	// logger.debug(StaticConfig.API_CODE_572 + "에 대한 권한이 없습니다");
	// throw new GroupRunTimeException(StaticConfig.ERROR_CODE_572401,
	// "권한이 없습니다");
	// }
	// logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");
	//
	// int updateGroupCode = groupReq.getGroupCode();
	//
	// if (updateGroupCode == 0) {
	// throw new GroupRunTimeException(StaticConfig.ERROR_CODE_572400,
	// "잘못된 접근: groupCode에 관한 정보가 잘못되었습니다");
	// }
	//
	// Response res = new Response();
	// int updateResult = groupService.updateGroup(groupCode, updateGroupCode);
	// res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
	//
	// res.setCode(StaticConfig.SUCCESS_CODE_572);
	// res.setMessage("그룹코드를 " + updateGroupCode + "번 으로 변경하였습니다.");
	//
	// return res;
	// }
	//
	// @RequestMapping(value = "/group/{groupCode}", method =
	// RequestMethod.DELETE)
	// @ResponseBody
	// public Response<GroupRes> deleteGroup(
	// @RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String
	// applicationKey,
	// @PathVariable int groupCode) throws Exception {
	//
	// logger.debug("deleteGroup");
	// logger.debug("그룹수정");
	// logger.debug(applicationKey + "의 권한 체크를 시작합니다!");
	// Token tokenId = tokenMapper.selectUserid(applicationKey);
	// String requsetUserId = tokenId.getUserId();
	//
	// List<Token> apiCode = tokenMapper.getApiCode(requsetUserId);
	// boolean tokenAuthCheck = false;
	// for (int i = 0; i < apiCode.size(); i++) {
	//
	// if (apiCode.get(i).getApiCode().equals(StaticConfig.API_CODE_573)) {
	// tokenAuthCheck = true;
	// }
	// }
	// if (tokenAuthCheck == false) {
	// logger.debug(StaticConfig.API_CODE_573 + "에 대한 권한이 없습니다");
	// throw new GroupRunTimeException(StaticConfig.ERROR_CODE_573401,
	// "권한이 없습니다");
	// }
	// logger.debug(applicationKey + "에 대한 권한체크가 완료되었습니다.");
	//
	//
	//
	// if (groupCode == 0) {
	// throw new GroupRunTimeException(StaticConfig.ERROR_CODE_573400,
	// "잘못된 접근: groupCode에 관한 정보가 잘못되었습니다");
	// }
	//
	// Response res = new Response();
	// int deleteResult = groupService.deleteGroup(groupCode);
	// res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
	//
	// res.setCode(StaticConfig.SUCCESS_CODE_572);
	// res.setMessage("그룹코드를 " + groupCode + "를 삭제하였습니다.");
	//
	// return res;
	// }

}