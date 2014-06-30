package kr.co.adflow.push.bsbank.service;

import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;

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

	int post(Topic grp) throws Exception;

	int delete(String userID, String topic) throws Exception;

}
