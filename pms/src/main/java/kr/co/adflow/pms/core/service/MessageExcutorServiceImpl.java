/*
 * 
 */
package kr.co.adflow.pms.core.service;

import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.config.StaticConfig;
import kr.co.adflow.pms.core.handler.DirectMsgHandlerBySessionCallback;
import kr.co.adflow.pms.core.util.MessageTRLog;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.AckMapper;
import kr.co.adflow.pms.domain.mapper.CtlQMapper;
import kr.co.adflow.pms.domain.mapper.MessageMapper;
import kr.co.adflow.pms.domain.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageSendServiceImpl.
 */
@Service
public class MessageExcutorServiceImpl implements MessageExcutorService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(MessageExcutorServiceImpl.class);

	/** The message mapper. */
	@Autowired
	private MessageMapper messageMapper;

	/** The user mapper. */
	@Autowired
	private UserMapper userMapper;

	/** The jms template. */
	@Autowired
	private JmsTemplate jmsTemplate;

	/** The validation mapper. */
	// @Autowired
	// private ValidationMapper validationMapper;

	/** The ctl q mapper. */
	@Autowired
	private CtlQMapper ctlQMapper;

	/** The ack mapper. */
	@Autowired
	private AckMapper ackMapper;

	// /** The rest template. */
	// @Autowired
	// RestTemplate restTemplate;

	@Override
	public int sendReservationMessageArray(String serverId, int limit) {
		logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!************************예약 메시지 전송시작");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectReservationList(param);
		int updateCnt = 0;
		for (Message msg : list) {
			logger.debug("updateCNT:" + updateCnt);

			jmsTemplate.execute(new DirectMsgHandlerBySessionCallback(
					jmsTemplate, msg));

			try {
				MessageTRLog.log(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String msgId = msg.getMsgId();
			msg.setMsgId(msgId);
			msg.setStatus(StaticConfig.MESSAGE_STATUS_SEND);
			messageMapper.insertMessageRV(msg);
			messageMapper.insertContent(msg);
			messageMapper.deleteReservationMessage(msgId);
			updateCnt++;
		}

		if (updateCnt > 0)
			logger.info("sendMessageArray is cnt {}", updateCnt);
		logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!***********************예약 메시지 전송끝");
		return updateCnt;
	}

}
