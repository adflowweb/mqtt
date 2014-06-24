package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.GroupDao;
import kr.co.adflow.push.domain.Group;
import kr.co.adflow.push.mapper.GroupMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component
public class GroupDaoImpl implements GroupDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(GroupDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	@Autowired
	private SqlSession sqlSession;

	@Override
	public Group[] get(String userID) throws Exception {
		logger.debug("get시작(userID=" + userID + ")");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		Group[] result = grpMapper.get(userID);
		logger.debug("get종료(updates=" + result + ")");
		return result;
	}

	@Override
	public int post(Group grp) throws Exception {
		logger.debug("post시작(Group=" + grp + ")");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		int result = grpMapper.post(grp);
		logger.debug("post종료(updates=" + result + ")");
		return result;
	}

	@Override
	public int delete(String userID, String topic) throws Exception {
		logger.debug("delete시작(userID=" + userID + ", topic=" + topic + ")");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		int result = grpMapper.delete(userID, topic);
		logger.debug("delete종료(updates=" + result + ")");
		return result;
	}

}
