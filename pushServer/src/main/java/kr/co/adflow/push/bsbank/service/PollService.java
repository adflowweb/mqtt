package kr.co.adflow.push.bsbank.service;

import kr.co.adflow.push.domain.bsbank.Poll;

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
}
