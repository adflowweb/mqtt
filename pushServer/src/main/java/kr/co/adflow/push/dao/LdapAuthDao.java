package kr.co.adflow.push.dao;

/**
 * @author nadir93
 * @date 2014. 6. 24.
 */
public interface LdapAuthDao {

	boolean auth(String userID, String password) throws Exception;

}
