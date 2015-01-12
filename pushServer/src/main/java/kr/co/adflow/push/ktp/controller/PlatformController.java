package kr.co.adflow.push.ktp.controller;

import java.util.ArrayList;
import java.util.List;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.domain.ktp.request.DigInfo;
import kr.co.adflow.push.domain.ktp.request.FwInfo;
import kr.co.adflow.push.domain.ktp.request.KeepAliveTime;
import kr.co.adflow.push.domain.ktp.request.Precheck;
import kr.co.adflow.push.ktp.service.PlatformService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class PlatformController {

	private static final Logger logger = LoggerFactory
			.getLogger(PlatformController.class);
	
	
	@Autowired
	private PlatformService platformService;

	@RequestMapping(value = "precheck", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public void sendPrecheck(@RequestBody Precheck precheck) throws Exception {
		platformService.sendPrecheck(precheck.getReceiver());
		
	}
	
	
	@RequestMapping(value = "devices/fwInfo", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyFwInfo(@RequestBody FwInfo fwInfo) throws Exception {
		
		
		platformService.modifyFwInfo(fwInfo);
		
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
			
//		messages.add("sender" + fwInfo.getSender());
		messages.add("receiver=" + fwInfo.getReceiver());
		messages.add("content=" + fwInfo.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	@RequestMapping(value = "devices/keepAliveTime", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyKeepAliveTime(@RequestBody KeepAliveTime keepAliveTime) throws Exception {
		
		platformService.modifyKeepAliveTime(keepAliveTime);
		
		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		
		messages.add("receiver=" + keepAliveTime.getReceiver());			
		messages.add("content=" + keepAliveTime.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	
	
	@RequestMapping(value = "users/digInfo", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response modifyDigInfo(@RequestBody DigInfo digInfo) throws Exception {
		
		
		platformService.modifyDigInfo(digInfo);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + digInfo.getReceiver());
		messages.add("content=" + digInfo.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	
	@RequestMapping(value = "admin/message", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response sendMessage(@RequestBody Message message) throws Exception {
		
		
		platformService.sendMessage(message);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + message.getReceiver());	
		messages.add("content=" + message.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	@RequestMapping(value = "users/message", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response sendUserMessage(@RequestBody Message message) throws Exception {
		
		
		platformService.sendMessage(message);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
			
		messages.add("receiver=" + message.getReceiver());
		messages.add("content=" + message.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	@RequestMapping(value = "users/userid/validation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response validUserID(@RequestBody Message message) throws Exception {
		
		
		platformService.sendMessage(message);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
			
		messages.add("receiver=" + message.getReceiver());
		messages.add("content=" + message.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}
	
	@RequestMapping(value = "users/ufmi/validation", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response validUFMI(@RequestBody Message message) throws Exception {
		
		
		platformService.sendMessage(message);

		Result result = new Result();
		result.setSuccess(true);
		List<String> messages = new ArrayList<String>();
			
		messages.add("receiver=" + message.getReceiver());
		messages.add("content=" + message.getContent());
			
		result.setInfo(messages);
		Response res = new Response(result);
		logger.info("response=" + res);
		return res;
	}



}
