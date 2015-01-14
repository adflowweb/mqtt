/*
 * 
 */
package kr.co.adflow.push.ktp.service.impl;

//import kr.co.adflow.push.ktp.dao.UserDao;
import kr.co.adflow.push.domain.Token;
import kr.co.adflow.push.domain.User;
import kr.co.adflow.push.service.impl.AbstractTokenServiceImpl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class TokenServiceImpl.
 *
 * @author nadir93
 * @date 2014. 7. 29.
 */
@Service
public class TokenServiceImpl extends AbstractTokenServiceImpl {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(TokenServiceImpl.class);

//	@Resource
//	UserDao uDao;

	/* (non-Javadoc)
 * @see kr.co.adflow.push.service.impl.AbstractTokenServiceImpl#post(kr.co.adflow.push.domain.User)
 */
@Override
	public Token post(User user) throws Exception {
		logger.debug("post시작(user=" + user + ")");
		Token tk = super.post(user);
		
		//KTP-skip-start
//		kr.co.adflow.push.domain.ktp.User bsUser = uDao
//				.get(user.getUserID());
//		user.setName(bsUser.getGw_user_nm());
//		// user.setDept(bsUser.getGw_deptmt_cdnm());
//		user.setPhone(bsUser.getMpno());
//		logger.debug("user=" + user);
//		// update user table
//		userDao.putWithoutRole(user);
		//KTP-skip-end
		
		
		logger.debug("post종료(token=" + tk + ")");
		return tk;
	}

}
