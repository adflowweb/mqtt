/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.MessageDao;
import kr.co.adflow.push.domain.Message;
import kr.co.adflow.push.mapper.MessageMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageDaoImpl.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
@Component
public class MessageDaoImpl implements MessageDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(MessageDaoImpl.class);

	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#get(java.lang.String)
	 */
	@Override
	public Message get(int msgID) throws Exception {
		logger.debug("get시작(msgID=" + msgID + ")");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message msg = msgMapper.get(msgID);
		logger.debug("get종료(" + msg + ")");
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#post(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public int post(Message msg) throws Exception {
		System.out.println("post시작(msg=" + msg + ")");
		logger.debug("post시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.postMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.postContent(msg);
		logger.debug("post종료(count=" + count + ")");
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.dao.MessageDAO#put(kr.co.adflow.push.domain.Message)
	 */
	@Override
	public int put(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.putMsg(msg);
		logger.debug("msg=" + msg);
		msgMapper.putContent(msg);
		logger.debug("put종료(count=" + count + ")");
		return count;
	}
	
	
	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.MessageDao#putIssue(kr.co.adflow.push.domain.Message)
	 */
	public int putIssue(Message msg) throws Exception {
		logger.debug("put시작(msg=" + msg + ")");
		// db 저장
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		int count = msgMapper.putIssue(msg);
		logger.debug("msg=" + msg);
		msgMapper.putContent(msg);
		logger.debug("put종료(count=" + count + ")");
		return count;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.MessageDAO#delete(java.lang.String)
	 */
	@Override
	public int delete(int msgID) throws Exception {
		logger.debug("delete시작(msgID=" + msgID + ")");
		MessageMapper messageMapper = sqlSession.getMapper(MessageMapper.class);
		int result = messageMapper.deleteMsg(msgID);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.MessageDao#getReservationMsgs()
	 */
	@Override
	public Message[] getReservationMsgs() throws Exception {
		logger.debug("getReservationMsgs시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message[] msg = msgMapper.getReservationMsgs();
		logger.debug("getReservationMsgs종료(" + msg + ")");
		return msg;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.MessageDao#getDeliveredMsgs()
	 */
	@Override
	public Message[] getDeliveredMsgs() throws Exception {
		logger.debug("getDeliveredMsgs시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Message[] msg = msgMapper.getDeliveredMsgs();
		logger.debug("getDeliveredMsgs종료(" + msg + ")");
		return msg;
	}
}
