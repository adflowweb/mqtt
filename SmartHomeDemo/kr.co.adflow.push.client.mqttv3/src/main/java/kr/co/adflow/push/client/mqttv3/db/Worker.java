/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.db;

import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.co.adflow.push.client.mqttv3.BuildConfig;
import kr.co.adflow.push.client.mqttv3.Client;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class Worker extends Thread {

    // Logger for this class.
    private static final String TAG = "Worker";
    private boolean running = true;
    private Client client = null;
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();

    /**
     * @param client
     */
    public Worker(Client client) {
        Log.d(TAG, "Worker 생성자 시작(client=" + client + ")");
        this.client = client;
        Log.d(TAG, "Worker 생성자 종료()");
    }

    public void discard() {
        Log.d(TAG, "discard 시작()");
        running = false;
        synchronized (this) {
            Log.d(TAG, "worker를 깨웁니다");
            this.notify();
        }
        Log.d(TAG, "discard 종료()");
    }

    @Override
    public void run() {
        while (running) {
            try {
                Log.d(TAG, "worker 작업 시작");
                if (client.getMqttClient() != null && client.getMqttClient().isConnected()) {

                    // job 처리
                    // 소요시간 측정 필요
                    Job[] jobList = client.getPushDBHelper().getJobList();
                    Log.d(TAG, "jobList=" + jobList);
                    if (jobList != null) {
                        Log.d(TAG, "작업 해야할 메시지 개수=" + jobList.length);
                        for (int i = 0; i < jobList.length; i++) {
                            int jobType = jobList[i].getType();
                            switch (jobType) {
                                case 4: // Job.ACK
                                    Log.d(TAG, "ACK 작업 시작");
                                    String content = jobList[i].getContent();
                                    JSONObject data = new JSONObject(content);
                                    String msgId = data.getString("msgId");
                                    String ackType = data.getString("ackType");
                                    client.publish(BuildConfig.ACK_TOPIC, content.getBytes(), 1 /* qos */, false /* retain */);

                                    if (ackType.equals("agent")) {
                                        // update message
                                        int updateCnt = client.getPushDBHelper().updateAgentAck(msgId);
                                        Log.d(TAG, "메시지 레코드(agentack)를 변경하였습니다 updateCnt=" + updateCnt);
                                    } else {
                                        // update message
                                        int updateCnt = client.getPushDBHelper().updateAppAck(msgId);
                                        Log.d(TAG, "메시지 레코드(appack)를 변경하였습니다 updateCnt=" + updateCnt);
                                    }

                                    // delete job
                                    client.getPushDBHelper().deteletJob(jobList[i].getId());

                                    //delete msg
                                    //client.getPushDBHelper().deleteMessage(msgId);

                                    Log.i(TAG, "ACK 작업이 수행되었습니다 jobId=" + jobList[i].getId());
                                    break;
                                default:
                                    Log.e(TAG, "jobType이 존재하지 않습니다");
                                    break;
                            }
                        }
                    }

                    // 메시지 테이블 appack가 zero인 것을 다시 브로드캐스팅
                    // 소요시간 측정 필요
                    Message[] msgList = client.getPushDBHelper().getBroadcastList();
                    Log.d(TAG, "msgList=" + msgList);

                    if (msgList != null) {
                        cal.setTime(new Date());
                        // 1분 이 전
                        cal.add(Calendar.MINUTE, -1);
                        Log.d(TAG, "브로드캐스팅 기준시간=" + cal.getTime());
                        Log.d(TAG, "브로드캐스팅 해야할 메시지 개수=" + msgList.length);
                        for (int i = 0; i < msgList.length; i++) {
                            Log.d(TAG, "msgList[" + i + "]=" + msgList[i]);
                            Date receivedate = transFormat.parse(msgList[i].getReceivedate());
                            // 받은 시간이 1분 이 후 데이타만 브로드캐스팅
                            if (msgList[i].getAppAck() == 0 && receivedate.before(cal.getTime())) {
                                JSONObject data = new JSONObject(new String(msgList[i].getPayload()));
                                Log.d(TAG, "data=" + data);
                                // broadcast
                                sendBroadcast(data, client.getCurrentToken(), msgList[i].getMsgId());
                            }
                        }
                    }

                    // TODO: 15. 10. 22.
                    // 오래된 메시지 삭제 기능 ex) 한달이전 메시지 삭제?
                }
                Log.d(TAG, "worker 작업 종료");
            } catch (Exception e) {
                Log.e(TAG, "worker 작업중 에러발생", e);
            }

            synchronized (this) {
                Log.d(TAG, "worker가 wait상태로 전환됩니다");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "wait상태로 전환중 에러발생", e);
                }
            }
        }
        Log.d(TAG, "worker 쓰레드가 종료됩니다");
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
