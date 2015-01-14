/*
 * 
 */
package kr.co.adflow.push.dao.impl;

import kr.co.adflow.push.dao.RoleDao;
import kr.co.adflow.push.domain.Role;
import kr.co.adflow.push.mapper.RoleMapper;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * The Class RoleDaoImpl.
 *
 * @author nadir93
 * @date 2014. 7. 21.
 */
@Repository
public class RoleDaoImpl implements RoleDao {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(RoleDaoImpl.class);

	// Autowired를 사용하여 sqlSession을 사용할수 있다.
	/** The sql session. */
	@Autowired
	private SqlSession sqlSession;

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.RoleDao#get(java.lang.String)
	 */
	@Override
	public Role[] get(String role) throws Exception {
		logger.debug("get시작(role=" + role + ")");
		RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
		Role[] result = roleMapper.get(role);
		logger.debug("get종료(result=" + result + ")");
		return result;
	}

	/* (non-Javadoc)
	 * @see kr.co.adflow.push.dao.RoleDao#get()
	 */
	@Override
	public Role[] get() throws Exception {
		logger.debug("get시작()");
		RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
		Role[] result = roleMapper.getRoles();
		logger.debug("get종료(result=" + result + ")");
		return result;
	}

}
