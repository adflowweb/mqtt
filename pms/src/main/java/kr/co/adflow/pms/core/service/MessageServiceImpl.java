/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import kr.co.adflow.pms.core.config.PmsConfig;
import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.exception.MessageRunTimeException;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;
import kr.co.adflow.pms.core.request.MessageReq;
import kr.co.adflow.pms.core.response.AckRes;
import kr.co.adflow.pms.core.response.MessagesListRes;
import kr.co.adflow.pms.core.response.StatisticsRes;
import kr.co.adflow.pms.core.util.CheckUtil;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.core.util.KeyGenerator;
import kr.co.adflow.pms.domain.AckData;
//import kr.co.adflow.pms.core.handler.PCFConnectionManager;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.MsgParams;
import kr.co.adflow.pms.domain.mapper.AckMapper;
import kr.co.adflow.pms.domain.mapper.InterceptMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.SummaryMapper;

import org.json.JSONObject;
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

	@Autowired
	private AckMapper ackMapper;

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
	public StatisticsRes getStatistics(Map<String, String> params)
			throws Exception {
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
				.get("agentAckCnt"))));
		statisticsRes.setAppAckCnt(this.getInt(String.valueOf(summaryResult
				.get("appAckCnt"))));

		return statisticsRes;
	}

	@Override
	public AckRes getAckMessage(Map<String, String> params, String msgId)
			throws Exception {
		AckRes ackRes = new AckRes();
		MsgParams msgParams = new MsgParams();
		msgParams.setMsgId(msgId);
		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
		List<AckData> ackRst = ackMapper.selectAckMessage(msgParams);
		if (ackRst == null || ackRst.size() == 0) {
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_533404,
					"수신확인 내역이 없습니다");
		}

		ackRes.setAckList(ackRst);

		return ackRes;
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

		msg.setQos(message.getQos());
		msg.setIssueId(issueId);
		msg.setMsgType(message.getMsgType());
		msg.setServiceId(message.getServiceId());
		msg.setAck(message.isAck());
		msg.setContentType(message.getContentType());
		msg.setContent(message.getContent());
		msg.setExpiry(message.getExpiry());
		msgId = this.getMsgId();
		msg.setMsgId(msgId);

		// sendJMS
		msg.setReceiver(message.getReceiver());

		msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);
		this.sendJMS(msg);

		// resultCnt++;

		return msg;
	}

	// JMS Send and Message DB insert
	public void sendJMS(Message msg) throws MessageRunTimeException {
		logger.debug("발송정 입력값 시작");
		logger.debug(msg.toString());
		msg.setIssueTime(new Date());
		// JMS message send

		String result = jmsTemplate
				.execute(new DirectMsgHandlerBySessionCallback(jmsTemplate, msg));
		if (result.equals("fail")) {
			logger.debug("메시지 전송실패!!!");
			throw new MessageRunTimeException(StaticConfig.ERROR_CODE_539000,
					"메시지 전송에 실패 하였습니다");
		}
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
