package kr.co.adflow.pms.core.controller;

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
}
