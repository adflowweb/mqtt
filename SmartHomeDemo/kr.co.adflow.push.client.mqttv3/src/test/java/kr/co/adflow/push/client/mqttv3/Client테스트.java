/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

import kr.co.adflow.push.client.mqttv3.handler.CallbackHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nadir93 on 15. 10. 16..
 */
public class Client테스트 implements MqttCallback {

    // TODO: 15. 10. 17.
    // 임의 핑 전송 테스트
    // 보안채널 연결 테스트
    // mqtt 연결 정보 변경시 자동 재연결 테스트


    @Test
    public void MqttClient연결테스트() throws Exception {
        System.out.println("connectMqttClient 시작()==============");
        int keepAlive = 60;
        int connectTimeout = 60;
        boolean cleanSession = false;
        String connString = "tcp://112.223.76.75:11883";
        String token = "12345678901234567890123";

        MqttClient mqttClient = new MqttClient(connString, token);

        MqttConnectOptions mOpts = new MqttConnectOptions();
        // mOpts.setUserName("testUser");
        // mOpts.setPassword("testPasswd".toCharArray());
        System.out.println("connectTimeout=" + connectTimeout);
        mOpts.setConnectionTimeout(connectTimeout);
        System.out.println("keepAlive=" + keepAlive);
        mOpts.setKeepAliveInterval(keepAlive);
        mOpts.setCleanSession(cleanSession);
        System.out.println("연결옵션=" + mOpts);
        mqttClient.setCallback(this);
        mqttClient.connect(mOpts);
        System.out.println("mqttClient=" + mqttClient);
        System.out.println("연결상태=" + mqttClient.isConnected());

        assertTrue(mqttClient.isConnected());
        mqttClient.disconnect();
        mqttClient.close();
        System.out.println("connectMqttClient 종료()==============");
    }

    @Test
    public void MqttAsyncClient연결테스트() throws Exception {
        System.out.println("connectMqttAsyncClient 시작()==============");
        int keepAlive = 60;
        int connectTimeout = 60;
        boolean cleanSession = false;
        String connString = "tcp://112.223.76.75:11883";
        String token = "12345678901234567890123";

        MqttAsyncClient mqttClient = new MqttAsyncClient(connString, token);
        MqttConnectOptions mOpts = new MqttConnectOptions();
        // mOpts.setUserName("testUser");
        // mOpts.setPassword("testPasswd".toCharArray());
        System.out.println("connectTimeout=" + connectTimeout);
        mOpts.setConnectionTimeout(connectTimeout);
        System.out.println("keepAlive=" + keepAlive);
        mOpts.setKeepAliveInterval(keepAlive);
        mOpts.setCleanSession(cleanSession);
        System.out.println("연결옵션=" + mOpts);
        mqttClient.setCallback(this);
        IMqttToken iMqttToken = mqttClient.connect(mOpts);
        iMqttToken.waitForCompletion();
        System.out.println("mqttClient=" + mqttClient);
        System.out.println("연결상태=" + mqttClient.isConnected());
        assertTrue(mqttClient.isConnected());
        mqttClient.disconnect();
        mqttClient.close();
        System.out.println("connectMqttAsyncClient 종료()==============");
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("connectionLost 시작(throwable=" + throwable + ")");
        System.out.println("connectionLost 종료()");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("messageArrived 시작(topic=" + topic + ", mqttMessage=" + mqttMessage + ")");
        System.out.println("messageArrived 종료()");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete 시작(iMqttDeliveryToken=" + iMqttDeliveryToken + ")");
        System.out.println("deliveryComplete 종료()");
    }
}
