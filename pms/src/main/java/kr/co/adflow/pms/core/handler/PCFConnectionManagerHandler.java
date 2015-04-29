/*
 * 
 */
package kr.co.adflow.pms.core.handler;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.resource.spi.ConnectionManager;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
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
public class PCFConnectionManagerHandler  {

	
	public static void PCFConnectionManager() {
			try {
				MQEnvironment.hostname = "14.63.217.141";
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

			} catch (Exception mqe) {
				System.err.println(mqe);
			}

	}

}
