package kr.co.adflow.push.db;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.github.kevinsawicki.http.HttpRequest;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import kr.co.adflow.push.BuildConfig;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.util.DebugLog;
import kr.co.adflow.push.util.TRLogger;

/**
 * Created by nadir93 on 2016. 10. 14..
 */
public class Worker extends Thread {
    private Context context;
    private PushHandler handler;
    private MqttAsyncClient mqttClient;
    private PushPreference preference;
    private PushDBHelper pushdb;
    private boolean running = true;

    public Worker(Context context, PushHandler handler, MqttAsyncClient mqttClient, PushDBHelper pushdb) {
        this.mqttClient = mqttClient;
        this.pushdb = pushdb;
        this.handler = handler;
        this.context = context;
        this.preference = new PushPreference(context);
        DebugLog.d("preference = " + this.preference);
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

    public void cleanUpMqttSession(String mqttBrokerInfo)
            throws Exception {
        DebugLog.d("cleanUpMqttSession 시작(mqttBrokerInfo = " + mqttBrokerInfo + ")");
        String token = this.handler.getToken();
        DebugLog.d("token = " + token);
        HttpRequest request = HttpRequest.delete(BuildConfig.CLEANUP_MQTT_SESSION_URL + token);
        request.connectTimeout(BuildConfig.HTTTP_CONNECT_TIMEOUT);
        request.readTimeout(BuildConfig.HTTTP_READ_TIMEOUT);
        request.header("token", token);
        request.header("mqttBrokerInfo", mqttBrokerInfo);
        request.contentType("application/json");
        if (!request.ok()) {
            throw new IOException("Unexpected code " + request.code());
        }
        TRLogger.i("Worker", "[" + token + "] mqttSession 제거 작업이 수행되었습니다 mqttBrokerInfo = " + mqttBrokerInfo);
        DebugLog.d("cleanUpMqttSession 종료()");
    }

    public void postTRLog(String hostInfo)
            throws Exception {
        DebugLog.d("postTRLog 시작(hostInfo = " + hostInfo + ")");
        String token = this.handler.getToken();
        DebugLog.d("token = " + token);
        String phone = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        DebugLog.d("전화번호 = " + phone);
        HttpRequest request = HttpRequest.post(hostInfo + BuildConfig.GET_TRLOG_URL + phone);
        request.connectTimeout(BuildConfig.HTTTP_CONNECT_TIMEOUT);
        request.readTimeout(BuildConfig.HTTTP_READ_TIMEOUT);
        request.header("token", phone);
        File localFile1 = new File(this.handler.getContext().getFilesDir() + "/ADFTRLog0.log");
        if (localFile1.exists()) {
            request.part("file", "trlogA.log", localFile1);
        }
        File localFile2 = new File(this.handler.getContext().getFilesDir() + "/ADFTRLog1.log");
        if (localFile2.exists()) {
            request.part("file", "trlogB.log", localFile2);
        }
        File localFile3 = new File(this.handler.getContext().getFilesDir() + "/ADFErrLog0.log");
        if (localFile3.exists()) {
            request.part("file", "errLogA.log", localFile3);
        }
        File localFile4 = new File(this.handler.getContext().getFilesDir() + "/ADFErrLog1.log");
        if (localFile4.exists()) {
            request.part("file", "errLogB.log", localFile4);
        }
        if (!request.ok()) {
            throw new IOException("Unexpected code " + request.code());
        }
        DebugLog.d("postTRLog 종료()");
    }

    @Override
    public void run() {
        while (running) {
            try {
                DebugLog.d("worker 작업 시작");
                if (mqttClient != null && mqttClient.isConnected()) {
                    Job[] jobs = pushdb.getJobList();
                    DebugLog.d("jobs = " + jobs);
                    if (jobs != null) {
                        DebugLog.d("작업 해야 할 메시지 개수 = " + jobs.length);
                        for (int i = 0; i < jobs.length; i++) {
                            int jobType = jobs[i].getType();
                            switch (jobType) {
                                case BuildConfig.JOB_PUBLISH: // PUBLISH
                                    break;
                                case BuildConfig.JOB_SUBSCRIBE:  // SUBSCRIBE
                                    DebugLog.d("SUBSCRIBE 작업 시작");
                                    String topic = jobs[i].getTopic();
                                    DebugLog.d("topic=" + topic);
                                    handler.subscribe(topic, 2/* qos 2 */);
                                    pushdb.deteletJob(jobs[i].getId());
                                    DebugLog.i("SUBSCRIBE 작업이 수행되었습니다 jobID = " + jobs[i].getId());
                                    break;
                                case BuildConfig.JOB_UNSUBSCRIBE: // UNSUBSCRIBE
                                    DebugLog.d("UNSUBSCRIBE 작업 시작");
                                    String unscribeTopic = jobs[i].getTopic();
                                    DebugLog.d("topic=" + unscribeTopic);
                                    handler.unsubscribe(unscribeTopic);
                                    pushdb.deteletJob(jobs[i].getId());
                                    DebugLog.i("UNSUBSCRIBE 작업이 수행되었습니다 jobID = " + jobs[i].getId());
                                    break;
                                case BuildConfig.JOB_BROADCAST: // BROADCAST
                                    DebugLog.d("브로드캐스트 작업 시작");
                                    String content = jobs[i].getContent();
                                    JSONObject data = new JSONObject(content);
                                    String msgId = data.getString("msgId");
                                    Message msg = pushdb.getMessage(msgId);
                                    // sendbroadcast
                                    handler.sendBroadcast(new JSONObject(new String(msg.getPayload())), msg.getToken(), msg.getMsgID());
                                    pushdb.deteletJob(jobs[i].getId());
                                    DebugLog.d("브로드캐스트 작업이 수행되었습니다 jodID = " + jobs[i].getId());
                                    break;
                                case BuildConfig.JOB_ACK: //ACK
                                    DebugLog.d("ACK 작업 시작");
                                    String ackContent = jobs[i].getContent();
                                    data = new JSONObject(ackContent);
                                    msgId = data.getString("msgId");
                                    handler.publish(PushHandler.ACK_TOPIC, ackContent.getBytes(), 1 /* qos 2 로 전송 */, false /* retain */);
                                    pushdb.deteletJob(jobs[i].getId());
                                    //delete msg
                                    pushdb.deleteMessage(msgId);
                                    DebugLog.i("ACK 작업이 수행되었습니다 jobID = " + jobs[i].getId());
                                    break;
                                case BuildConfig.JOB_GET_TRLOG: //trlog
                                    //// TODO: 2016. 10. 15.
                                    // 수정해야함
                                    DebugLog.d("TRLOG 작업 시작");
                                    String trlogContent = jobs[i].getContent();
                                    data = new JSONObject(trlogContent);
                                    String hostInfo = data.getString("hostInfo");
                                    postTRLog(hostInfo);
                                    pushdb.deteletJob(jobs[i].getId());
                                    DebugLog.i("TRLOG 작업이 수행되었습니다 jobID = " + jobs[i].getId());
                                    break;
                                case BuildConfig.JOB_CHANGE_MQTT_CHANNEL: //change channel
                                    DebugLog.d("MQTT 채널 변경 작업 시작");
                                    String changeChannelontent = jobs[i].getContent();
                                    data = new JSONObject(changeChannelontent);
                                    //String mqttCluster = data.getString("mqttCluster");
                                    String serverURL = preference.getValue("serverURL", null);
                                    handler.stop();
                                    cleanUpMqttSession(serverURL);
                                    preference.put("serverURL", null);
                                    pushdb.deteletJob(jobs[i].getId());
                                    DebugLog.i("MQTT 채널 변경 작업이 수행되었습니다 jobID = " + jobs[i].getId());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                DebugLog.d("worker 작업 종료");
            } catch (Exception e) {
                DebugLog.e("예외 상황 발생", e);
            }

            synchronized (this) {
                DebugLog.d("worker가 wait상태로 전환됩니다");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    DebugLog.e("예외 상황 발생", e);
                }
            }
        }
        DebugLog.d("worker 쓰레드가 종료됩니다");
    }
}
