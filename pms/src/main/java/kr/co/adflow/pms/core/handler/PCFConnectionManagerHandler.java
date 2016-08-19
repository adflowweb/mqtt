/*
 * 
 */
package kr.co.adflow.pms.core.handler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.resource.spi.ConnectionManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import kr.co.adflow.pms.core.config.PmsConfig;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.pcf.PCFException;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonDateSerializer.
 */

public class PCFConnectionManagerHandler extends HttpServlet {

	public void init() throws ServletException {

		try {

			File file = new File(System.getProperty("user.home") + "/pms/config.properties");

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			Properties prop = new Properties();

			prop.load(bis);

			// System.out.println("mq.pcf.hostname ::"+
			// prop.getProperty("mq.pcf.hostname"));
			// MQEnvironment.hostname = "14.63.216.249";
			MQEnvironment.hostname = prop.getProperty("mq.pcf.hostname");
			MQEnvironment.port = Integer.parseInt(prop.getProperty("mq.pcf.port"));
			MQEnvironment.channel = prop.getProperty("mq.pcf.channel");
			MQEnvironment.userID = prop.getProperty("mq.pcf.userID");
			MQEnvironment.password = prop.getProperty("mq.pcf.password");

			// MQPoolToken token = MQEnvironment.addConnectionPoolToken();
			MQSimpleConnectionManager connMan = new MQSimpleConnectionManager();
			connMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
			connMan.setTimeout(3600000);
			connMan.setMaxConnections(50);
			connMan.setMaxUnusedConnections(2);

			MQEnvironment.setDefaultConnectionManager(connMan);

			// System.out.println("=== PCFConnectionManagerHandler Load OK
			// ===");

		} catch (Exception mqe) {
			System.err.println(mqe);
		}

	}

}