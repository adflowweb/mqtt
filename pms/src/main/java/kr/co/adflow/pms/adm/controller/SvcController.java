package kr.co.adflow.pms.adm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.adflow.pms.adm.response.MessagesRes;
import kr.co.adflow.pms.adm.service.SvcService;
import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;

@Controller
public class SvcController extends BaseController {
	
	@Autowired
	private SvcService svcService;
	
	
	@RequestMapping(value = "/adm/svc/messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<MessagesRes>> getMessageList(@RequestParam Map<String,String> params
			,@RequestHeader(PmsConfig.HEADER_APPLICATION_TOKEN) String appKey) throws Exception {

		String sEcho = (String) params.get("sEcho");
		params.put("appKey", appKey);
		
		MessagesRes messagesRes = svcService.getSvcMessageList(params);
		
		messagesRes.setsEcho(sEcho);
		
		
		Result<MessagesRes> result = new Result<MessagesRes>();
		result.setSuccess(true);
		result.setData(messagesRes);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<MessagesRes>> res = new Response(result);
		return res;

	}

}
