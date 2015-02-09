package kr.co.adflow.pms.inf.controller;

import java.util.HashMap;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.service.PCBSService;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PCBSController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PCBSController.class);

	@Autowired
	private PCBSService pcbsService;


	@RequestMapping(value = "/inf/service/users", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<HashMap<String, String>>> addUser(
			@RequestBody UserReq user
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey) throws Exception {

		logger.debug("addUser");

		String userId = pcbsService.addUser(user,appKey);

		Result<HashMap<String, String>> result = new Result<HashMap<String, String>>();
		result.setSuccess(true);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("userId", userId);

		result.setData(hash);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<HashMap<String, String>>> res = new Response(result);
		return res;

	}

}
