package kr.co.adflow.push.bsbank.dao.impl;

import kr.co.adflow.push.bsbank.dao.PollDao;
import kr.co.adflow.push.domain.bsbank.Poll;
import kr.co.adflow.push.mapper.PollMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PollDaoImpl implements PollDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PollDaoImpl.class);

	@Autowired
	private SqlSession sqlSession;

	/*
	 * 설문조사입력
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.bsbank.dao.PollDao#post(kr.co.adflow.push.domain.bsbank
	 * .Poll)
	 */
	@Override
	public int post(Poll poll) throws Exception {
		logger.debug("post시작(poll=" + poll + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		int result = pollMapper.post(poll);
		logger.debug("post종료(result=" + result + ")");
		return result;
	}

	/*
	 * 설문조사항목입력
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.dao.PollDao#postAnswer(int, int,
	 * java.lang.String)
	 */
	@Override
	public int postAnswer(int id, int answerid, String content)
			throws Exception {
		logger.debug("postAnswer시작(id=" + id + ", answerid=" + answerid
				+ ", content=" + content + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		int result = pollMapper.postAnswer(id, answerid, content);
		logger.debug("postAnswer종료(result=" + result + ")");
		return result;
	}

}
