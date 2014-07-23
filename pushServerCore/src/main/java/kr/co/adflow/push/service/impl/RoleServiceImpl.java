package kr.co.adflow.push.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.dao.RoleDao;
import kr.co.adflow.push.dao.UserDao;
import kr.co.adflow.push.domain.Role;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.RoleService;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 7. 21.
 */
@Service
public class RoleServiceImpl implements RoleService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(RoleServiceImpl.class);

	@Resource
	RoleDao roleDao;

	@Resource
	UserDao userDao;

	@Override
	public Role[] getByUser(String userID) throws Exception {
		logger.debug("getByUser시작(userID=" + userID + ")");
		User user = userDao.get(userID);
		Role[] rst = roleDao.get(user.getRole());
		logger.debug("getByUser종료(rst=" + rst + ")");
		return rst;
	}

	@Override
	public Role[] get() throws Exception {
		logger.debug("get시작()");
		Role[] rst = roleDao.get();
		logger.debug("get종료(rst=" + rst + ")");
		return rst;
	}

	@Override
	public Role[] getByRole(String roleID) throws Exception {
		logger.debug("getByRole시작(roleID=" + roleID + ")");
		Role[] rst = roleDao.get(roleID);
		logger.debug("getByRole종료(rst=" + rst + ")");
		return rst;
	}

}
