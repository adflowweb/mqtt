package kr.co.adflow.push.ktp.dao;

import kr.co.adflow.push.domain.ktp.Affiliate;
import kr.co.adflow.push.domain.ktp.Department;
import kr.co.adflow.push.domain.ktp.User;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface GroupDao {

	/**
	 * 계열사정보가져오기
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	Affiliate[] get() throws Exception;

	/**
	 * 토픽정보가져오기
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	User getTopic(String userID) throws Exception;

	Department[] getDept(String groupID) throws Exception;

	Department[] getAllDept() throws Exception;

}
