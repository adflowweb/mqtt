package kr.co.adflow.push.ktp.handler;

import javapns.Push;

import javax.annotation.Resource;

//import kr.co.adflow.push.ktp.dao.UserDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.User;
import kr.co.adflow.push.handler.AbstractMessageHandler;
import kr.co.adflow.push.mapper.DeviceMapper;

import org.json.simple.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 7. 29.
 */
@Component
public class MessageHandler extends AbstractMessageHandler {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageHandler.class);

}
