package kr.co.adflow.pms.core.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.pms.adm.response.PCFRes;
import kr.co.adflow.pms.core.service.PCFService;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

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

	@RequestMapping(value = "/mqtt/{token}/status", method = RequestMethod.GET)
	@ResponseBody
	public Response<PCFRes> getTokenStatus(@PathVariable String token)
			throws Exception {
		logger.debug("token:" + token);
		String tokenStatus = pcfService.getStatus(token);
		PCFRes pcfRes = new PCFRes();
		pcfRes.setStatus(tokenStatus);

		Result<PCFRes> result = new Result<PCFRes>();
		result.setSuccess(true);
		result.setData(pcfRes);
		Response<PCFRes> res = new Response<PCFRes>(result);
		logger.debug("response=" + res);

		return res;
	}

	@RequestMapping(value = "/topic/subscriptions/{token:.+}", method = RequestMethod.GET)
	@ResponseBody
	public Response<String[]> getTopic(@PathVariable String token)
			throws Exception {
		logger.debug("token=" + token);
		Result<String[]> result = new Result<String[]>();
		result.setSuccess(true);
		String[] resultTopics = pcfService.getTopics(token);
		if (resultTopics == null) {
			List<String> messageList = new ArrayList<String>() {
				{
					add("topic not found");
				}

			};
			logger.info("topics:", resultTopics);
			resultTopics = new String[0];
			result.setData(resultTopics);
			result.setInfo(messageList);
		} else {
			result.setData(resultTopics);
		}
		Response<String[]> res = new Response<String[]>(result);
		logger.debug("response:" + res);
		return res;
	}
}
