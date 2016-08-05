/*
 * 
 */
package kr.co.adflow.pms.users.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kr.co.adflow.pms.adm.service.AccountService;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.controller.BaseController;
import kr.co.adflow.pms.core.exception.PmsRuntimeException;
import kr.co.adflow.pms.domain.MessageResult;
import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.validator.UserValidator;
import kr.co.adflow.pms.response.Response;
import kr.co.adflow.pms.response.Result;
import kr.co.adflow.pms.users.request.MessageReq;
import kr.co.adflow.pms.users.service.UserMessageService;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// TODO: Auto-generated Javadoc
/**
 * The Class UserMessageController.
 */
@Controller
@RequestMapping(value = "/users")
public class UserMessageController extends BaseController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(UserMessageController.class);

	/** The push message service. */
	@Autowired
	private UserMessageService userMessageService;

	/** The account service. */
	@Autowired
	private AccountService accountService;

	/** The user validator. */
	@Autowired
	private UserValidator userValidator;
	

	/**
	 * Send message.
	 *
	 * @param appKey the app key
	 * @param msg the msg
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/message", method = RequestMethod.POST, consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<List<String>>> sendMessage(
			@RequestHeader(StaticConfig.HEADER_APPLICATION_KEY) String appKey,
			@RequestBody @Valid MessageReq msg) throws Exception {
		
		logger.debug("=== msg::{}","getContentType::"+msg.getContentType()+",getExpiry::"+msg.getExpiry()+",getQos::"+msg.getQos()+",getReceiver::"+msg.getReceiver()+",getSender::"+msg.getSender());

		if (msg.getSender() == null || msg.getSender().trim().length() == 0) {
//			throw new RuntimeException("Sender is empty.");
			throw new PmsRuntimeException("Sender is empty.");
		}
		if (msg.getReceiver() == null || msg.getReceiver().trim().length() == 0) {
//			throw new RuntimeException("Receiver is empty.");
			throw new PmsRuntimeException("Receiver is empty.");
		}
		if (msg.getContentType() == null || msg.getContentType().trim().length() == 0) {
//			throw new RuntimeException("ContentType is empty.");
			throw new PmsRuntimeException("ContentType is empty.");
		}
		if (msg.getContent() == null || msg.getContent().trim().length() == 0) {
//			throw new RuntimeException("Content is empty.");
			throw new PmsRuntimeException("Content is empty.");
		}


		int resultCnt = userMessageService.sendMessage (msg, appKey);


		List<String> messages = new ArrayList<String>();
		messages.add("receiver=" + msg.getReceiver());
		messages.add("content=" + msg.getContent());

		Result<List<String>> result = new Result<List<String>>();
		result.setSuccess(true);
		result.setInfo(messages);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<List<String>>> res = new Response(result);
		return res;

	}

	

	/**
	 * Valid phone no.
	 *
	 * @param phoneNo the phone no
	 * @return the response
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/groups/listCnt", method = RequestMethod.GET, params = "groupTopic", consumes = StaticConfig.HEADER_CONTENT_TYPE, produces = StaticConfig.HEADER_CONTENT_TYPE)
	@ResponseBody
	public Response<Result<Integer>> groupsListCnt(@RequestParam("groupTopic") String groupTopic) throws Exception {

		logger.debug("=== group Topic :{}", groupTopic);

		if (groupTopic == null || groupTopic.trim().length() == 0) {
			throw new RuntimeException("Group Topic is empty.");
		}

		Integer resultCnt = userMessageService.groupListCnt(groupTopic);

		Result<Integer> result = new Result<Integer>();
		result.setSuccess(true);

		result.setData(resultCnt);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Response<Result<Integer>> res = new Response(result);

		logger.debug("=== resultCnt :{}", resultCnt);
		return res;
	}

	/**
	 * Checks if is valid.
	 *
	 * @param receiver the receiver
	 * @return true, if is valid
	 */
	private boolean isValid(String receiver) {
		return userValidator.validRequestValue(receiver);
	}

}
