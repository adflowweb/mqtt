package kr.co.adflow.push.controller;

import javax.annotation.Resource;

import kr.co.adflow.push.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UserController.class);

	@Resource
	private UserService userService;
}
