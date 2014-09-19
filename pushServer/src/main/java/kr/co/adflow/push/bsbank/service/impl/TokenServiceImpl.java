package kr.co.adflow.push.bsbank.service.impl;

import javax.annotation.Resource;

import kr.co.adflow.push.bsbank.dao.UserDao;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.impl.AbstractTokenServiceImpl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author nadir93
 * @date 2014. 7. 29.
 */
@Service
public class TokenServiceImpl extends AbstractTokenServiceImpl {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenServiceImpl.class);

	@Resource
	UserDao uDao;

	@Override
	public Token post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		Token tk = super.post(user);
		kr.co.adflow.push.domain.bsbank.User bsUser = uDao
				.get(user.getUserID());

		logger.debug("부산은행사용자=" + bsUser);
		if (bsUser != null) {
			// 부산은행디비를 이용해 사용자이름과 전화번호 업데이트
			user.setName(bsUser.getGw_user_nm());
			// user.setDept(bsUser.getGw_deptmt_cdnm());
			user.setPhone(bsUser.getMpno());
			logger.debug("user=" + user);
			// update user table
			userDao.putWithoutRole(user);
		}
		logger.debug("post종료(token=" + tk + ")");
		return tk;
	}

}
