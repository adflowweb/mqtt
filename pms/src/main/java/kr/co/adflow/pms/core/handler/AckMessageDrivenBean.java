/*
 * 
 */
package kr.co.adflow.pms.core.handler;

import java.util.Date;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import kr.co.adflow.pms.core.util.AckTRLog;
import kr.co.adflow.pms.domain.Ack;
import kr.co.adflow.pms.domain.mapper.AckMapper;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

// TODO: Auto-generated Javadoc
//@Component("ackMessageDrivenBean")
/**
 * The Class AckMessageDrivenBean.
 */
public class AckMessageDrivenBean implements MessageListener {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AckMessageDrivenBean.class);

	/** The ack mapper. */
	@Autowired
	private AckMapper ackMapper;

	/** The ctl q mapper. */
	@Autowired
	private CtlQMapper ctlQMapper;

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The executor server id. */
	@Value("#{pms['executor.server.id1']}")
	private String EXECUTOR_SERVER_ID;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {

		logger.debug("Message Driven Bean: New Message");

		byte[] body = null;
		try {
			body = new byte[(int) ((BytesMessage) message).getBodyLength()];
			((BytesMessage) message).readBytes(body);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Ack ack = this.getAck(body);

		// message tran log
		try {
			AckTRLog.log(ack);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int cnt = 0;

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("msgId", ack.getMsgId());

		logger.debug("callback param ::" + param.toString());
		try {

			cnt = ackMapper.insertAck(ack);

			logger.debug("ack result : {}", cnt);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the ctl q.
	 * 
	 * @param ack
	 *            the ack
	 * @return the ctl q
	 */
	// private CtlQ getCtlQ(Ack ack) {
	// CtlQ ctlQ = new CtlQ();
	//
	// logger.debug("========== ack type: " + ack.getAckType());
	// if (ack.getAckType().equals("pma")) {
	// ctlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_PMA);
	// logger.debug("========== setExeType1: " + ctlQ.getExeType());
	// } else {
	// ctlQ.setExeType(StaticConfig.CONTROL_QUEUE_EXECUTOR_TYPE_CALLBACK_APP);
	// logger.debug("========== setExeType2: " + ctlQ.getExeType());
	// }
	// logger.debug("========== setExeType3: " + ctlQ.getExeType());
	//
	// ctlQ.setTableName(ack.getKeyMon());
	// ctlQ.setMsgId(ack.getMsgId());
	// ctlQ.setIssueTime(new Date());
	// ctlQ.setServerId(EXECUTOR_SERVER_ID);
	// // ctlQ.setServerId("S01");
	//
	// return ctlQ;
	// }

	/**
	 * Gets the ack.
	 * 
	 * @param body
	 *            the body
	 * @return the ack
	 */
	private Ack getAck(byte[] body) {
		Ack ack = new Ack();

		String text = new String(body);
		JSONObject msgObject = new JSONObject(text);

		// ack.setKeyMon(this.getKeyMon(msgObject.getString("msgId")));
		ack.setMsgId(msgObject.getString("msgId"));
		ack.setAckType(msgObject.getString("ackType"));
		ack.setTokenId(msgObject.getString("token"));
		ack.setAckTime(new Date(msgObject.getLong("ackTime")));
		ack.setServerId(EXECUTOR_SERVER_ID);
		// ack.setServerId("S01");

		return ack;

	}

	/**
	 * Gets the key mon.
	 * 
	 * @param string
	 *            the string
	 * @return the key mon
	 */
	private String getKeyMon(String string) {
		String result = string.substring(0, 6);
		logger.debug("getKeyMon is {}", result);
		return result;
	}

}
