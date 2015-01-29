package kr.co.adflow.pms.inf.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;






import kr.co.adflow.pms.domain.mapper.TableMgtMapper;
import kr.co.adflow.pms.inf.request.UserReq;
import kr.co.adflow.pms.inf.service.PCBSService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PCBSController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(PCBSController.class);
	
	@Autowired
	private PCBSService pcbsService;
	
	@Autowired
	private TableMgtMapper tableMgtMapper;
	
	@RequestMapping(value = "/inf/service/users", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	public Response addUser(@RequestBody UserReq user) throws Exception {
		
		String userId = pcbsService.addUser(user);
		
		
		//tableMgtMapper.createMessage(user.getUserId());
		
		Result result = new Result();
		result.setSuccess(true);
		HashMap<String,String> hash = new HashMap<String,String>();
		hash.put("userId", userId);
		
		result.setData(hash);
		Response res = new Response(result);
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
