package kr.co.adflow.pms.adm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.adflow.pms.domain.User;
import kr.co.adflow.pms.domain.mapper.UserMapper;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> listAllUser() {
		
		return userMapper.listAll();
	}

}
