package kr.co.adflow.push.bsbank.dao;

import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
public interface GroupDao {

	Affiliate[] get() throws Exception;

	int post(Topic grp) throws Exception;

	int delete(String userID, String topic) throws Exception;

	Department[] getDept(String groupID) throws Exception;

	Department[] getAllDept() throws Exception;

}
