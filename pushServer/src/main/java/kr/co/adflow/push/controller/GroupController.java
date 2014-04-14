package kr.co.adflow.push.controller;

import javax.annotation.Resource;

import kr.co.adflow.push.service.GroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author nadir93
 * @date 2014. 4. 14.
 * 
 */
@Controller
public class GroupController {

	private static final Logger logger = LoggerFactory
			.getLogger(GroupController.class);

	@Resource
	private GroupService groupService;
}
