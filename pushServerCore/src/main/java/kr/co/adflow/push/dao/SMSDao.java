/*
 * 
 */
package kr.co.adflow.push.dao;

// TODO: Auto-generated Javadoc
/**
 * The Interface SMSDao.
 *
 * @author nadir93
 * @param <T> the generic type
 * @date 2014. 6. 23.
 */
public interface SMSDao<T> {

	/**
	 * Post.
	 *
	 * @param phoneNum the phone num
	 * @param callbackNum the callback num
	 * @param msg the msg
	 * @throws Exception the exception
	 */
	void post(String phoneNum, String callbackNum, String msg) throws Exception;

	/**
	 * Sets the SMS sender.
	 *
	 * @param sender the new SMS sender
	 */
	void setSMSSender(T sender);

	/**
	 * Gets the SMS sender.
	 *
	 * @return the SMS sender
	 */
	T getSMSSender();

}
