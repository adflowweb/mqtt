package kr.co.adflow.push.bsbank.dao.impl;

import kr.co.adflow.push.bsbank.dao.GroupDao;
import kr.co.adflow.push.bsbank.mapper.GroupMapper;
import kr.co.adflow.push.domain.Topic;
import kr.co.adflow.push.domain.bsbank.Affiliate;
import kr.co.adflow.push.domain.bsbank.Department;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Component("busanBankGroupDaoImpl")
public class GroupDaoImpl implements GroupDao {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(GroupDaoImpl.class);
	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	@Autowired
	@Qualifier("bsBanksqlSession")
	private SqlSession sqlSession;

	@Override
	public int post(Topic grp) throws Exception {
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

	@Override
	public Affiliate[] get() throws Exception {
		logger.debug("get시작()");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		Affiliate[] result = grpMapper.get();
		logger.debug("get종료(result=" + result + ")");
		return result;
	}

	@Override
	public Department[] getDept(String groupID) throws Exception {
		logger.debug("get시작(groupID=" + groupID + ")");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		Department[] result = grpMapper.getDept(groupID);
		logger.debug("get종료(result=" + result + ")");
		return result;
	}

	@Override
	public Department[] getAllDept() throws Exception {
		logger.debug("getAllDept시작()");
		GroupMapper grpMapper = sqlSession.getMapper(GroupMapper.class);
		Department[] result = grpMapper.getAllDept();
		logger.debug("getAllDept종료(result=" + result + ")");
		return result;
	}

}
