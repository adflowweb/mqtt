package kr.co.adflow.pms.adm.controller;

import java.util.List;

import kr.co.adflow.pms.adm.service.SystemService;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.domain.User;
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
public class SystemController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemController.class);
	
	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/adm/sys/users", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<List<User>>> getUsers() throws Exception {

		
		List<User> resultList = systemService.listAllUser();
		
		Result<List<User>> result = new Result<List<User>>();
		result.setSuccess(true);
		result.setData(resultList);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<User>>> res = new Response(result);
		return res;

	}
	
	@RequestMapping(value = "/adm/sys/loglevel/{level}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Result<String>> setLogger(@PathVariable("level") String level) throws Exception {

//		switch (level) {
//		case "debug":
//		org.apache.log4j.Logger.getRootLogger().setLevel(Level.DEBUG);
//			break;
//		case "info":
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.INFO);
//			break;
//		case "error":
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.ERROR);
//			break;
//		default:
//			org.apache.log4j.Logger.getRootLogger().setLevel(Level.FATAL);
//			break;
//		}
//		
//		String str = org.apache.log4j.Logger.getRootLogger().getLevel().toString();

		Result<String> result = new Result<String>();
		result.setSuccess(true);
		result.setData("");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<String>> res = new Response(result);
		return res;

	}
}
