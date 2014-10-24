package kr.co.adflow.push.bsbank.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.co.adflow.push.controller.AbstractMessageController;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * 
 */
@Controller("bsBankMessageController")
public class MessageController extends AbstractMessageController {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageController.class);

	/**
	 * 메시지 전송하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.POST, consumes = "text/plain")
	@ResponseBody
	public Response post(@RequestBody String requestBody) throws Exception {
		logger.debug("메시지=" + requestBody);
		// message parsing
		String msgStr = requestBody.substring(522);
		logger.debug("msgStr=" + msgStr);

		Map<String, String> map = getQueryMap(msgStr);
		Set<String> keys = map.keySet();
		for (String key : keys) {
			logger.debug("Name=" + key);
			logger.debug("Value=" + map.get(key));
		}

		// sender, receiver, sms, timeOut, qos, retained, reservation,
		// type, content, status, category

		// 다중수신자처리 요망

		if (!map.containsKey("receiver")) {
			throw new Exception("receiver not found");
		}

		String[] receivers = map.get("receiver").split(",");
		logger.debug("receivers=" + receivers.length);

		Message msg = new Message();

		if (map.containsKey("sender")) {
			msg.setSender(map.get("sender"));
		} else {
			throw new Exception("sender not found");
		}

		msg.setSms(Boolean.parseBoolean(map.get("sms")));

		if (map.containsKey("timeOut")) {
			msg.setTimeOut(Integer.parseInt(map.get("timeOut")));
		}

		if (map.containsKey("reservation")) {
			msg.setReservation(new Date(map.get("reservation")));
		}

		if (map.containsKey("type")) {
			msg.setType(Integer.parseInt(map.get("type")));
		}

		msg.setContent(map.get("content"));
		msg.setCategory(map.get("category"));

		if (receivers.length > 1) {
			// 다중수신자처리
			final int count;
			int cnt = 0;
			for (int i = 0; i < receivers.length; i++) {
				if (receivers[i] != null && !receivers[i].equals("")) {
					msg.setReceiver(receivers[i]);
					// 계열사 메시지 type = 2
					// 부서 메시지 type = 3
					if (receivers[i].startsWith("/users")) {
						msg.setType(0);
					} else if (receivers[i].startsWith("/groups")) {
						// group message
						logger.debug("length=" + receivers[i].split("/").length);
						if (receivers[i].split("/").length == 3) {
							logger.debug("계열사메시지입니다.");
							msg.setType(2);
						} else if (receivers[i].split("/").length == 4) {
							logger.debug("부서메시지입니다.");
							msg.setType(3);
						}
					}
					cnt += msgService.post(msg);
				}
			}
			count = cnt;
			Result result = new Result();
			result.setSuccess(true);
			List<String> messages = new ArrayList<String>() {
				{
					add("updates=" + count);
				}
			};
			result.setInfo(messages);
			Response res = new Response(result);
			logger.debug("response=" + res);
			return res;
		} else {
			final int count = msgService.post(msg);
			Result result = new Result();
			result.setSuccess(true);
			List<String> messages = new ArrayList<String>() {
				{
					add("updates=" + count);
				}
			};
			result.setInfo(messages);
			Response res = new Response(result);
			logger.debug("response=" + res);
			return res;
		}
	}

	/**
	 * 메시지 전송하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Response post(@RequestBody Message msg) throws Exception {
		logger.debug("메시지=" + msg);

		String[] receivers = msg.getReceiver().split(",");
		logger.debug("receivers=" + receivers.length);

		if (receivers.length > 1) {
			// 다중수신자처리
			final int count;
			int cnt = 0;
			for (int i = 0; i < receivers.length; i++) {
				if (receivers[i] != null && !receivers[i].equals("")) {
					msg.setReceiver(receivers[i]);
					// 계열사 메시지 type = 2
					// 부서 메시지 type = 3
					if (receivers[i].startsWith("/users")) {
						msg.setType(0);
					} else if (receivers[i].startsWith("/groups")) {
						// group message
						logger.debug("length=" + receivers[i].split("/").length);
						if (receivers[i].split("/").length == 3) {
							logger.debug("계열사메시지입니다.");
							msg.setType(2);
						} else if (receivers[i].split("/").length == 4) {
							logger.debug("부서메시지입니다.");
							msg.setType(3);
						}
					}

					cnt += msgService.post(msg);
				}
			}
			count = cnt;
			Result result = new Result();
			result.setSuccess(true);
			List<String> messages = new ArrayList<String>() {
				{
					add("updates=" + count);
				}
			};
			result.setInfo(messages);
			Response res = new Response(result);
			logger.debug("response=" + res);
			return res;
		} else {
			final int count = msgService.post(msg);
			Result result = new Result();
			result.setSuccess(true);
			List<String> messages = new ArrayList<String>() {
				{
					add("updates=" + count);
				}
			};
			result.setInfo(messages);
			Response res = new Response(result);
			logger.debug("response=" + res);
			return res;
		}

	}
}
