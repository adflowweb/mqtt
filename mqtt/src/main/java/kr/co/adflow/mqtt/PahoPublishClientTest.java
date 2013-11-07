package kr.co.adflow.mqtt;

import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PahoPublishClientTest implements MqttCallback {

	// static String url = "tcp://192.168.0.171:1883";
	static String url = "tcp://m2m.eclipse.org:1883";
	// static String url = "tcp://127.0.0.1:1883";
	static String clientId = "SampleJavaV3_publish";
	static String message = "Message from blocking MQTTv3 Java client sample";
	static String topicName = "test/topic";
	static int qos = 0;
	static boolean cleanSession = true;

	public static void main(String[] args) {

		// Construct an MQTT blocking mode client
		try {
			long start = System.currentTimeMillis();

			// Construct the connection options object that contains
			// connection
			// parameters
			// such as cleansession and LWAT
			MqttConnectOptions conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(cleanSession);

			// Construct an MQTT blocking mode client
			MqttClient client = new MqttClient(url, clientId, null);

			// Set this wrapper as the callback handler
			client.setCallback(new PahoPublishClientTest());

			// client.publish("test/topic", qos, message.getBytes());

			// Connect to the MQTT server
			System.out.println("Connecting to " + url + " with client ID "
					+ clientId);
			client.connect(conOpt);
			System.out.println("Connected");

			String time = new Timestamp(System.currentTimeMillis()).toString();
			System.out.println("Publishing at: " + time + " to topic \""
					+ topicName + "\" qos " + qos);

			// Create and configure a message
			MqttMessage msg = new MqttMessage(message.getBytes());
			msg.setQos(qos);

			// Send the message to the server, control is not returned until
			// it has been delivered to the server meeting the specified
			// quality of service.

			// for (int i = 0; i < 100000; i++) {

			client.publish(topicName, msg);
			// }

			// Disconnect the client
			client.disconnect();
			System.out.println("Disconnected");

			long stop = System.currentTimeMillis();
			System.out.println("elapsed time : " + (stop - start) + " ms");

		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void connectionLost(Throwable cause) {
		cause.printStackTrace();

	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		System.out.println("topic : " + topic);
		System.out.println("message : " + message);

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// System.out.println("token : " + token);

	}

}
