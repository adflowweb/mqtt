package kr.co.adflow.pms.svc.controller;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageReq;
import kr.co.adflow.pms.svc.service.PushMessageService;

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
public class PushMessageController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageController.class);

	@Autowired
	private PushMessageService pushMessageService;

	@RequestMapping(value = "/svc/messages", method = RequestMethod.POST, consumes = PmsConfig.HEADER_CONTENT_TYPE, produces = PmsConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<String[]>> sendMessage(
			@RequestHeader(PmsConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody MessageReq msg) throws Exception {
		logger.debug("sendMessage");

		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
			throw new RuntimeException("getReceivers is null");
		}

		String[] msgIdArray = pushMessageService.sendMessage(appKey, msg);

		Result<String[]> result = new Result<String[]>();
		result.setSuccess(true);

		result.setData(msgIdArray);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String[]>> res = new Response(result);
		return res;

	}

}
