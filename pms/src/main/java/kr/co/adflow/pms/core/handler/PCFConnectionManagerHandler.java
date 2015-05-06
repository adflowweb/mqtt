/*
 * 
 */
package kr.co.adflow.pms.core.handler;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
//@Component
public class PCFConnectionManagerHandler extends HttpServlet {
	
	/** The Pms Config. */
	@Autowired
	private PmsConfig pmsConfig;

	
	public void init() throws ServletException {
		try {
			
//			MQEnvironment.hostname = pmsConfig.MQ_PCF_HOSTNAME;
//			MQEnvironment.port = pmsConfig.MQ_PCF_PORT;
//			MQEnvironment.channel = pmsConfig.MQ_PCF_CHANNEL;
//			MQEnvironment.userID = pmsConfig.MQ_PCF_USERID;
//			MQEnvironment.password = pmsConfig.MQ_PCF_PASSWORD;
			
			
			MQEnvironment.hostname = "14.63.216.249";
//			MQEnvironment.hostname = "14.63.217.141";
			MQEnvironment.port = 1414;
			MQEnvironment.channel = "ADFlowPCF";
			MQEnvironment.userID = "adflow";
			MQEnvironment.password = "!ADFlow@";
			

			// MQPoolToken token = MQEnvironment.addConnectionPoolToken();
			MQSimpleConnectionManager connMan = new MQSimpleConnectionManager();
			connMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
			connMan.setTimeout(3600000);
			connMan.setMaxConnections(50);
			connMan.setMaxUnusedConnections(2);
			
			MQEnvironment.setDefaultConnectionManager(connMan);
			
			System.out.println("=== PCFConnectionManagerHandler Load OK ===");

		} catch (Exception mqe) {
			System.err.println(mqe);
		}

	}

}
