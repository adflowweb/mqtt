package kr.co.adflow.push.bsbank.dao.impl;

import kr.co.adflow.push.bsbank.dao.PollDao;
import kr.co.adflow.push.domain.bsbank.Answer;
import kr.co.adflow.push.domain.bsbank.Poll;
import kr.co.adflow.push.domain.bsbank.PollResponse;
import kr.co.adflow.push.mapper.PollMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author nadir93
 * @date 2014. 6. 23.
 */
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
		pollMapper.post(poll);
		logger.debug("post종료(poll=" + poll + ")");
		return poll.getId();
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

	/*
	 * 모든설문조사가져오기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.dao.PollDao#getPolls()
	 */
	@Override
	public Poll[] getPolls() throws Exception {
		logger.debug("getPolls시작()");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		Poll[] poll = pollMapper.getPolls();
		logger.debug("getPolls종료(poll=" + poll + ")");
		return poll;
	}

	@Override
	public int delete(int id) throws Exception {
		logger.debug("delete시작(pollID=" + id + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		int result = pollMapper.delete(id);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	@Override
	public Poll get(int id) throws Exception {
		logger.debug("get시작(pollID=" + id + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		Poll poll = pollMapper.get(id);
		logger.debug("get종료(poll=" + poll + ")");
		return poll;
	}

	/*
	 * 설문항목가져오기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.dao.PollDao#getAnswers(int)
	 */
	@Override
	public Answer[] getAnswers(int id) throws Exception {
		logger.debug("getAnswers시작(pollID=" + id + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		Answer[] answers = pollMapper.getAnswers(id);
		logger.debug("getAnswers종료(answers=" + answers + ")");
		return answers;
	}

	@Override
	public PollResponse[] getResults(int id) throws Exception {
		logger.debug("getResult시작(pollID=" + id + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		PollResponse[] res = pollMapper.getResults(id);
		logger.debug("getResult종료(res=" + res + ")");
		return res;
	}

	@Override
	public Answer getResult(int id, String userID) throws Exception {
		logger.debug("getResult시작(pollID=" + id + ", userID=" + userID + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		Answer res = pollMapper.getResult(id, userID);
		logger.debug("getResult종료(res=" + res + ")");
		return res;
	}

	@Override
	public PollResponse[] getSum(int id) throws Exception {
		logger.debug("getSum시작(pollID=" + id + ")");
		PollMapper pollMapper = sqlSession.getMapper(PollMapper.class);
		PollResponse[] res = pollMapper.getSum(id);
		logger.debug("getSum종료(res=" + res + ")");
		return res;
	}

}
