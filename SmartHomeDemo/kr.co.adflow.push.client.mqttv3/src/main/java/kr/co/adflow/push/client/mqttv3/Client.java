package kr.co.adflow.push.client.mqttv3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import kr.co.adflow.push.client.mqttv3.db.Job;
import kr.co.adflow.push.client.mqttv3.db.PushDBHelper;
import kr.co.adflow.push.client.mqttv3.db.Worker;
import kr.co.adflow.push.client.mqttv3.handler.CallbackHandler;
import kr.co.adflow.push.client.mqttv3.handler.PingHandler;
import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;
import kr.co.adflow.push.client.mqttv3.ssl.ADFSSLSocketFactory;
import kr.co.adflow.push.client.mqttv3.util.DebugLog;
import kr.co.adflow.push.client.mqttv3.util.SharedPreferenceEntry;

/**
 * Created by nadir93 on 15. 10. 16..
 */
public class Client {

    private Context context = null;
    private MqttAsyncClient mqttClient = null;
    private String currentToken = null;
    private ArrayList<String> currentServerURIs = null;
    private int currentPort;
    private int currentKeepAlive;
    private boolean currentCleanSession = false;
    private boolean currentSSL = false;

    private PingHandler pingHandler = null;
    private PushDBHelper pushDBHelper = null;
    private Worker worker = null;


    /**
     * @param context
     */
    public Client(Context context) {
        DebugLog.d("Client 생성자 시작(Context=" + context + ")");
        //Log.d(TAG, "Client 생성자 시작(Context=" + context + ")");
        this.context = context;
        pushDBHelper = new PushDBHelper(context);
        //this.sharedPreferencesHelper = ((PushServiceImpl) context).getmSharedPreferencesHelper();
        //Log.d(TAG, "Client 생성자 종료()");
        DebugLog.d("Client 생성자 종료()");
    }

    /**
     * @return
     */
    public PushDBHelper getPushDBHelper() {
        return pushDBHelper;
    }

    /**
     * @return
     */
    public String getCurrentToken() {
        return currentToken;
    }

    /**
     * @return
     */
    public MqttAsyncClient getMqttClient() {
        return mqttClient;
    }

    /**
     * @return
     */
    public PingHandler getPingHandler() {
        return pingHandler;
    }

