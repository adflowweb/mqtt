package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.response.PCFRes;
import kr.co.adflow.pms.core.service.PCFService;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PCFController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PCFController.class);
	@Autowired
	PCFService pcfService;

	private final static String S_503_SUCCESS_CODE = "503200";
	private final static String S_505_SUCCESS_CODE = "505200";
	private final static String S_505_NOT_FOUND_CODE = "505404";

	@RequestMapping(value = "/mqtt/{token}/status", method = RequestMethod.GET)
	@ResponseBody
	public Response<PCFRes> getTokenStatus(@PathVariable String token)
			throws Exception {
		logger.debug("token:" + token);
		String tokenStatus = pcfService.getStatus(token);
		Response<PCFRes> res = new Response<PCFRes>();
		PCFRes pcfRes = new PCFRes();
		pcfRes.setMQTTStatus(tokenStatus);
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(pcfRes);
		res.setCode(S_503_SUCCESS_CODE);
		res.setMessage("MQTT 연결 상태를 체크 하였습니다");

		logger.debug("response=" + res);

		return res;
	}

	@RequestMapping(value = "/topic/subscriptions/{token:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<String[]> getTopic(@PathVariable String token)
			throws Exception {
		logger.debug("token=" + token);

		Response<String[]> res = new Response<String[]>();
		String[] resultTopics = pcfService.getTopics(token);
		if (resultTopics == null) {
			logger.info("topics:", resultTopics);
			res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
			res.setCode(S_505_NOT_FOUND_CODE);
			res.setMessage("Subscriptions 목록을 찾을 수 없습니다");
			res.setExplaination("해당 토큰의 Subscriptions 목록을 조회 하였으나 그 값이 없습니다");

		} else {
			res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
			res.setData(resultTopics);
			res.setCode(S_505_SUCCESS_CODE);
			res.setMessage("Subscriptions 목록을 조회 하였습니다");

		}

		logger.debug("response:" + res);
		return res;
	}
}
