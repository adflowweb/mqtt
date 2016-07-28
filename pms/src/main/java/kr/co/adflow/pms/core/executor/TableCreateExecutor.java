/*
 * 
 */
package kr.co.adflow.pms.core.executor;

import kr.co.adflow.pms.core.handler.ZookeeperHandler;
import kr.co.adflow.pms.core.util.DateUtil;
import kr.co.adflow.pms.domain.mapper.TableMgtMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class TableCreateExecutor.
 */
@Component("tableCreateExecutor")
public class TableCreateExecutor {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(TableCreateExecutor.class);

	/** The table mgt mapper. */
	@Autowired
	private TableMgtMapper tableMgtMapper;

	@Autowired
	ZookeeperHandler zookeeperHandler;

	/**
	 * Creates the table.
	 */
	public void createTable() {

		if (!zookeeperHandler.getLeader()) {
			logger.debug("현재 리더가 아닙니다!");
			return;
		}

		logger.debug("현재 리더 입니다!");
		String name = DateUtil.getYYYYMM(1);

		logger.info("createTable1");

		try {
			tableMgtMapper.selectMessage(name);
			logger.info("selectMessage");
		} catch (Exception e) {
			tableMgtMapper.createMessage(name);
			logger.info("createMessage");
		}
		logger.info("createTable2");
		try {
			tableMgtMapper.selectContent(name);
			logger.info("selectContent");
		} catch (Exception e) {
			tableMgtMapper.createContent(name);
			logger.info("createContent");
		}
		logger.info("createTable3");
		try {
			tableMgtMapper.selectAck(name);
			logger.info("selectAck");
		} catch (Exception e) {
			tableMgtMapper.createAck(name);
			logger.info("createAck");
		}

		logger.info("createTable4");
		try {
			tableMgtMapper.selectGroupMessage(name);
			logger.info("selectGroupMessage");
		} catch (Exception e) {
			tableMgtMapper.createGroupMessage(name);
			logger.info("createGroupMessage");
		}

	}

}