    /**
     * 모든 처리는 keepAlive를 통한다
     *
     * @return
     */
    public synchronized void keepAlive() {
        DebugLog.d("keepAlive 시작()");
        DebugLog.v("현재시간 : " + new Date());

        try {
            // mqtt 정보 가져오기
            SharedPreferenceEntry mqttInfo = ((PushServiceImpl) context).getmSharedPreferencesHelper().getMqttInfo();
            DebugLog.d("mqttInfo=" + mqttInfo);

            ArrayList<String> serverURIs = mqttInfo.getServerURIs();

            //String server = mqttInfo.getServer();
            //int port = mqttInfo.getPort();
            int keepAlive = mqttInfo.getKeepAlive();
            boolean cleanSession = mqttInfo.isCleanSession();
            //boolean ssl = mqttInfo.isSsl();
            String token = mqttInfo.getToken();
            DebugLog.d("token=" + token);

            // token 값이 없을 경우 종료한다
            if (token == null || token.equals("")) {
                DebugLog.e("토큰이 등록되어 있지 않습니다");
                //mqtt 중지
                stop();
            }

            DebugLog.d("currentServerURIs=" + currentServerURIs);
            DebugLog.d("currentToken=" + currentToken);
            DebugLog.d("currentKeepAlive=" + currentKeepAlive);
            DebugLog.d("currentCleanSession=" + currentCleanSession);
            DebugLog.d("serverURIsCompare=" + ((currentServerURIs != null) ? currentServerURIs.equals(serverURIs) : false));

            if (currentServerURIs == null || currentToken == null || !currentServerURIs.equals(serverURIs)
                    || !currentToken.equals(token) || currentKeepAlive != keepAlive
                    || Boolean.valueOf(currentCleanSession).compareTo(Boolean.valueOf(cleanSession)) != 0) {

                DebugLog.v("mqtt정보가 변경되었으므로 다시 시작합니다");
                //mqtt 중지
                stop();
                currentServerURIs = serverURIs;
                //currentPort = port;
                currentToken = token;
                currentKeepAlive = keepAlive;
                currentCleanSession = cleanSession;
                //currentSSL = ssl;
            }

            //mqtt 상태 확인
            if (mqttClient == null) {

                //String connectString = ((currentSSL) ? "ssl://" : "tcp://") + currentServer + ":" + currentPort;
                DebugLog.d("primaryServerURI=" + currentServerURIs.get(0));

                pingHandler = new PingHandler(context);
                mqttClient = new MqttAsyncClient(currentServerURIs.get(0), currentToken,
                        new MemoryPersistence(), pingHandler);
                // 연결
                start(mqttInfo);
            } else {
                DebugLog.i("mqttClient 연결상태="
                        + ((mqttClient.isConnected()) ? "이미 연결됨" : "끊어짐"));
                if (!mqttClient.isConnected()) {
                    // 연결
                    start(mqttInfo);
                }
            }

            // 할 일 처리
            if (worker == null) {
                worker = new Worker(this);
                worker.start();
                DebugLog.d("worker=" + worker);
            } else {
                synchronized (worker) {
                    DebugLog.d("worker를 깨웁니다");
                    worker.notify();
                }
            }

            // send ping
            new PingAsyncTask().execute();

            //testCode
            //Log.d(TAG, "서버URI=" + mqttClient.getServerURI());
            //testEnd
        }
        //catch (MqttException e) {
        // sendBroadcast
        //}
        catch (Exception e) {
            DebugLog.e("keepAlive 처리중 에러발생");
            e.printStackTrace();

            PowerManager.WakeLock lock = PushServiceImpl.getWakeLock();
            DebugLog.d("웨이크락=" + lock);

            if (lock != null && lock.isHeld()) {
                try {
                    lock.release();
                    DebugLog.d("웨이크락이 해제되었습니다");
                } catch (Exception ex) {
                    DebugLog.e("웨이크락 해제 처리중 에러발생");
                    e.printStackTrace();
                }
            } else {
                DebugLog.d("웨이크락이 없거나 이미 해제되었습니다");
            }
        }
        DebugLog.d("keepAlive 종료()");
    }

    /**
     * mqtt 세션 시작
     */
    public synchronized void start(SharedPreferenceEntry mqttInfo) throws Exception {
        DebugLog.d("start 시작(mqttInfo=" + mqttInfo + ")");

        if (mqttClient.isConnected()) {
            DebugLog.d("이미 연결되어 있습니다");
            return;
        }

        // 연결 옵션 생성
        MqttConnectOptions mOpts = new MqttConnectOptions();
        // mOpts.setUserName("testUser");
        // mOpts.setPassword("testPasswd".toCharArray());
        DebugLog.d("connectTimeout=" + BuildConfig.MQTT_CONNECT_TIMEOUT);
        mOpts.setConnectionTimeout(BuildConfig.MQTT_CONNECT_TIMEOUT);

        DebugLog.d("keepAlive=" + mqttInfo.getKeepAlive());
        mOpts.setKeepAliveInterval(mqttInfo.getKeepAlive());

        DebugLog.d("cleanSession=" + mqttInfo.isCleanSession());
        mOpts.setCleanSession(mqttInfo.isCleanSession());

        mOpts.setMqttVersion(BuildConfig.MQTT_VERSION);

        // ssl 처리
        if (currentServerURIs.get(0).startsWith("ssl://")) {
            mOpts.setSocketFactory(ADFSSLSocketFactory.getSSLSokcetFactory());
        }

        String[] serverURIs = new String[currentServerURIs.size()];
        mOpts.setServerURIs(currentServerURIs.toArray(serverURIs));

        DebugLog.d("연결옵션=" + mOpts);
        mqttClient.setCallback(new CallbackHandler(this));

        // 연결
        IMqttToken iMqttToken = mqttClient.connect(mOpts);
        // 비동기 기다림
        iMqttToken.waitForCompletion();

        DebugLog.d("mqttClient=" + mqttClient);
        DebugLog.i("연결상태=" + (mqttClient.isConnected() ? "연결됨" : "끊어짐"));

        if (mqttClient.isConnected()) {
            Intent i = new Intent("kr.co.adflow.push.status");
            i.putExtra("message", "푸시서버와 연결되었습니다");
            i.putExtra("code", 112100);
            DebugLog.d("event=" + i);
            ((PushServiceImpl) context).getBroadcaster().sendBroadcast(i);
        }
        DebugLog.d("start 종료()");
    }

