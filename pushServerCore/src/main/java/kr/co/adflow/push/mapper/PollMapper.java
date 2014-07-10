package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.bsbank.Answer;
import kr.co.adflow.push.domain.bsbank.Poll;

import org.apache.ibatis.annotations.Param;

public interface PollMapper {

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
	 * @param id
	 * @param answerid
	 * @param content
	 * @return
	 * @throws Exception
	 */
	int postAnswer(@Param("id") int id, @Param("answerid") int answerid,
			@Param("content") String content) throws Exception;

	/**
	 * 설문조사결과입력
	 * 
	 * @param id
	 * @param answerid
	 * @param userid
	 */
	int postResponse(@Param("id") int id, @Param("answerid") int answerid,
			@Param("userid") String userid) throws Exception;

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
	 * 설문조사가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Poll get(int id) throws Exception;

	/**
	 * 설문항목가져오기
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Answer[] getAnswers(int id) throws Exception;

}
