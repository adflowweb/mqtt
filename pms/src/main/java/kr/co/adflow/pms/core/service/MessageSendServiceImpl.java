package kr.co.adflow.pms.core.service;

import java.util.HashMap;
import java.util.List;

import kr.co.adflow.pms.core.handler.DirectMsgHandler;
import kr.co.adflow.pms.domain.Message;
import kr.co.adflow.pms.domain.mapper.MessageMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSendServiceImpl implements MessageSendService {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageSendServiceImpl.class);

	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public int sendMessageArray(String serverId, int limit) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("serverId", serverId);
		param.put("limit", limit);

		List<Message> list = messageMapper.selectList(param);

		int updateCnt = 0;
		for (Message msg : list) {

			jmsTemplate.execute(msg.getReceiver(), new DirectMsgHandler(msg));

			msg.setStatus(1);

			int resultCnt = messageMapper.updateStatus(msg);
			logger.info("update count is {}", resultCnt);
			updateCnt++;
		}

		logger.info("sendMessageArray is cnt {}", updateCnt);
		return updateCnt;
	}

}
