/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.HADao;
import kr.co.adflow.push.domain.HA;
import kr.co.adflow.push.mapper.HAMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class HADaoImpl.
 *
 * @author nadir93
 * @date 2014. 7. 24.
 */
@Component
public class HADaoImpl implements HADao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(HADaoImpl.class);

	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.HADao#get()
	 */
	@Override
	public HA get() throws Exception {
		logger.debug("get시작()");
		HAMapper haMapper = sqlSession.getMapper(HAMapper.class);
		HA ha = haMapper.get();
		logger.debug("get종료(ha=" + ha + ")");
		return ha;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.HADao#put(kr.co.adflow.push.domain.HA)
	 */
	@Override
	public int put(HA ha) throws Exception {
		logger.debug("get시작()");
		HAMapper haMapper = sqlSession.getMapper(HAMapper.class);
		int rst = haMapper.put(ha);
		logger.debug("get종료(rst=" + rst + ")");
		return rst;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.HADao#post(kr.co.adflow.push.domain.HA)
	 */
	@Override
	public int post(HA ha) throws Exception {
		logger.debug("get시작()");
		HAMapper haMapper = sqlSession.getMapper(HAMapper.class);
		int rst = haMapper.post(ha);
		logger.debug("get종료(rst=" + rst + ")");
		return rst;
	}

}
