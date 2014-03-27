package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingReq;

import android.util.Log;

public class MqttClientWithPing extends MqttClient {

	// Debug TAG
	public static final String DEBUGTAG = "MqttClient(핑되는버전)";

	public MqttClientWithPing(String serverURI, String clientId,
			MqttClientPersistence persistence) throws MqttException {
		super(serverURI, clientId, persistence);
	}

	public MqttDeliveryToken sendPING() throws MqttException {
		Log.d(DEBUGTAG, "sendPING시작()");
		MqttDeliveryToken token = new MqttDeliveryToken(getClientId());
		MqttPingReq pingMsg = new MqttPingReq();
		aClient.comms.getClientState().send(pingMsg, token);
		// aClient.comms.sendNoWait(pingMsg, token);
		Log.d(DEBUGTAG, "sendPING종료(토큰=" + token + ")");
		return token;
	}

}
