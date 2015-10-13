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

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQSimpleConnectionManager;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonDateSerializer.
 */

public class PCFConnectionManagerHandler extends HttpServlet {

	public void init() throws ServletException {

		try {

			File file = new File(System.getProperty("user.home")
					+ "/pms/config.properties");

			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));

			Properties prop = new Properties();

			prop.load(bis);

			// System.out.println("mq.pcf.hostname ::"+
			// prop.getProperty("mq.pcf.hostname"));
			// MQEnvironment.hostname = "14.63.216.249";
			MQEnvironment.hostname = prop.getProperty("mq.pcf.hostname");
			MQEnvironment.port = Integer.parseInt(prop
					.getProperty("mq.pcf.port"));
			MQEnvironment.channel = prop.getProperty("mq.pcf.channel");
			MQEnvironment.userID = prop.getProperty("mq.pcf.userID");
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			// MQPoolToken token = MQEnvironment.addConnectionPoolToken();
			MQSimpleConnectionManager connMan = new MQSimpleConnectionManager();
			connMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
			connMan.setTimeout(3600000);
			connMan.setMaxConnections(200);
			connMan.setMaxUnusedConnections(50);

			MQEnvironment.setDefaultConnectionManager(connMan);

			System.out.println("=== PCFConnectionManagerHandler Load OK ===");

		} catch (Exception mqe) {
			System.err.println(mqe);
		}

	}

}
