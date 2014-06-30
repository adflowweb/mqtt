package kr.co.adflow.push.bsbank.mapper;

import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;

public interface GroupMapper {

	Affiliate[] get() throws Exception;

	Department[] getAllDept() throws Exception;

	Department[] getDept(String userID) throws Exception;

	int post(Topic grp) throws Exception;

	int delete(String userID, String group) throws Exception;

}
