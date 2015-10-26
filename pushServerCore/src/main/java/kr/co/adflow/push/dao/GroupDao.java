/*
 * 
 */
package kr.co.adflow.push.dao;

import kr.co.adflow.push.domain.Topic;

// TODO: Auto-generated Javadoc
/**
 * The Interface GroupDao.
 *
 * @author nadir93
 * @date 2014. 4. 14.
 */
public interface GroupDao {

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
	 * @param topic the topic
	 * @return the int
	 * @throws Exception the exception
	 */
	int delete(String userID, String topic) throws Exception;

}
