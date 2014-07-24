package kr.co.adflow.push.bsbank.dao;

import kr.co.adflow.push.domain.bsbank.Answer;
import kr.co.adflow.push.domain.bsbank.Poll;
import kr.co.adflow.push.domain.bsbank.PollResponse;

public interface PollDao {

	/**
	 * 설문조사입력
	 * 
	 * @param poll
	 * @return
	 * @throws Exception
	 */
	int post(Poll poll) throws Exception;

	/**
	 * 설문조사항목입력
	 * 
	 * @param poll
	 * @return
	 * @throws Exception
	 */
	int postAnswer(int id, int answerid, String content) throws Exception;

	/**
	 * 설문조사가져오기
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Poll get(int id) throws Exception;

	/**
	 * 모든설문조사가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Poll[] getPolls() throws Exception;

	/**
	 * 설문조사삭제하기
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int delete(int id) throws Exception;

	/**
	 * 설문항목가져오기
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Answer[] getAnswers(int id) throws Exception;

	/**
	 * 설문결과조회
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	PollResponse[] getResults(int id) throws Exception;

	/**
	 * 설문결과조회
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	PollResponse[] getSum(int id) throws Exception;

	/**
	 * 해당유저설문결과조회
	 * 
	 * @param id
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	Answer getResult(int id, String userID) throws Exception;

}
