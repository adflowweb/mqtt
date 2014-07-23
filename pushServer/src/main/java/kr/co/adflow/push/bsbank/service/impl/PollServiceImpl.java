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
		logger.debug("answers.length=" + answers.length);
		String[] contents = new String[answers.length];
		logger.debug("contents.length=" + contents.length);
		for (int i = 0; i < answers.length; i++) {
			contents[i] = answers[i].getContent();
			logger.debug("contents[" + i + "]" + contents[i]);
		}
		poll.setAnswers(contents);
		PollResponse[] res = pollDao.getSum(id);
		logger.debug("res.length=" + res.length);
		float[] result = new float[answers.length];
		for (int i = 0; i < answers.length; i++) {

			result[i] = 0;
			for (int j = 0; j < res.length; j++) {
				if (i == res[j].getAnswerid()) {
					result[i] = ((float) res[j].getCount() / (float) poll
							.getResponses()) * 100;
					logger.debug("result[" + i + "] = " + res[j].getCount()
							+ " / " + poll.getResponses() + " * 100 ");
				}
			}

			// if (i < res.length) {
			// result[i] = ((float) res[i].getCount() / (float) poll
			// .getResponses()) * 100;
			// logger.debug("result[" + i + "] = " + res[i].getCount() + " / "
			// + poll.getResponses() + " * 100 ");
			// } else {
			// result[i] = 0;
			// logger.debug("result[" + i + "] = " + result[i]);
			// }

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

	@Override
	public PollResponse getResult(int id, String userID) throws Exception {
		logger.debug("getResult시작(pollID=" + id + ", userID=" + userID + ")");
		PollResponse res = pollDao.getResult(id, userID);
		logger.debug("getResult종료(PollResponse=" + res + ")");
		return res;
	}

	@Override
	public PollResponse[] getResults(int pollID) throws Exception {
		logger.debug("getResults시작(pollID=" + pollID + ")");
		PollResponse[] res = pollDao.getResults(pollID);
		logger.debug("getResults종료(PollResponse=" + res + ")");
		return res;
	}

}
