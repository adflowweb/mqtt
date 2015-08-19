package kr.co.adflow.pms.core.controller;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

public class TestMQTT {

	public static void main(String[] args) {
		MQQueueManager qmgr = null;
		try {
			MQEnvironment.hostname = "192.168.0.10";
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

			qmgr = new MQQueueManager("MQTT",
					MQEnvironment.getDefaultConnectionManager());

			PCFMessageAgent agent = new PCFMessageAgent(qmgr);
			// PCFMessage request = new
			// PCFMessage(MQConstants.MQCMD_INQUIRE_SUB_STATUS);
			// request.addParameter(MQConstants.MQCACF_SUB_NAME, "*");
			//
			// request.addFilterParameter(MQConstants.MQCA_TOPIC_STRING,MQConstants.MQCFOP_EQUAL,
			// "mms/P1/82/200/g38");
			//
			// long start;
			// long stop;
			// for (int j = 0; j < 1; j++) {
			// start = System.currentTimeMillis();
			// PCFMessage[] responses = agent.send(request);
			// stop = System.currentTimeMillis();
			// System.out.println("elapsedTime=" + (stop - start) + "ms");
			//
			// for (int i = 0; i < responses.length; i++) {
			// System.out.println(responses[i].getParameterValue(MQConstants.MQCACF_SUB_NAME));
			// }
			// }

			PCFMessage request = new PCFMessage(
					MQConstants.MQCMD_INQUIRE_Q);
			request.addParameter(MQConstants.MQCA_Q_NAME, "SYSTEM.MQTT.TRANSMIT.QUEUE");
//			request.addParameter(MQConstants.MQIACH_CHANNEL_TYPE,
//					MQConstants.MQCHT_MQTT);
//			//
			// request.addFilterParameter(MQConstants.MQCA_TOPIC_STRING,
			// MQConstants.MQCFOP_EQUAL, "/test");

			long start;
			long stop;
			for (int j = 0; j < 1; j++) {
				start = System.currentTimeMillis();
				PCFMessage[] responses = agent.send(request);
				stop = System.currentTimeMillis();
				System.out.println("elapsedTime=" + (stop - start) + "ms");

				System.out.println("client count =" + responses.length);
				for (int i = 0; i < responses.length; i++) {
					System.out
							.println(responses[i].getParameterValue(MQConstants.MQIA_CURRENT_Q_DEPTH));
				}
			}

			// qmgr = ;
			// System.out.println("qmgrStarted");

			// for (int i = 0; i < 20; i++) {
			// Sender sender = new Sender(new MQQueueManager("MQTT", connMan));
			// sender.start();
			// }

			// } catch (PCFException pcfe) {
			// if (pcfe.reasonCode == 2428) { // 2428 0x0000097c
			// // MQRC_NO_SUBSCRIPTION
			// System.err.println("No subscriptions.");
			// } else {
			// System.err.println("PCF error: " + pcfe);
			// }
			// } catch (MQException mqe) {
			// System.err.println(mqe);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// finally {
		// try {
		// qmgr.disconnect();
		// } catch (MQException e) {
		// e.printStackTrace();
		// }
		// }

	}

}

// class Sender extends Thread {
//
// private MQQueueManager qmgr = null;
//
// public Sender(MQQueueManager qmgr) {
// this.qmgr = qmgr;
// }
//
// @Override
// public void run() {
// try {
// System.out.println("qmgr=" + qmgr);
// PCFMessageAgent agent = new PCFMessageAgent(qmgr);
// PCFMessage request = new PCFMessage(
// MQConstants.MQCMD_INQUIRE_SUB_STATUS);
// request.addParameter(MQConstants.MQCACF_SUB_NAME, "*");
//
// request.addFilterParameter(MQConstants.MQCA_TOPIC_STRING,
// MQConstants.MQCFOP_EQUAL, "rcs/82");
//
// long start;
// long stop;
// for (int j = 0; j < 1; j++) {
// start = System.currentTimeMillis();
// PCFMessage[] responses = agent.send(request);
// stop = System.currentTimeMillis();
// System.out.println("elapsedTime=" + (stop - start) + "ms");
//
// for (int i = 0; i < responses.length; i++) {
// System.out.println(responses[i]
// .getParameterValue(MQConstants.MQCACF_SUB_NAME));
// }
// }
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }