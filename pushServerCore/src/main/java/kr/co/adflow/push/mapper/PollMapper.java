package kr.co.adflow.push.mapper;

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

}
