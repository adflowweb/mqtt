package kr.co.adflow.push.dao;

/**
 * @author nadir93
 * @date 2014. 6. 23.
 */
public interface SMSDao<T> {

	void post(String phoneNum, String callbackNum, String msg) throws Exception;

	void setSMSSender(T sender);

	T getSMSSender();

}
