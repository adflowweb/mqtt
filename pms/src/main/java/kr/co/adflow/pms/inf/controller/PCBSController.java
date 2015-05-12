/*
 * 
 */
package kr.co.adflow.pms.inf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.inf.request.PCBSReq;
import kr.co.adflow.pms.inf.request.PasswordReq;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.request.UserUpdateReq;
import kr.co.adflow.pms.inf.service.PCBSService;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class PCBSController.
 */
@Controller
@RequestMapping(value = "/inf")
public class PCBSController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(PCBSController.class);

	/** The pcbs service. */
	@Autowired
	private PCBSService pcbsService;
	
	/** The intercept mapper. */
	@Autowired
	private InterceptMapper interceptMapper;

	/**
	 * Adds the user.
	 *
	 * @param user the user
	 * @return the response
	 * @throws Exception the exception
	 */
//	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "text/xml")
	@ResponseBody
	public String addUser (
			@RequestBody MultiValueMap<String,String> params)
			throws Exception {

		logger.debug("=== params={}","certKey::"+params.get("certKey")+",solutionId::"+params.get("solutionId")+",solutionPw::"+params.get("solutionPw")
				+",saId::"+params.get("saId")+",dSvcCd::"+params.get("dSvcCd")+",statusCd::"+params.get("statusCd")+",ptalk20Bunch::"+params.get("ptalk20Bunch")
				+",usegroupNm::"+params.get("usegroupNm"));
		
//		System.out.println("========= pCBSReq");
//		System.out.println("========= pCBSReq.getCertKey()::"+ params.get("certKey"));
//		
//		for (String key : params.keySet()) {
//			   System.out.println("key: " + key);
//			   System.out.println("value: " + params.get(key));
//			  }
//		
		boolean resultCk = false;
		
		try {
			
			List<String> tempList = params.get("certKey");

			String issueId = interceptMapper.selectCashedUserId(tempList.get(0));
			//appkey check
			if (issueId == null || issueId.trim().length() <= 0) {
				logger.error("applicationKey error is {}", tempList.get(0));
				throw new RuntimeException("CertKey not valid");
			}
			
			UserReq userReq = new UserReq();
			
			tempList = params.get("solutionId");
			userReq.setUserId(tempList.get(0));
			
			tempList = params.get("solutionPw");
			userReq.setPassword(tempList.get(0));
			
			tempList = params.get("saId");
			userReq.setSaId(tempList.get(0));
			
			tempList = params.get("dSvcCd");
			userReq.setUfmi(tempList.get(0));
			
			tempList = params.get("statusCd");
			if (tempList.get(0).equals("01")) {
				userReq.setStatus(1);
			} else {
				//상태코드 "01"아닌것은 모두 USER_STATUS_BROCK(2)처리 
				userReq.setStatus(2);
			}
			
			
			tempList = params.get("ptalk20Bunch");
			String ptalk20Bunch = tempList.get(0);
			
			tempList = params.get("usegroupNm");
			String usegroupNm = tempList.get(0);
			String ufmi = userReq.getUfmi();
			
			usegroupNm = "130,131,132";
			ufmi = "82*200*1111";
			ptalk20Bunch = "150";
			
			int firstIndex = ufmi.indexOf("*");
			int lastIndex = ufmi.lastIndexOf("*");
			List<String> groupTopics = new ArrayList<String>();
			String[] groupList = usegroupNm.split(",");
			StringBuffer grpupTopics = new StringBuffer();
			String bunchid = 

			ufmi.replace("*", "/");
			//group_toipc
			tempList = params.get("solutionPw");
			if (ufmi.substring(0, 2).equals("82")) {
				//Ptalk 1.0
				for (int i = 0; i < groupList.length; i++) {
					grpupTopics.append("mms/P1/"+ufmi.substring(firstIndex, lastIndex)+"/g"+groupList[i]);
					if (i + 1 < groupList.length) {
						grpupTopics.append(",");
					}
				}
				
			} else {
				//Ptalk 2.0
				for (int i = 0; i < groupList.length; i++) {
					grpupTopics.append("mms/P2/"+ufmi.substring(0, firstIndex)+"/b"+ptalk20Bunch+"/g"+groupList[i]);
					if (i + 1 < groupList.length) {
						grpupTopics.append(",");
					}
				}

			}
			userReq.setGroupTopics(grpupTopics.toString());
			
			System.out.println("userReq::"+userReq.toString());
			System.out.println("issueId::"+issueId);
			
			
			
			String userId = pcbsService.addUser(userReq, issueId);
			if (userId != null && userId.trim().length() > 0) {
				resultCk = true;
			}
			
		} catch (Exception e) {
			resultCk = false;
			e.printStackTrace();
		}


		String res = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> \n <boolen>" + resultCk + "</boolen>";
		
		return res;

	}

