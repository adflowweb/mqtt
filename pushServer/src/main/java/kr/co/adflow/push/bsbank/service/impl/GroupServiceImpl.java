package kr.co.adflow.push.bsbank.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.dao.GroupDao;
import kr.co.adflow.push.bsbank.service.GroupService;
import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;
import kr.co.adflow.push.domain.bsbank.User;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 6. 30.
 */
@Service("bsBankGroupServiceImpl")
public class GroupServiceImpl implements GroupService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(GroupServiceImpl.class);

	@Resource
	GroupDao grpDao;

	/*
	 * 계열사 정보 가져오기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.busanbank.service.GroupService#get()
	 */
	@Override
	public Affiliate[] get() throws Exception {
		logger.debug("get시작()");
		Affiliate[] grp = grpDao.get();
		logger.debug("get종료(result=" + grp + ")");
		return grp;
	}

	/*
	 * 계열사부서정보가져오기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.adflow.push.busanbank.service.GroupService#getDept(java.lang.String
	 * )
	 */
	@Override
	public Department[] getDept(String groupID) throws Exception {
		logger.debug("getDept시작()");
		Department[] grp = grpDao.getDept(groupID);
		logger.debug("getDept종료(result=" + grp + ")");
		return grp;
	}

	/*
	 * 모든부서정보가져오기
	 * 
	 * (non-Javadoc)
	 * 
	 * @see kr.co.adflow.push.busanbank.service.GroupService#getAllDept()
	 */
	@Override
	public Department[] getAllDept() throws Exception {
		logger.debug("getAllDept시작()");
		Department[] grp = grpDao.getAllDept();
		logger.debug("getAllDept종료(result=" + grp + ")");
		return grp;
	}

	@Override
	public User getTopic(String userID) throws Exception {
		logger.debug("get시작(group=" + userID + ")");
		User grp = grpDao.getTopic(userID);
		logger.debug("get종료(result=" + grp + ")");
		return grp;
	}

}
