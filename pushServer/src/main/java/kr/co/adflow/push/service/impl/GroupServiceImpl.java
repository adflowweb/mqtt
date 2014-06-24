package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.GroupDao;
import kr.co.adflow.push.domain.Group;
import kr.co.adflow.push.service.GroupService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Service
public class GroupServiceImpl implements GroupService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(GroupServiceImpl.class);

	@Resource
	GroupDao grpDao;

	@Override
	public Group[] get(String userID) throws Exception {
		logger.debug("get시작(group=" + userID + ")");
		Group[] grp = grpDao.get(userID);
		logger.debug("get종료(result=" + grp + ")");
		return grp;
	}

	@Override
	public int post(Group grp) throws Exception {
		logger.debug("post시작(group=" + grp + ")");
		int result = grpDao.post(grp);
		logger.debug("post종료(result=" + result + ")");
		return result;
	}

	@Override
	public int delete(String userID, String topic) throws Exception {
		logger.debug("delete시작(userID=" + userID + ",topic=" + topic + ")");
		int result = grpDao.delete(userID, topic);
		logger.debug("delete종료(result=" + result + ")");
		return result;
	}

}
