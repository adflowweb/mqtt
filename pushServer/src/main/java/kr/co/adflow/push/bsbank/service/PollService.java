package kr.co.adflow.push.bsbank.service;

import kr.co.adflow.push.domain.bsbank.Poll;
import kr.co.adflow.push.domain.bsbank.PollResponse;

public interface PollService {

	/**
	 * 설문조사입력
	 * 
	 * @param poll
	 * @return
	 * @throws Exception
	 */
	int post(Poll poll) throws Exception;

	/**
	 * 설문조사삭제
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int delete(int id) throws Exception;

	/**
	 * 설문조사조회
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Poll get(int id) throws Exception;

	/**
	 * 설문조사조회(유저별)
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	PollResponse getResult(int id, String userID) throws Exception;

	/**
	 * 모든설문조사가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Poll[] getPolls() throws Exception;

	/**
	 * 설문조사결과조회
	 * 
	 * @param pollID
	 * @return
	 * @throws Exception
	 */
	PollResponse[] getResults(int pollID) throws Exception;
}
