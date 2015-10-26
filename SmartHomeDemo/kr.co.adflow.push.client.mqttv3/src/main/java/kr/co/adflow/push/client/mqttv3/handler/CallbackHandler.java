/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.handler;

import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Date;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.Client;
import kr.co.adflow.push.client.mqttv3.db.Job;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

/**
 * Created by nadir93 on 15. 10. 16..
 */
public class CallbackHandler implements MqttCallback {

    // Logger for this class.
    private static final String TAG = "CallbackHandler";

    private Client client;

    public CallbackHandler(Client client) {

        this.client = client;
    }

    /**
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        Log.d(TAG, "connectionLost 시작(throwable=" + throwable + ")");
        Log.e(TAG, "mqtt 연결이 종료되었습니다", throwable);

        try {
            Intent i = new Intent("kr.co.adflow.push.status");
            i.putExtra("message", "푸시서버 연결이 유실되었습니다");
            i.putExtra("code", 112103);
            Log.d(TAG, "event=" + i);

            PushServiceImpl.getInstance().getBroadcaster().sendMessage(i);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "connectionLost 처리중 에러발생", e);
        }

        // TODO: 15. 10. 22.
        // 시간을 늘려가며 슬립을 주는 로직이 필요할 듯
        // 아니면 무한 루프 돌 수 있음
        client.keepAlive();
        Log.d(TAG, "connectionLost 종료()");
    }

    /**
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "messageArrived 시작(topic=" + topic + ", message=" + message + ")");

        try {
            JSONObject data = new JSONObject(new String(message.getPayload()));
            Log.d(TAG, "data=" + data);

            // message 분기처리
            int msgType = data.getInt("msgType");
            String serviceId = data.getString("serviceId");
            String msgId = data.getString("msgId");

            int ack = 0;
            if (data.has("ack")) {
                ack = data.getInt("ack");
            }

            // 받은 메시지 저장
            client.getPushDBHelper().addMessage(msgId, serviceId, topic, message.getPayload(),
                    message.getQos(), ack, client.getCurrentToken());

            if (ack == 1) {
                // add job
                JSONObject job = new JSONObject();
                job.put("msgId", msgId);
                job.put("token", client.getCurrentToken());
                job.put("ackType", "agent");
                job.put("ackTime", System.currentTimeMillis());
                addJobAck(job);

                // ack sync
                // client.ack(msgId, new Date(), "agent");
            }

            switch (msgType) {
                case BuildConfig.SET_KEEP_ALIVE_MESSAGE: // keepAlive 설정변경
                    JSONObject content = data.getJSONObject("content");
                    Log.d(TAG, "content=" + content);
                    int keepAlive = content.getInt("keepAlive");

                    // keepAlive 저장
                    PushServiceImpl.getInstance().getmSharedPreferencesHelper().saveKeepAlive(keepAlive);
                    break;
                case BuildConfig.USER_MESSAGE:
                    // broadcast
                    sendBroadcast(data, client.getCurrentToken(), msgId);
                    break;
                default:
                    Log.e(TAG, "메시지타입이 존재하지 않습니다");
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "messageArrived 처리중 에러발생", e);
        }
        Log.d(TAG, "messageArrived 종료()");
    }

    /**
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d(TAG, "deliveryComplete 시작(iMqttDeliveryToken=" + iMqttDeliveryToken + ")");
        Log.d(TAG, "deliveryComplete 종료()");
    }

    /**
     * @param data
     * @throws Exception
     */
    public void addJobAck(JSONObject data) throws Exception {
        Log.d(TAG, "addJobAck 시작(data=" + data + ")");
        int job = client.getPushDBHelper().addJob(Job.ACK, data.toString());
        Log.i(TAG, "ack 작업이 추가되었습니다 jobId=" + job);
        Log.d(TAG, "addJobAck 종료()");
    }

    /**
     * @param data
     * @param token
     * @param msgId
     * @throws Exception
     */
    private void sendBroadcast(JSONObject data, String token, String msgId) throws Exception {
        Log.d(TAG, "sendBroadcast 시작(data=" + data + ", token=" + token + ", msgId=" + msgId + ")");
        String serviceId = data.getString("serviceId");
        Log.d(TAG, "serviceId=" + serviceId);

        Intent i = new Intent(serviceId);
        i.putExtra("msgId", msgId);
        i.putExtra("token", token);
        i.putExtra("contentType", data.getString("contentType"));
        i.putExtra("content", data.getString("content"));
        i.putExtra("sender", data.getString("sender"));
        i.putExtra("receiver", data.getString("receiver"));

        Log.d(TAG, "intent=" + i);
        PushServiceImpl.getInstance().getBroadcaster().sendMessage(i);
        Log.d(TAG, "sendBroadcast 종료()");
    }
}
