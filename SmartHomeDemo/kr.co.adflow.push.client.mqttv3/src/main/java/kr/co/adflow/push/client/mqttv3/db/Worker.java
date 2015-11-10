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
import kr.co.adflow.push.client.mqttv3.util.DebugLog;
import kr.co.adflow.push.client.mqttv3.util.TRLogger;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class Worker extends Thread {

    private static final String TAG = "Worker";

    private boolean running = true;
    private Client client = null;
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();

    /**
     * @param client
     */
    public Worker(Client client) {
        DebugLog.d("Worker 생성자 시작(client=" + client + ")");
        this.client = client;
        DebugLog.d("Worker 생성자 종료()");
    }

    public void discard() {
        DebugLog.d("discard 시작()");
        running = false;
        synchronized (this) {
            DebugLog.d("worker를 깨웁니다");
            this.notify();
        }
        DebugLog.d("discard 종료()");
    }

    @Override
    public void run() {
        while (running) {
            try {
                DebugLog.d("worker 작업 시작");
                if (client.getMqttClient() != null && client.getMqttClient().isConnected()) {

                    // job 처리
                    // 소요시간 측정 필요
                    long start = System.currentTimeMillis();
                    Job[] jobList = client.getPushDBHelper().getJobList();
                    DebugLog.d("jobList=" + jobList);
                    if (jobList != null) {
                        DebugLog.d("작업 해야할 메시지 개수=" + jobList.length);
                        for (int i = 0; i < jobList.length; i++) {
                            int jobType = jobList[i].getType();
                            switch (jobType) {
                                case 4: // Job.ACK
                                    DebugLog.d("ACK 작업 시작");
                                    String content = jobList[i].getContent();
                                    JSONObject data = new JSONObject(content);
                                    String msgId = data.getString("msgId");
                                    String ackType = data.getString("ackType");
                                    client.publish(BuildConfig.ACK_TOPIC, content.getBytes(), 1 /* qos */, false /* retain */);

                                    if (ackType.equals("agent")) {
                                        // update message
                                        int updateCnt = client.getPushDBHelper().updateAgentAck(msgId);
                                        DebugLog.d("메시지 레코드(agentack)를 변경하였습니다 updateCnt=" + updateCnt);
                                    } else {
                                        // update message
                                        int updateCnt = client.getPushDBHelper().updateAppAck(msgId);
                                        DebugLog.d("메시지 레코드(appack)를 변경하였습니다 updateCnt=" + updateCnt);
                                    }

                                    // delete job
                                    client.getPushDBHelper().deteletJob(jobList[i].getId());

                                    //delete msg
                                    //client.getPushDBHelper().deleteMessage(msgId);

                                    DebugLog.i("ACK 작업이 수행되었습니다 jobId=" + jobList[i].getId());

                                    //트랜잭션 저장
                                    //메시지 수신 트랜잭션 (ack 완료)
                                    TRLogger.i(TAG, "[" + msgId + "] 메시지 " + ackType + " ack 완료");
                                    break;
                                default:
                                    DebugLog.e("jobType이 존재하지 않습니다");
                                    break;
                            }
                        }
                    }
                    DebugLog.i("Job처리 소요시간=" + (System.currentTimeMillis() - start) + "ms");

                    // 메시지 테이블 appack가 zero인 것을 다시 브로드캐스팅
                    // 소요시간 측정 필요
                    start = System.currentTimeMillis();
                    Message[] msgList = client.getPushDBHelper().getBroadcastList();
                    DebugLog.d("msgList=" + msgList);

                    if (msgList != null) {
                        cal.setTime(new Date());
                        // 1분 이 전
                        cal.add(Calendar.MINUTE, -1);
                        DebugLog.d("브로드캐스팅 기준시간=" + cal.getTime());
                        DebugLog.d("브로드캐스팅 해야할 메시지 개수=" + msgList.length);
                        for (int i = 0; i < msgList.length; i++) {
                            DebugLog.d("msgList[" + i + "]=" + msgList[i]);
                            Date receivedate = transFormat.parse(msgList[i].getReceivedate());
                            // 받은 시간이 1분 이 후 데이타만 브로드캐스팅
                            if (msgList[i].getAppAck() == 0 && receivedate.before(cal.getTime())) {
                                JSONObject data = new JSONObject(new String(msgList[i].getPayload()));
                                DebugLog.d("브로드캐스팅 메시지=" + data);
                                // broadcast
                                // sendBroadcast(data, client.getCurrentToken(), msgList[i].getMsgId());

                                String serviceId = data.getString("serviceId");
                                DebugLog.d("serviceId=" + serviceId);

                                Intent intent = new Intent(serviceId);
                                intent.putExtra("msgId", msgList[i].getMsgId());
                                intent.putExtra("token", client.getCurrentToken());
                                intent.putExtra("contentType", data.getString("contentType"));
                                intent.putExtra("content", data.getString("content"));
                                intent.putExtra("sender", data.getString("sender"));
                                intent.putExtra("receiver", data.getString("receiver"));

                                DebugLog.d("intent=" + i);
                                PushServiceImpl.getInstance().getBroadcaster().sendBroadcast(intent);
                            }
                        }
                    }
                    DebugLog.i("BroadCast처리 소요시간=" + (System.currentTimeMillis() - start) + "ms");

                    // TODO: 15. 10. 22.
                    // 오래된 메시지 삭제 기능 ex) 한달이전 메시지 삭제?
                }
                DebugLog.d("worker 작업 종료");
            } catch (Exception e) {
                DebugLog.e("worker 작업중 에러발생");
                e.printStackTrace();
            }

            synchronized (this) {
                DebugLog.d("worker가 wait상태로 전환됩니다");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    DebugLog.e("wait상태로 전환중 에러발생");
                    e.printStackTrace();
                }
            }
        }
        DebugLog.d("worker 쓰레드가 종료됩니다");
    }
}
