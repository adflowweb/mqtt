/*
 * 
 */
package kr.co.adflow.push.ktp.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import kr.co.adflow.push.handler.AbstractMessageHandler;
import kr.co.adflow.push.ktp.service.impl.PCFServiceImpl;

import org.slf4j.LoggerFactory;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQSimpleConnectionManager;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonDateSerializer.
 */

public class PCFConnectionManagerHandler extends HttpServlet {

	/** The Constant logger. */
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PCFServiceImpl.class);

	/** The Constant CONFIG_PROPERTIES. */
	private static final String CONFIG_PROPERTIES = "/config.properties";

	/** The prop. */
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(PCFConnectionManagerHandler.class.getResourceAsStream(CONFIG_PROPERTIES));
			logger.debug("속성값=" + prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init() throws ServletException {

		try {

			// File file = new
			// File(System.getProperty("user.home")+"/pms/config.properties");
			//
			// BufferedInputStream bis = new BufferedInputStream(new
			// FileInputStream(file));
			//
			//
			// Properties prop = new Properties();
			//
			// prop.load(bis);

			// System.out.println("mq.pcf.hostname ::"+
			// prop.getProperty("mq.pcf.host"));
			// MQEnvironment.hostname = "14.63.216.249";
			MQEnvironment.hostname = prop.getProperty("mq.pcf.host");
			MQEnvironment.port = Integer.parseInt(prop.getProperty("mq.pcf.port"));
			MQEnvironment.channel = prop.getProperty("mq.pcf.channel");
			MQEnvironment.userID = prop.getProperty("mq.pcf.id");
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			// MQPoolToken token = MQEnvironment.addConnectionPoolToken();
			MQSimpleConnectionManager connMan = new MQSimpleConnectionManager();
			connMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
			connMan.setTimeout(3600000);
			connMan.setMaxConnections(Integer.parseInt(prop.getProperty("mq.pcf.maxconnections")));
			connMan.setMaxUnusedConnections(2);
	       

			MQEnvironment.setDefaultConnectionManager(connMan);

			// System.out.println("=== PCFConnectionManagerHandler Load OK
			// ===");

		} catch (Exception mqe) {
			System.err.println(mqe);
		}

	}

}
