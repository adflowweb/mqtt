package kr.co.adflow.push.bsbank.dao;

import kr.co.adflow.push.domain.bsbank.Poll;

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

}
