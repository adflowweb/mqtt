package kr.co.adflow.push.bsbank.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.dao.PollDao;
import kr.co.adflow.push.bsbank.service.PollService;
import kr.co.adflow.push.domain.bsbank.Answer;
import kr.co.adflow.push.domain.bsbank.Poll;
import kr.co.adflow.push.domain.bsbank.PollResponse;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 7. 10.
 * 
 */
@Service
public class PollServiceImpl implements PollService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(PollServiceImpl.class);

	@Resource
	PollDao pollDao;

	/*
	 * 설문조사입력
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.bsbank.service.PollService#post(kr.co.adflow.push.domain
	 * .bsbank.Poll)
	 */
	@Override
	public int post(Poll poll) throws Exception {
		logger.debug("post시작(poll=" + poll + ")");
		int pollID = pollDao.post(poll);

		for (int i = 0; i < poll.getAnswers().length; i++) {
			pollDao.postAnswer(poll.getId(), i, poll.getAnswers()[i]);
		}

		logger.debug("post종료(pollID=" + pollID + ")");
		return pollID;
	}

	/*
	 * 설문조사삭제하기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.service.PollService#delete(int)
	 */
	@Override
	public int delete(int id) throws Exception {
		logger.debug("delete시작(pollID=" + id + ")");
		int result = pollDao.delete(id);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

	/*
	 * 설문조사가져오기(통계포함)
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.service.PollService#get(int)
	 */
	@Override
	public Poll get(int id) throws Exception {
		logger.debug("get시작(pollID=" + id + ")");
		Poll poll = pollDao.get(id);
		Answer[] answers = pollDao.getAnswers(id);
		String[] contents = new String[answers.length];
		for (int i = 0; i < answers.length; i++) {
			contents[i] = answers[i].getContent();
		}
		poll.setAnswers(contents);
		PollResponse[] res = pollDao.getResults(id);
		float[] result = new float[answers.length];
		for (int i = 0; i < answers.length; i++) {
			if (i < res.length && res[i] != null) {
				result[i] = ((float) res[i].getCount() / (float) poll
						.getResponses()) * 100;
				logger.debug(res[i].getCount() + " / " + poll.getResponses()
						+ " * 100 = " + result[i]);
			} else {
				result[i] = 0;
			}

		}
		poll.setResult(result);
		logger.debug("get종료(poll=" + poll + ")");
		return poll;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.bsbank.service.PollService#getPolls()
	 */
	@Override
	public Poll[] getPolls() throws Exception {
		logger.debug("getPolls시작()");
		Poll[] poll = pollDao.getPolls();
		logger.debug("getPolls종료(poll=" + poll + ")");
		return poll;
	}

}
