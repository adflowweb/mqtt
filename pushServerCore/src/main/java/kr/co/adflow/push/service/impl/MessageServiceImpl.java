/*
 * 
 */
package kr.co.adflow.push.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.adflow.push.domain.ktp.MsgParams;
import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.domain.ktp.MessagesRes;
import kr.co.adflow.push.service.MessageService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageServiceImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Service
public class MessageServiceImpl implements MessageService {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	/** The message dao. */
	@Resource
	MessageDao messageDao;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.MessageService#get(int)
	 */
	@Override
	public Message get(int messageID) throws Exception {
		logger.debug("get시작(msg=" + messageID + ")");
		Message msg = messageDao.get(messageID);
		logger.debug("get종료(msg=" + msg + ")");
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.MessageService#post(kr.co.adflow.push.domain
	 * .Message)
	 */
	@Override
	public int post(Message msg) throws Exception {
		logger.debug("post시작(msg=" + msg + ")");
		int count = messageDao.post(msg);
		logger.debug("post종료(updates=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.service.MessageService#put(kr.co.adflow.push.domain
	 * .Message)
	 */
	@Override
	public int put(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		int count = messageDao.put(msg);
		logger.debug("put종료(updates=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.service.MessageService#delete(int)
	 */
	@Override
	public int delete(int msgID) throws Exception {
		logger.debug("delete시작(msgID=" + msgID + ")");
		int count = messageDao.delete(msgID);
		logger.debug("delete종료(updates=" + count + ")");
		return count;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.MessageService#getMsgs()
	 */
	@Override
	public Message[] getMsgs() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.MessageService#getDeliveredMsgs()
	 */
	@Override
	public Message[] getDeliveredMsgs() throws Exception {
		logger.debug("getDeliveredMsgs시작()");
		Message[] msg = messageDao.getDeliveredMsgs();
		logger.debug("getDeliveredMsgs종료(msg=" + msg + ")");
		return msg;
	}
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.MessageService#getMessageList()
	 */
	@Override
	public MessagesRes getMessageList(Map<String, String> params) throws Exception {
		logger.debug("getDeliveredMsgs시작()");
		
		
		MsgParams msgParams = new MsgParams();
		
		msgParams.setiDisplayStart(this.getInt(params.get("iDisplayStart")));
		msgParams.setiDisplayLength(this.getInt(params.get("iDisplayLength")));

		msgParams.setDateStart(this.getDate(params.get("cSearchDateStart")));
		msgParams.setDateEnd(this.getDate(params.get("cSearchDateEnd")));
		msgParams.setReceiver(params.get("cSearchReceiver"));
		if (params.get("cSearchType") == null) {
			msgParams.setType(0);
		} else {
			msgParams.setType(this.getInt(params.get("cSearchType")));
		}
		
		logger.debug("=== msgParams::" + msgParams.toString());
		MessagesRes msg = messageDao.getMessageList(msgParams);
		logger.debug("getDeliveredMsgs종료(msg=" + msg + ")");
		return msg;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.service.MessageService#getReservationMsgs()
	 */
	@Override
	public Message[] getReservationMsgs() throws Exception {
		logger.debug("getReservationMsgs시작()");
		Message[] msg = messageDao.getReservationMsgs();
		logger.debug("getReservationMsgs종료(msg=" + msg + ")");
		return msg;
	}
	
	/**
	 * Gets the int.
	 *
	 * @param string the string
	 * @return the int
	 */
	private int getInt(String string) {
		return Integer.parseInt(string);
	}
	
	/**
	 * Gets the date.
	 *
	 * @param string the string
	 * @return the date
	 */
	private Date getDate(String string) {
		return this.fromISODateString(string);
	}
	
	/**
	 * From iso date string.
	 *
	 * @param isoDateString the iso date string
	 * @return the date
	 */
	public static Date fromISODateString(String isoDateString) {
		if (isoDateString == null) {
			return null;
		}

		Calendar calendar = javax.xml.bind.DatatypeConverter
				.parseDateTime(isoDateString);

		return calendar.getTime();
	}
}
