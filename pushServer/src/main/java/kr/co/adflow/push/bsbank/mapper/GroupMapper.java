package kr.co.adflow.push.bsbank.mapper;

import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;
import kr.co.adflow.push.domain.bsbank.User;

public interface GroupMapper {

	/**
	 * 계열사정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Affiliate[] get() throws Exception;

	/**
	 * 해당사용자 그룹정보가져오기
	 * 
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	User getTopic(String userID) throws Exception;

	/**
	 * 모든부서정보가져오기
	 * 
	 * @return
	 * @throws Exception
	 */
	Department[] getAllDept() throws Exception;

	/**
	 * 계열사소속부서정보가져오기
	 * 
	 * @param affiliateID
	 * @return
	 * @throws Exception
	 */
	Department[] getDept(String affiliateID) throws Exception;

	int post(Topic grp) throws Exception;

	int delete(String userID, String group) throws Exception;

}
