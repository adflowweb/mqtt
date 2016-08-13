package kr.co.adflow.push.exception;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by nadir93 on 16. 8. 10..
 */
public class MQConnectException extends MqttException {

    public MQConnectException(int reasonCode) {
        super(reasonCode);
    }

    public MQConnectException(Throwable cause) {
        super(cause);
    }

    public MQConnectException(int reasonCode, Throwable cause) {
        super(reasonCode, cause);
    }
}

