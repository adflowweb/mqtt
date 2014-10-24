package kr.co.adflow.push.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.Response;
import kr.co.adflow.push.domain.Result;
import kr.co.adflow.push.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
// @Controller
abstract public class AbstractMessageController {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractMessageController.class);

	@Resource
	protected MessageService msgService;

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
		// 다중수신자처리

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

		Message msg = new Message();
		msg.setSender(map.get("sender"));
		msg.setReceiver(map.get("receiver"));
		// 다중수신자처리 요망
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

	/**
	 * 메시지 전송하기 테스트
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sendMsgTest", method = RequestMethod.GET)
	@ResponseBody
	public Response sendMsgTest() throws Exception {
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/kicho");
		String jsonString = "{\"notification\":{\"notificationStyle\":1,\"contentTitle\":\"교육장소공지\","
				+ "\"contentText\":\"메시지전송테스트.\", \"ticker\":\"부산은행교육장소알림장소: 수림연수원 시간: 3월 22일 오전: 12시\","
				+ "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: "
				+ (int) (Math.random() * 100)
				+ "시\", \"image\":\""
				+ "encodedStr"
				+ "\"},"
				+ "\"event\":{\"title\":\"부산은행교육\", \"location\":\"수림연수원\", \"desc\":\"\","
				+ "\"year\":\"2014\", \"month\":\"2\","
				+ "\"day\":\"22\"}"
				+ "}";
		msg.setContent(jsonString);
		msg.setQos(1);
		// msg.setSms(true);
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

	/**
	 * 메시지 전송하기 테스트
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sendSMSMsgTest", method = RequestMethod.GET)
	@ResponseBody
	public Response sendSMSMsgTest() throws Exception {
		Message msg = new Message();
		msg.setSender("nadir93");
		msg.setReceiver("/users/kicho");
		String jsonString = "{\"notification\":{\"notificationStyle\":1,\"contentTitle\":\"교육장소공지\","
				+ "\"contentText\":\"메시지전송테스트.\", \"ticker\":\"부산은행교육장소알림장소: 수림연수원 시간: 3월 22일 오전: 12시\","
				+ "\"summaryText\":\"장소: 수림연수원 시간: 3월 22일 오전: "
				+ (int) (Math.random() * 100)
				+ "시\", \"image\":\""
				+ "encodedStr"
				+ "\"},"
				+ "\"event\":{\"title\":\"부산은행교육\", \"location\":\"수림연수원\", \"desc\":\"\","
				+ "\"year\":\"2014\", \"month\":\"2\","
				+ "\"day\":\"22\"}"
				+ "}";
		msg.setContent(jsonString);
		msg.setQos(1);
		msg.setSms(true);
		msg.setTimeOut(3);
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

	/**
	 * 메시지 수정하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.PUT)
	@ResponseBody
	public Response put(@RequestBody Message msg) throws Exception {
		logger.debug("메시지=" + msg);
		final int count = msgService.put(msg);
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

	/**
	 * 메시지 삭제하기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages/{msgID}", method = RequestMethod.DELETE)
	@ResponseBody
	public Response delete(@PathVariable int msgID) throws Exception {
		logger.debug("메시지번호=" + msgID);
		final int count = msgService.delete(msgID);
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

	/**
	 * 메시지 가져오기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages/{msgID}", method = RequestMethod.GET)
	@ResponseBody
	public Response<Message> get(@PathVariable int msgID) throws Exception {
		logger.debug("msgID=" + msgID);
		Message msg = msgService.get(msgID);
		logger.debug("메시지=" + msg);
		Result<Message> result = new Result<Message>();
		result.setSuccess(true);
		result.setData(msg);
		Response<Message> res = new Response<Message>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 전체 메시지 가져오기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", method = RequestMethod.GET)
	@ResponseBody
	public Response<Message[]> getMsgs() throws Exception {
		Message[] msg = msgService.getMsgs();
		logger.debug("메시지=" + msg);
		Result<Message[]> result = new Result<Message[]>();
		result.setSuccess(true);
		result.setData(msg);
		Response<Message[]> res = new Response<Message[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 발송 메시지 가져오기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", params = "type=sent", method = RequestMethod.GET)
	@ResponseBody
	public Response<Message[]> getDeliveredMsgs(
	/* @RequestParam(value = "type", required = true) String type */)
			throws Exception {
		Result<Message[]> result = new Result<Message[]>();
		result.setSuccess(true);
		// if (type.equals("reservation")) {
		Message[] msg = msgService.getDeliveredMsgs();
		logger.debug("메시지=" + msg);
		result.setData(msg);
		// } else {
		// List<String> messages = new ArrayList<String>() {
		// {
		// add("type not found");
		// }
		// };
		// result.setErrors(messages);
		// }
		Response<Message[]> res = new Response<Message[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 예약 메시지 가져오기
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "messages", params = "type=reservation", method = RequestMethod.GET)
	@ResponseBody
	public Response<Message[]> getReservedMsgs(
	/* @RequestParam(value = "type", required = true) String type */)
			throws Exception {
		Result<Message[]> result = new Result<Message[]>();
		result.setSuccess(true);
		// if (type.equals("reservation")) {
		Message[] msg = msgService.getReservationMsgs();
		logger.debug("메시지=" + msg);
		result.setData(msg);
		// } else {
		// List<String> messages = new ArrayList<String>() {
		// {
		// add("type not found");
		// }
		// };
		// result.setErrors(messages);
		// }
		Response<Message[]> res = new Response<Message[]>(result);
		logger.debug("response=" + res);
		return res;
	}

	/**
	 * 예외처리
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Response handleAllException(final Exception e) {
		logger.error("예외발생", e);
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

	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}

	// /**
	// * @param msg
	// * @param topicName
	// * @return
	// */
	// @RequestMapping(value = "messages/groups/{groupID}", method =
	// RequestMethod.POST)
	// @ResponseBody
	// public Response sendGroupMessage(@RequestBody Message msg,
	// @PathVariable String groupID) throws Exception {
	// msg.setReceiver("groups/" + groupID);
	// logger.debug("그룹메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(messageService.post(msg));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }

	// /**
	// * 푸시서비스동작여부확인
	// *
	// * { result : { success: true, errors : [], - warnings : [], info : [],
	// data
	// * : { available: true, message : "" } } }
	// *
	// * @return
	// *
	// * @throws Exception
	// */
	// @RequestMapping(value = "/", method = RequestMethod.GET)
	// @ResponseBody
	// public Response isAvailable() throws Exception {
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(pushService.isAvailable());
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param topicName
	// * @return
	// */
	// @RequestMapping(value = "groups/{groupID}", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendGroupMessage(@RequestBody Message msg,
	// @PathVariable String groupID) throws Exception {
	// msg.setReceiver("groups/" + groupID);
	// logger.debug("그룹메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param userName
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value = "users/{userID}", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendPersonalMessage(@RequestBody Message msg,
	// @PathVariable String userID) throws Exception {
	// msg.setReceiver("users/" + userID);
	// logger.debug("개인메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
	//
	// /**
	// * @param msg
	// * @param userName
	// * @return
	// */
	// @RequestMapping(value = "users", method = RequestMethod.POST)
	// @ResponseBody
	// public Response sendMessageAll(@RequestBody Message msg) throws Exception
	// {
	// msg.setReceiver("users");
	// logger.debug("전체메시지=" + msg);
	// Result result = new Result();
	// result.setSuccess(true);
	// result.setData(new
	// SendMessageResponseData(pushService.sendMessage(msg)));
	// Response res = new Response(result);
	// logger.debug("response=" + res);
	// return res;
	// }
}