//	/**
//	 * Gets the users.
//	 *
//	 * @param userId the user id
//	 * @return the users
//	 */
//	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
//	@ResponseBody
//	public Response<Result<User>> getUsers(@PathVariable("userId") String userId) {
//
//		User user = pcbsService.retrieveUser(userId);
//
//		Result<User> result = new Result<User>();
//		result.setSuccess(true);
//		result.setData(user);
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Response<Result<User>> res = new Response(result);
//		return res;
//
//	}
//
//	/**
//	 * Modify password.
//	 *
//	 * @param req the req
//	 * @param appKey the app key
//	 * @return the response
//	 */
//	@RequestMapping(value = "/users/{userId}/sec", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
//	@ResponseBody
//	public Response<Result<List<String>>> modifyPassword(@RequestBody PasswordReq req,
//			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) {
//
//		int resultCnt = pcbsService.modifyPassword(req, appKey);
//
//		List<String> messages = new ArrayList<String>();
//		messages.add("modifyPassword SUCCESS :" + resultCnt);
//
//		Result<List<String>> result = new Result<List<String>>();
//		result.setSuccess(true);
//		result.setInfo(messages);
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Response<Result<List<String>>> res = new Response(result);
//		return res;
//
//	}
//
//	/**
//	 * Update user.
//	 *
//	 * @param userId the user id
//	 * @param user the user
//	 * @param appKey the app key
//	 * @return the response
//	 */
//	@RequestMapping(value = "/users/{userId}/msgCntLimit", method = RequestMethod.PUT, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
//	@ResponseBody
//	public Response<Result<HashMap<String, Integer>>> updateUser(
//			@PathVariable("userId") String userId,
//			@RequestBody UserUpdateReq user,
//			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) {
//
//		logger.debug("updateUser");
//
//		// userId force setting
//		user.setUserId(userId);
//		int resultCnt = pcbsService.updateUser(user, appKey);
//
//		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
//		result.setSuccess(true);
//		HashMap<String, Integer> hash = new HashMap<String, Integer>();
//		hash.put("updateCnt", resultCnt);
//
//		result.setData(hash);
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Response<Result<HashMap<String, Integer>>> res = new Response(result);
//		return res;
//
//	}
//
//	/**
//	 * Delete user.
//	 *
//	 * @param userId the user id
//	 * @param appKey the app key
//	 * @return the response
//	 */
//	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
//	@ResponseBody
//	public Response<Result<HashMap<String, Integer>>> deleteUser(
//			@PathVariable("userId") String userId,
//			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey) {
//
//		int resultCnt = pcbsService.deleteUser(userId, appKey);
//
//		Result<HashMap<String, Integer>> result = new Result<HashMap<String, Integer>>();
//		result.setSuccess(true);
//		HashMap<String, Integer> hash = new HashMap<String, Integer>();
//		hash.put("deleteCnt", resultCnt);
//
//		result.setData(hash);
//		@SuppressWarnings({ "unchecked", "rawtypes" })
//		Response<Result<HashMap<String, Integer>>> res = new Response(result);
//		return res;
//
//	}

}
