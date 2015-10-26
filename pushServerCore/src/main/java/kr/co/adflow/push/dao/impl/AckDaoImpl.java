/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.AckDao;
import kr.co.adflow.push.domain.Acknowledge;
import kr.co.adflow.push.mapper.MessageMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class AckDaoImpl.
 *
 * @author nadir93
 */
@Component
public class AckDaoImpl implements AckDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AckDaoImpl.class);

	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.dao.AckDao#get(int)
	 */
	@Override
	public Acknowledge[] get(int msgID) throws Exception {
		logger.debug("get시작()");
		MessageMapper msgMapper = sqlSession.getMapper(MessageMapper.class);
		Acknowledge[] acks = msgMapper.getAckAll(msgID);
		logger.debug("get종료(" + acks + ")");
		return acks;
	}

}
