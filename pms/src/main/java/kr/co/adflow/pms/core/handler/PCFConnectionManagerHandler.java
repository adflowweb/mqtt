/*
 * 
 */
package kr.co.adflow.pms.core.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQSimpleConnectionManager;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonDateSerializer.
 */

public class PCFConnectionManagerHandler extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(PCFConnectionManagerHandler.class);

	public void init() throws ServletException {

		try {

			File file = new File(System.getProperty("user.home")
					+ "/pms/config.properties");

			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));

			Properties prop = new Properties();

			prop.load(bis);

			MQEnvironment.hostname = prop.getProperty("mq.pcf.hostname");
			MQEnvironment.channel = prop.getProperty("mq.pcf.channel");
			MQEnvironment.userID = prop.getProperty("mq.pcf.userID");
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			MQSimpleConnectionManager connMan = new MQSimpleConnectionManager();
			connMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
			connMan.setTimeout(3600000);
			connMan.setMaxConnections(200);
			connMan.setMaxUnusedConnections(50);

			MQEnvironment.setDefaultConnectionManager(connMan);

			logger.debug("=== PCFConnectionManagerHandler Load OK ===");
		} catch (Exception mqe) {
			logger.error(mqe.toString());
		}

	}
}
