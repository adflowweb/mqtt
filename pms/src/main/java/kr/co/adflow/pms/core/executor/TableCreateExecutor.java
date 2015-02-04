package kr.co.adflow.pms.core.executor;

import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.mapper.TableMgtMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class TableCreateExecutor {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TableCreateExecutor.class);
	
	@Autowired
	private TableMgtMapper tableMgtMapper;
	
	//@Scheduled(cron="0 * * * * *")
	public void createTable() {
		
//		String name = DateUtil.getYYYYMM(0);
//		
//		logger.info("createTable1");
//		
//		try {
//			tableMgtMapper.selectMessage(name);
//			logger.info("selectMessage");
//		} catch(Exception e) {
//			tableMgtMapper.createMessage(name);
//			logger.info("createMessage");
//		}
//		logger.info("createTable2");
//		try {
//			tableMgtMapper.selectContent(name);
//			logger.info("selectContent");
//		} catch(Exception e) {
//			tableMgtMapper.createContent(name);
//			logger.info("createContent");
//		}
//		logger.info("createTable3");
//		try {
//			tableMgtMapper.selectAck(name);
//			logger.info("selectAck");
//		} catch(Exception e) {
//			tableMgtMapper.createAck(name);
//			logger.info("createAck");
//		}
		
		
	}

}
