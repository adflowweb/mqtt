package kr.co.adflow.pms.core.controller;

import java.util.HashMap;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.MonitorRunTimeException;
import kr.co.adflow.pms.core.service.ServerInfoService;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.domain.ServerInfo;
import kr.co.adflow.pms.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MonitorController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MonitorController.class);

	@Autowired
	ServerInfoService serverInfoService;

	@Autowired
	CheckUtil checkUtil;

	@RequestMapping(value = "/server", method = RequestMethod.GET)
	@ResponseBody
	public Response<ServerInfo> getServerInfo(
			@RequestHeader(value = StaticConfig.HEADER_APPLICATION_KEY) String applicationKey)
			throws Exception {
		logger.debug("/server(GET)==========");
		String requestUserId = checkUtil.checkAuth(applicationKey,
				StaticConfig.API_CODE_590);

		ServerInfo serverInfo = serverInfoService.getServerInfo();

		Response<ServerInfo> res = new Response<ServerInfo>();
		res.setStatus(StaticConfig.RESPONSE_STATUS_OK);
		res.setData(serverInfo);
		res.setCode(StaticConfig.SUCCESS_CODE_590);
		res.setMessage("현재서버의 정보를 조회하였습니다");

		return res;

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		Response res = new Response();
		res.setStatus(StaticConfig.RESPONSE_STATUS_FAIL);
		if (e instanceof MonitorRunTimeException) {
			HashMap<String, String> errMap = ((MonitorRunTimeException) e)
					.getErrorMsg();
			String errCode = errMap.get("errCode");
			String errMsg = errMap.get("errMsg");
			logger.error(errCode);
			logger.error(errMsg);
			res.setCode(errCode);
			res.setMessage(errMsg);
		} else {
			res.setCode(StaticConfig.ERROR_CODE_599000);
			res.setMessage(e.getMessage());
		}
		e.printStackTrace();
		return res;
	}

}