    /**
     * mqtt 세션 종료
     */
    public synchronized void stop() {
        DebugLog.d("stop 시작()");
        DebugLog.d("mqttClient=" + mqttClient);

        if (mqttClient != null) {
            try {
                IMqttToken token = mqttClient.disconnect();
                token.waitForCompletion();
                mqttClient.close();

                Intent i = new Intent("kr.co.adflow.push.status");
                i.putExtra("message", "푸시서버 연결이 종료되었습니다");
                i.putExtra("code", 112101);
                DebugLog.d("event=" + i);
                ((PushServiceImpl) context).getBroadcaster().sendBroadcast(i);
            } catch (Exception e) {
                DebugLog.e("stop 처리중 에러발생");
                e.printStackTrace();

                // 어찌됐 건 여기서
                // 무조건 mqtt가 disconnect 및
                // 모든 리소스가 정리 돼야함
            }
            mqttClient = null;

            //worker thread 종료
            if (worker != null) {
                worker.discard();
                worker = null;
            }
        }
        DebugLog.d("stop 종료()");
    }

    /**
     * 토픽 구독
     *
     * @param topic
     * @param qos
     * @throws Exception
     */
    public synchronized void subscribe(String topic, int qos) throws Exception {
        DebugLog.d("subScribe 시작(토픽=" + topic + ", qos=" + qos + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("Mqtt 연결이 없거나 초기화되지 안았습니다");
        }

        // 토픽구독
        IMqttToken token = mqttClient.subscribe(topic, qos);
        token.waitForCompletion();
        DebugLog.d("토픽 구독을 완료하였습니다");
        DebugLog.d("subscribe 종료()");
    }

    /**
     * 토픽 구독 취소
     *
     * @param topic
     * @throws Exception
     */
    public synchronized void unsubscribe(String topic) throws Exception {
        DebugLog.d("unsubscribe 시작(토픽=" + topic + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("Mqtt 연결이 없거나 초기화되지 안았습니다");
        }

        // 토픽구독
        IMqttToken token = mqttClient.unsubscribe(topic);
        token.waitForCompletion();
        DebugLog.d("토픽 구독을 취소하였습니다");
        DebugLog.d("unsubscribe 종료()");
    }

    /**
     * 메시지 전송하기 SYNC
     *
     * @param topic
     * @param payload
     * @param qos
     * @param retained
     * @throws Exception
     */
    public synchronized void publish(String topic, byte[] payload, int qos,
                                     boolean retained) throws Exception {
        DebugLog.d("publish 시작(토픽=" + topic + ", payload=" + new String(payload)
                + ", qos=" + qos + ", retained=" + retained + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("Mqtt 연결이 없거나 초기화되지 안았습니다");
        }

        IMqttDeliveryToken token = mqttClient.publish(topic, payload, qos,
                retained);
        token.waitForCompletion();
        DebugLog.d("메시지 전송이 완료되었습니다");
        DebugLog.d("publish 종료()");
    }

