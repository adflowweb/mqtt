package kr.co.adflow.pms.svc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;







import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.svc.request.MessageReq;
import kr.co.adflow.pms.svc.service.PushMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PushMessageController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(PushMessageController.class);
	
	@Autowired
	private PushMessageService pushMessageService;
	
	
	@RequestMapping(value = "/svc/messages", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response sendMessage(@RequestHeader("x-application-key") String appKey,@RequestBody MessageReq msg) throws Exception {
		
		if (msg.getReceivers() == null || msg.getReceivers().length == 0) {
			//
			throw new RuntimeException("getReceivers is null");
		}
		
		
		String[] msgIdArray = pushMessageService.sendMessage(appKey,msg);
		
		Result<String[]> result = new Result<String[]>();
		result.setSuccess(true);
		
		result.setData(msgIdArray);
		Response<Result<String[]>> res = new Response(result);
		return res;
		
	}
	
	
	/**
	 * 예외처리.
	 *
	 * @param e the e
	 * @return the response
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		
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
