/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.request.ReservationCancelReq;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
//import kr.co.adflow.pms.core.handler.PCFConnectionManager;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.SummaryMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class UserMessageServiceImpl.
 */
@Service
public class MessageServiceImpl implements MessageService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private InterceptMapper interceptMapper;

	@Autowired
	private SummaryMapper summaryMapper;

	/** The push Mapper. */
	// @Autowired
	// private PushMapper pushMapper;

	/** The check util. */
	@Autowired
	private CheckUtil checkUtil;

	/** The pms config. */
	@Autowired
	private PmsConfig pmsConfig;

	/** The jms template. */
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public MessagesListRes getMessageList(Map<String, String> params)
			throws Exception {

		MessagesListRes messageListRes = null;
		MsgParams msgParams = new MsgParams();
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		msgParams.setStatusArray(this.getStringArray(params
				.get("cSearchStatus")));

		String filter = params.get("cSearchFilter");
		msgParams.setAckType(-1);
		if ("receiver".equals(filter)) {
			msgParams.setReceiver(params.get("cSearchFilterContent"));
		} else if ("msgId".equals(filter)) {
			msgParams.setMsgId(params.get("cSearchFilterContent"));
		} else if ("issueId".equals(filter)) {
			msgParams.setIssueId(params.get("cSearchFilterContent"));
		} else if ("msgType".equals(filter)) {
			msgParams
					.setMsgType(this.getInt(params.get("cSearchFilterContent")));
		}

		int cnt = messageMapper.getMessageListCnt(msgParams);
		logger.info("cnt :::::::{}", cnt);

		List<Message> list = messageMapper.getMessageList(msgParams);
		logger.info("list size :::::::{}", list.size());

		messageListRes = new MessagesListRes();
		messageListRes.setRecordsFiltered(cnt);
		messageListRes.setRecordsTotal(cnt);
		messageListRes.setData(list);

		return messageListRes;
	}

	@Override
	public StatisticsRes getstatistics(Map<String, String> params) {
		// TODO Auto-generated method stub
		StatisticsRes statisticsRes = null;
		MsgParams msgParams = new MsgParams();
		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));

		Map<String, String> summaryResult = summaryMapper
				.getMessageStatistics(msgParams);
		statisticsRes = new StatisticsRes();
		statisticsRes.setMsgTotalCnt(this.getInt(String.valueOf(summaryResult
				.get("msgCnt"))));
		statisticsRes.setMsgSuccessCnt(this.getInt(String.valueOf(summaryResult
				.get("msgSuccessCnt"))));
		statisticsRes.setMsgFailCnt(this.getInt(String.valueOf(summaryResult
				.get("msgFailCnt"))));
		statisticsRes.setPmaAckCnt(this.getInt(String.valueOf(summaryResult
				.get("pmaAckCnt"))));
		statisticsRes.setAppAckCnt(this.getInt(String.valueOf(summaryResult
				.get("appAckCnt"))));

		return statisticsRes;
	}

	@Override
	public Message sendMessage(MessageReq message, String appKey)
			throws Exception {

		logger.debug("=== message::{}",
				"getContentType::" + message.getContentType() + ",getExpiry::"
						+ message.getExpiry() + ",getQos::" + message.getQos()
						+ ",getReceiver::" + message.getReceiver());
		int resultCnt = 0;

		String msgId = null;

		String issueId = interceptMapper.selectCashedUserId(appKey);
		logger.debug("메시지 생서 아이디");
		logger.debug(issueId);

		Message msg = new Message();

		msg.setServerId(pmsConfig.EXECUTOR_SERVER_ID);

		if (message.getExpiry() == 0) {
			logger.debug("expiry 시간이 입력되지 않아 기본 시간으로 세팅툅니다!"
					+ pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);

			msg.setExpiry(pmsConfig.MESSAGE_HEADER_EXPIRY_DEFAULT);
		} else {
			logger.debug("expiry 시간이 입력되었습니다!" + message.getExpiry());
			long expiryTime = message.getExpiry() * 1000;
			msg.setExpiry(expiryTime);
		}

		msg.setQos(message.getQos());
		msg.setIssueId(issueId);

		msg.setServiceId(pmsConfig.MESSAGE_SERVICE_ID_DEFAULT);
		// message.service.id.adflow=kr.co.adflow.push.message
		// Ack false
		msg.setAck(true);
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		msgId = this.getMsgId();
		msg.setMsgId(msgId);

		msg.setMsgType(100);

		// sendJMS
		msg.setReceiver(message.getReceiver());

		msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);
		this.sendJMS(msg);

		// resultCnt++;

		return msg;
	}

	// JMS Send and Message DB insert
	public void sendJMS(Message msg) {
		logger.debug(msg.toString());

		// JMS message send

		jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate,
				msg));

		// DB message insert
		messageMapper.insertMessage(msg);

	}

	/**
	 * Gets the msg id.
	 * 
	 * @return the msg id
	 */
	private String getMsgId() {
		return KeyGenerator.generateMsgId();
	}

	private int getInt(String string) {
		return Integer.parseInt(string);
	}

	private Date getDate(String string) {
		return DateUtil.fromISODateString(string);
	}

	private String[] getStringArray(String string) {
		if ("ALL".equals(string)) {
			return null;
		}
		return string.split(",");
	}

	// @Override
	// public MessagesListRes getResevationMessageList(Map<String, String>
	// params)
	// throws Exception {
	//
	// MessagesListRes res = null;
	//
	// String issueId = interceptMapper.selectCashedUserId(params
	// .get("appKey"));
	//
	// MsgParams msgParams = new MsgParams();
	//
	// // 관제 메세지 only msg_type : 10
	// // msgParams.setMsgType(10);
	//
	// // msgParams.setKeyMon(params.get("cSearchDate"));
	// msgParams.setIssueId(issueId);
	//
	// msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
	// msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));
	//
	// msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
	// msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
	//
	// String filter = params.get("cSearchFilter");
	// msgParams.setAckType(-1);
	// if ("receiver".equals(filter)) {
	// msgParams.setReceiver(params.get("cSearchContent"));
	// } else if ("msgId".equals(filter)) {
	// msgParams.setMsgId(params.get("cSearchContent"));
	// } else if ("ack".equals(filter)) {
	// msgParams.setAckType(this.getInt(params.get("cSearchContent")));
	// }
	//
	// int cnt = messageMapper.getResevationMessageListCnt(msgParams);
	// logger.info("cnt :::::::{}", cnt);
	//
	// List<Message> list = messageMapper.getResevationMessageList(msgParams);
	// logger.info("list size :::::::{}", list.size());
	//
	// res = new MessagesListRes();
	// res.setRecordsFiltered(cnt);
	// res.setRecordsTotal(cnt);
	// res.setData(list);
	//
	// return res;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.pms.adm.service.SvcService#cancelReservationList(java.lang
	 * .String, kr.co.adflow.pms.adm.request.ReservationCancelReq)
	 */
	// @Override
	// public int cancelReservationList(String appKey, ReservationCancelReq
	// reqIds)
	// throws Exception {
	//
	// String issueId = interceptMapper.selectCashedUserId(appKey);
	//
	// List<Message> list;
	// Message msg;
	// String keyMon = DateUtil.getYYYYMM();
	// String[] msgIds = reqIds.getMsgIds();
	// int cnt = 0;
	// for (int i = 0; i < msgIds.length; i++) {
	//
	// logger.debug("msgIds[i]::" + msgIds[i] + "==");
	// list = messageMapper.selectReservationMessage(msgIds[i]);
	// if (list.size() == 0) {
	// continue;
	// }
	// msg = list.get(0);
	//
	// logger.debug("msg::" + msg.toString());
	//
	// msg.setStatus(StaticConfig.MESSAGE_STATUS_RESEVATION_CANCEL);
	//
	// messageMapper.insertMessageRV(msg);
	// messageMapper.deleteReservationMessage(msgIds[i]);
	// cnt++;
	//
	// }
	// return cnt;
	// }

}