    /**
     * 메시지 수신확인 전송
     *
     * @param msgId
     * @param ackTime
     * @throws Exception
     */
    public void ack(String msgId, Date ackTime, String ackType) throws Exception {
        DebugLog.d("ack 시작(msgId=" + msgId + ", ackTime=" + ackTime + ", ackType=" + ackType + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("Mqtt 연결이 없거나 초기화되지 안았습니다");
        }

        // add job
        JSONObject job = new JSONObject();
        job.put("msgId", msgId);
        job.put("token", currentToken);
        job.put("ackType", ackType);
        job.put("ackTime", ackTime.getTime());
        addJobAck(job);

//        JSONObject ack = new JSONObject();
//        ack.put("msgId", msgId);
//        ack.put("token", currentToken);
//        ack.put("ackType", ackType);
//        ack.put("ackTime", ackTime.getTime());
//        Log.d(TAG, "ack message=" + ack);
//
//        Message msg = pushDBHelper.getMessage(msgId);
//        if (msg.getAck() == 1 && msg.getAppAck() == 0) {
//            // ack
//            publish(BuildConfig.ACK_TOPIC, ack.toString().getBytes(), 1 /* qos */, false /* retained */);
//        }
//
//        // update message
//        int updateCnt = pushDBHelper.updateAppAck(msgId);
//        Log.d(TAG, "메시지 레코드(appack)를 변경하였습니다 updateCnt=" + updateCnt);
        DebugLog.d("ack 종료()");
    }

    /**
     * ping
     *
     * @throws Exception
     */
    public synchronized void ping() throws Exception {
        DebugLog.d("ping 시작()");
        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("Mqtt 연결이 없거나 초기화되지 안았습니다");
        }

        JSONObject ping = new JSONObject();
        ping.put("pingTime", new Date());
        ping.put("token", currentToken);
        DebugLog.d("ping message=" + ping);

        // ping
        publish(BuildConfig.PING_TOPIC, ping.toString().getBytes(), 1 /* qos */, false /* retained */);
        DebugLog.d("ping 종료()");
    }

    /**
     * mqtt 연결상태
     *
     * @return
     */
    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    /**
     * 토큰 가져오기
     *
     * @return
     */
    public String getToken() {
        return currentToken;
    }


    /**
     * 토픽 구독정보 가져오기 (REST)
     *
     * @return
     */
    public String getSubscriptions() throws Exception {
        DebugLog.d("getSubscriptions 시작()");

        SharedPreferenceEntry mqttInfo = ((PushServiceImpl) context).getmSharedPreferencesHelper().getMqttInfo();
        DebugLog.d("mqttInfo=" + mqttInfo);
        String token = mqttInfo.getToken();

        DebugLog.d("X-Application-Key=" + token);
        DebugLog.d("GET_SUBSCRIPTIONS_URL=" + BuildConfig.GET_SUBSCRIPTIONS_URL + token);
        HttpRequest request = HttpRequest.get(BuildConfig.GET_SUBSCRIPTIONS_URL + token)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-Application-Key", currentToken)
                .header("Content-Type", "application/json;charset=utf-8");

        DebugLog.d("responseCode=" + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("getSubscriptions 종료(response=" + response + ")");
        return response;
    }

    /**
     * ping asyncTask
     */
    private class PingAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DebugLog.d("PingAsyncTask 시작()");
            try {
                pingHandler.ping();
            } catch (Exception e) {
                DebugLog.e("PingAsyncTask 처리중 에러발생");
                e.printStackTrace();
            }
            DebugLog.d("PingAsyncTask 종료()");
            return null;
        }
    }

    /**
     * @param data
     * @throws Exception
     */
    public void addJobAck(JSONObject data) throws Exception {
        DebugLog.d("addJobAck 시작(data=" + data + ")");
        int job = getPushDBHelper().addJob(Job.ACK, data.toString());
        DebugLog.i("ack 작업이 추가되었습니다 jobId=" + job);
        DebugLog.d("addJobAck 종료()");
    }
}
