package kr.co.adflow.push.bsbank.handler;

import javapns.Push;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.dao.UserDao;
import kr.co.adflow.push.domain.Device;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.bsbank.User;
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

	@Resource
	UserDao userDao;

	@Override
	protected void sendGroupAPNS(DeviceMapper deviceMapper, Message msg)
			throws Exception {
		logger.debug("sendGroupAPNS시작(msg=" + msg + ")");
		// 그룹메시지
		// 해당그룹사용자를 가져온다.
		String group = msg.getReceiver().substring(8);
		logger.debug("group=" + group);
		int type = msg.getType();

		User[] users = null;
		if (type == 2) {
			// 2:계열사
			logger.debug("계열사메시지입니다.");
			users = userDao.getUsersBySBSD(group);
		} else if (type == 3) {
			// 3:부서
			logger.debug("부서메시지입니다.");
			users = userDao.getUsersByDepartment(group);
		} else {
			logger.debug("해당타입의처리자가없습니다.");
		}

		Message message = msgMapper.get(msg.getId());
		logger.debug("message(컨텐츠포함)=" + message);

		JSONObject obj = (JSONObject) parser.parse(message.getContent());
		JSONObject noti = (JSONObject) (obj.get("notification"));
		String title = (String) (noti.get("contentTitle"));
		logger.debug("title=" + title);

		for (int i = 0; i < users.length; i++) {
			String userID = users[i].getGw_stf_cdnm();
			logger.debug("userID=" + userID);
			Device[] devices = deviceMapper.getAppleDevicesByUser(userID);

			for (int j = 0; j < devices.length; j++) {
				logger.debug("apnsSend. apnsToken=" + devices[j].getApnsToken());
				Push.combined(title, devices[j].getUnRead() + 1, "default",
						apnsKeyFile, apnsKeyFilePassword, apnsProduction,
						devices[j].getApnsToken());
				logger.debug("APNS완료.userID=" + userID + ", deivceID="
						+ devices[j].getDeviceID() + ", unreadCount="
						+ (devices[j].getUnRead() + 1));
				deviceMapper.increaseUnread(userID, devices[j].getDeviceID());
				logger.debug("unread카운트증가완료");
			}

		}

		// user 테이블과 맵핑하여 apns token list 를 가져온다.
		// list기반으로 apns발송한다.
		logger.debug("sendGroupAPNS종료()");
	}
}
