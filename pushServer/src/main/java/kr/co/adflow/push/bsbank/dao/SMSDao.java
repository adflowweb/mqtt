package kr.co.adflow.push.bsbank.dao;

/**
 * @author nadir93
 * @date 2014. 6. 23.
 */
public interface SMSDao {

	void post(String msg) throws Exception;

}
