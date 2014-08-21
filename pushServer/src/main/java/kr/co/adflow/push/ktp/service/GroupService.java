package kr.co.adflow.push.ktp.service;

import kr.co.adflow.push.domain.ktp.Affiliate;
import kr.co.adflow.push.domain.ktp.Department;
import kr.co.adflow.push.domain.ktp.User;

/**
 * @author nadir93
 * @date 2014. 6. 30.
 */
public interface GroupService {

	/**
	 * 계열사정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Affiliate[] get() throws Exception;

	/**
	 * 유저토픽정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	User getTopic(String userID) throws Exception;

	/**
	 * 계열사부서정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Department[] getDept(String groupID) throws Exception;

	/**
	 * 모든부서정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Department[] getAllDept() throws Exception;

}
