/*
 * 
 */
package kr.co.adflow.push.mapper;

import kr.co.adflow.push.domain.Topic;

// TODO: Auto-generated Javadoc
/**
 * The Interface GroupMapper.
 */
public interface GroupMapper {

	/**
	 * Gets the.
	 *
	 * @param userID the user id
	 * @return the topic[]
	 * @throws Exception the exception
	 */
	Topic[] get(String userID) throws Exception;

	/**
	 * Post.
	 *
	 * @param grp the grp
	 * @return the int
	 * @throws Exception the exception
	 */
	int post(Topic grp) throws Exception;

	/**
	 * Delete.
	 *
	 * @param userID the user id
	 * @param group the group
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String userID, String group) throws Exception;

}
