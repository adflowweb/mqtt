package kr.co.adflow.push.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.Settings.Secure;

import com.github.kevinsawicki.http.HttpRequest;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import kr.co.adflow.push.BuildConfig;
import kr.co.adflow.push.TRLogger;
import kr.co.adflow.push.PingSender;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.db.PushDBHelper;
import kr.co.adflow.push.db.Worker;
import kr.co.adflow.push.exception.MQConnectException;
import kr.co.adflow.push.exception.ServerInfoException;
import kr.co.adflow.push.receiver.PushReceiver;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.push.util.DebugLog;
import kr.co.adflow.push.util.ErrLogger;
import kr.co.adflow.ssl.ADFSSLSocketFactory;

/**
 * @author nadir93
 */
public class PushHandler implements MqttCallback {

    // TAG for debug
    public static final String TAG = "PushHandler";

    public static final String ACK_TOPIC = "mms/ack";
    public static PushDBHelper pushdb;

    public static final int FIRST_MQTT_CONNECTED = 1000;
    public static final String FIRST_MQTT_CONNECTED_MESSAGE = "초기 MQTT연결이 성공하였습니다";
    public static final int MQTT_CONNECTED = 1001;
    public static final String MQTT_CONNECTED_MESSAGE = "MQTT연결이 성공하였습니다";
    public static final int MQTT_DISCONNECTED = 1002;
    public static final String MQTT_DISCONNECTED_MESSAGE = "MQTT연결이 유실되었습니다";
    public static final int MQTT_CHANNEL_CHANGED = 1003;
    public static final String MQTT_CHANNEL_CHANGED_MESSAGE = "MQTT채널이 변경되었습니다";
    public static final int MQTT_TOKEN_CHANGED = 1004;
    public static final String MQTT_TOKEN_CHANGED_MESSAGE = "MQTT토큰이 변경되었습니다";

    private static final String MQTT_PACKAGE = "org.eclipse.paho.client.mqttv3";
    private static final int MQTTVERSION_3 = 3;
    private static int CONN_LOST_COUNT;
    private static Context context;
    private static Worker worker;

    private MqttAsyncClient mqttClient;
    private PushPreference preference;
    private PingSender pingSender;
    private String currentToken = null;
    private int connectRetry = 0;
    private boolean serverConnectToggle = true;

    /**
     * @param cxt
     */
    public PushHandler(Context cxt) {
        DebugLog.d("PushHandler 생성자 시작(context = " + cxt + ")");
        context = cxt;
        pushdb = new PushDBHelper(context);
        DebugLog.d("pushdb = " + pushdb);
        DebugLog.d("Handler = " + this);
        if (BuildConfig.CLIENT_SESSION_DEBUG) {
            setMqttClientLog();
        }
        preference = new PushPreference(context);
        DebugLog.d("preference = " + preference);
        DebugLog.d("PushHandler 생성자 종료()");
    }

    public Context getContext() {
        return context;
    }

    /**
     * 푸시핸들러시작
     * <p/>
     * 시작이 호출되면 기존 채널을 중지후 다시 시작한다.
     */
    public void start() {
        DebugLog.d("start 시작()");
        stop();
        keepAlive();
        DebugLog.d("start 종료()");
    }

    /**
     * 푸시핸들러종료
     */
    public void stop() {
        DebugLog.d("stop 시작()");
        // disconnect mqtt session
        DebugLog.d("mqttClient = " + mqttClient);
        if (mqttClient != null) {
            if (mqttClient.isConnected()) {
                try {
                    IMqttToken token = mqttClient.disconnect();
                    token.waitForCompletion();
                } catch (Exception e) {
                    DebugLog.e("에러 발생", e);
                    ErrLogger.e(TAG, "에러 발생", e);
                }
            }

            try {
                mqttClient.close();
            } catch (Exception e) {
                DebugLog.e("에러 발생", e);
                ErrLogger.e(TAG, "에러 발생", e);
            }

            mqttClient = null;
        }

        if (worker != null) {
            //dbworker thread 종료
            worker.discard();
            worker = null;
        }
        DebugLog.d("stop 종료()");
    }

    /**
     * keepAlive
     */
    public void keepAlive() {
        DebugLog.d("keepAlive 시작()");
        DebugLog.d("mqttClient = " + mqttClient);

        try {
            //로밍체크
            if (isRoaming()) return;

            // 연결
            connect();

            // ping
            DebugLog.d("pingSender = " + pingSender);
            if (pingSender != null) {
                pingSender.ping();
            }

            // 할 일 처리
            if (worker == null) {
                worker = new Worker(context, this, mqttClient, pushdb);
                worker.start();
                DebugLog.d("worker = " + worker);
            } else {
                synchronized (worker) {
                    DebugLog.d("worker를 깨웁니다");
                    worker.notify();
                }
            }
        } catch (MQConnectException e) {
            e.printStackTrace();
            DebugLog.e("MQConnectException 발생", e);
            ErrLogger.e(TAG, "MQConnectException 발생", e);

            connectRetry++;
            DebugLog.d("연결 재시도 횟수 = " + connectRetry);
            serverConnectToggle = !serverConnectToggle;
            DebugLog.d("serverConnectToggle = " + serverConnectToggle);

            /**
             * 서버에서 토큰을 삭제하는 경우가 있음
             * 인증실패로 인한 연결실패시 토큰을 지우고 다음턴에서 다시 인증을 받고 연결한다.
             */
            DebugLog.d("ReasonCode = " + e.getReasonCode());
            if (e.getReasonCode() == MqttException.REASON_CODE_NOT_AUTHORIZED
                    || e.getReasonCode() == MqttException.REASON_CODE_INVALID_CLIENT_ID
                    /*|| e.getReasonCode() == MqttException.REASON_CODE_SERVER_CONNECT_ERROR*/) {
                preference.put(PushPreference.TOKEN, null);
                stop();
            }
            releaseWakeLock();

            //set alarm
            // 알람설정
            DebugLog.d("알람을 설정합니다");
            double delay = Math.pow((double) 2, (double) connectRetry);
            DebugLog.d("delay = " + delay);
            long delayInSeconds = (long) Math.min(delay, BuildConfig.CONNECT_RETRY_MAX_INTERVAL);
            DebugLog.d("delayInSeconds = " + delayInSeconds);
            long nextAlarmInMilliseconds = System.currentTimeMillis() + (delayInSeconds * 1000);
            DebugLog.d("nextAlarmInMilliseconds = " + nextAlarmInMilliseconds);
            AlarmManager service = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, PushReceiver.class);
            i.setAction("kr.co.adflow.push.service.KEEPALIVE");
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            service.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                    BuildConfig.ALARM_INTERVAL * 1000, pending);
            DebugLog.d("알람이 설정되었습니다");
        } catch (Exception e) {
            DebugLog.e("keepAlive 처리시 예외 상황 발생", e);
            ErrLogger.e(TAG, "keepAlive 처리시 예외 상황 발생", e);
            releaseWakeLock();
        }
        DebugLog.d("keepAlive 종료()");
    }

    /**
     * @return
     */
    private boolean isRoaming() {
        // 로밍체크
        // testCode
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        DebugLog.d("networkInfo = " + networkInfo);

        if (networkInfo != null) {
            DebugLog.d(networkInfo.isRoaming() ? "로밍 사용자 입니다" : "로밍 사용자가 아닙니다");

            //로밍사용자
            if (networkInfo.isRoaming()) {
                if (mqttClient != null) {
                    //푸시핸들러 종료
                    stop();
                }
                return true;
            }
        }//end 로밍체크
        return false;
    }


    /**
     *
     */
    private void releaseWakeLock() {
        if (PushServiceImpl.getWakeLock() != null) {
            try {
                PushServiceImpl.getWakeLock().release();
                DebugLog.d("웨이크락을 해제했습니다" + PushServiceImpl.getWakeLock());
            } catch (Exception e) {
                DebugLog.e("웨이크락 해제시 예외 상황 발생", e);
            }
        }
    }

    /**
     * 오래된 메시지 삭제
     */
    private void expireMsg() throws Exception {
        DebugLog.d("expireMsg 시작()");
        pushdb.testQuery();
        DebugLog.d("expireMsg 종료()");
    }

    /**
     * @param info
     * @return
     */
    private boolean validateServerInfo(String info) {
        DebugLog.d("validateServerInfo 시작()");

        try {
            JSONObject data = new JSONObject(info);
            if (!data.has("mqttbroker")) {
                DebugLog.d("validateServerInfo 종료(false)");
                return false;
            }
            DebugLog.d("serverInfo = " + data.getJSONArray("mqttbroker").getString(0));
            DebugLog.d("serverInfo = " + data.getJSONArray("mqttbroker").getString(1));

            if (!data.getJSONArray("mqttbroker").getString(0).startsWith("ssl")
                    || !data.getJSONArray("mqttbroker").getString(1).startsWith("ssl")) {
                DebugLog.d("validateServerInfo 종료(false)");
                return false;
            }
        } catch (Exception e) {
            DebugLog.e("validateServerInfo중 예외 상황 발생", e);
            DebugLog.d("validateServerInfo 종료(false)");
            return false;
        }
        DebugLog.d("validateServerInfo 종료(true)");
        return true;
    }

    private String getServerInfo() throws ServerInfoException {
        DebugLog.d("getServerInfo 시작()");
        String response = null;

        try {
            DebugLog.d("서버 접속 정보를 가져옵니다");
            String phoneNum = preference.getValue(PushPreference.PHONENUM, null);
            DebugLog.d("phoneNum = " + phoneNum);

            if (phoneNum == null) {
                throw new Exception("전화번호가 없습니다");
            }

            DebugLog.d("currentToken = " + currentToken);
            if (currentToken == null) {
                throw new Exception("토큰이 존재하지 않습니다");
            }

            //setMode
            setStrictMode();

            //DebugLog.d("X-ApiKey = " + currentToken);
            DebugLog.d("GET_PROVISIONING_URL = " + BuildConfig.GET_PROVISIONING_URL + phoneNum + "?token=" + currentToken);
            HttpRequest request = HttpRequest.get(BuildConfig.GET_PROVISIONING_URL + phoneNum + "?token=" + currentToken)
                    //.trustAllCerts() //Accept all certificates
                    //.trustAllHosts() //Accept all hostnames
                    .header("Accept-Version", "1.0.0") //Accept-Version : 1.0.0
                    .header("Content-Type", "application/json");

            DebugLog.d("responseCode = " + request.code());
            if (!request.ok()) {
                throw new IOException("Unexpected code "
                        + request.code());
            }
            response = request.body();
            DebugLog.d("getServerInfo 종료(response = " + response + ")");
        } catch (Exception e) {
            throw new ServerInfoException(e);
        }
        return response;
    }


    /**
     * 토큰 발급
     *
     * @return
     * @throws Exception
     */
    private String issueToken() throws Exception {
        DebugLog.d("issueToken 시작()");
        String token = null;
        DebugLog.d("토큰을 발급합니다");
        String phoneNum = preference.getValue(PushPreference.PHONENUM, null);
        DebugLog.d("phoneNum = " + phoneNum);

        if (phoneNum == null) {
            throw new Exception("전화번호가 없습니다");
        }

        //토큰발급
        JSONObject returnData = new JSONObject();
        JSONObject result = new JSONObject();
        String res = null;

        //인증
        String androidID = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);
        DebugLog.d("androidID = " + androidID);
        DebugLog.d("AUTH_URL = " + BuildConfig.AUTH_URL);
        res = this.auth(BuildConfig.AUTH_URL, phoneNum, androidID);
        JSONObject obj = new JSONObject(res);
        JSONObject rst = obj.getJSONObject("result");

        if (!rst.getBoolean("success")) {
            //에러데이타 추출후 응답메시지에 추가요망
            throw new Exception("인증 문제가 발생하였습니다");
        }
        JSONObject data = rst.getJSONObject("data");
        token = data.getString("tokenID");
        DebugLog.d("token = " + token);
        DebugLog.d("issueToken 종료(token = " + token + ")");
        return token;
    }

    /**
     * eclipse paho client용 로거
     */
    private void setMqttClientLog() {
        DebugLog.d("setMqttClientLog 시작()");
        java.util.logging.Handler defaultHandler = new ConsoleHandler();
        LogManager logManager = LogManager.getLogManager();
        Logger logger = Logger.getLogger(MQTT_PACKAGE);
        defaultHandler.setFormatter(new SimpleFormatter());
        defaultHandler.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
        logger.addHandler(defaultHandler);
        logManager.addLogger(logger);
        DebugLog.d("setMqttClientLog 종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
     * Throwable)
     */
    @Override
    public void connectionLost(Throwable throwable) {
        DebugLog.d("connectionLost 시작(에러 = " + throwable + ")");
        DebugLog.e("TCP 세션 연결이 끊겼습니다", throwable);
        CONN_LOST_COUNT++;
        DebugLog.d("connectionLostCount = " + CONN_LOST_COUNT);
        //sendBroadcast
        try {
            sendBroadcast(MQTT_DISCONNECTED_MESSAGE, MQTT_DISCONNECTED);
        } catch (Exception e) {
            DebugLog.e("예외 상황 발생", e);
        }
        ErrLogger.e(TAG, "TCP 세션 연결이 끊겼습니다", throwable);
        DebugLog.d("connectionLost 종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse
     * .paho.client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        DebugLog.d("deliveryComplete 시작(토큰 = " + token + ")");
        DebugLog.d("deliveryComplete 종료()");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
     * String, org.eclipse.paho.client.mqttv3.MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        DebugLog.d("messageArrived 시작(토픽 = " + topic + ",메시지 = " + message + ",qos = "
                + message.getQos() + ")");
        try {
            //메시지 파싱
            JSONObject data = new JSONObject(new String(message.getPayload()));
            DebugLog.d("data = " + data);

            // message 분기처리
            int msgType = data.getInt("msgType");

            //precheck
            if (msgType == 103) {
                DebugLog.d("preCheck received");
                return;
            }

            //testCode
            if (msgType == 900) {
                DebugLog.d("msg received");
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isScreenOn();
                DebugLog.d("isScreenOn = " + isScreenOn);

                DebugLog.d("BOARD = " + Build.BOARD);
                DebugLog.d("BRAND = " + Build.BRAND);
                DebugLog.d("CPU_ABI = " + Build.CPU_ABI);
                DebugLog.d("DEVICE = " + Build.DEVICE);
                DebugLog.d("DISPLAY = " + Build.DISPLAY);
                DebugLog.d("FINGERPRINT = " + Build.FINGERPRINT);
                DebugLog.d("HOST = " + Build.HOST);
                DebugLog.d("ID = " + Build.ID);
                DebugLog.d("MANUFACTURER = " + Build.MANUFACTURER);
                DebugLog.d("MODEL = " + Build.MODEL);
                DebugLog.d("PRODUCT = " + Build.PRODUCT);
                DebugLog.d("TAGS = " + Build.TAGS);
                DebugLog.d("TYPE = " + Build.TYPE);
                DebugLog.d("USER = " + Build.USER);
                DebugLog.d("VERSION.RELEASE = " + Build.VERSION.RELEASE);

                if (!isScreenOn) {
                    ConnectivityManager connectivityManager;
                    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    DebugLog.d("networkInfo.type = " + networkInfo.getTypeName());
                    if (!networkInfo.getTypeName().startsWith("WIFI")) {
                        Intent intent = new Intent(context, TRLogger.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("url", data.getString("url"));
                        context.startActivity(intent);
                    }
                }
                return;
            }
            //testEnd

            String serviceId = data.getString("serviceId");
            DebugLog.d("serviceId = " + serviceId);

            String msgId = data.getString("msgId");
            DebugLog.d("msgId = " + msgId);

            //트랜잭션 저장
            //메시지 수신 트랜잭션 (시작)
            kr.co.adflow.push.util.TRLogger.i(TAG, "[" + msgId + "] 메시지 수신 data = " + data);

            int ack = 0;
            if (data.has("ack")) {
                ack = data.getInt("ack");
            }
            DebugLog.d("ack = " + ack);

            if (ack == 1) {
                //받은메시지 저장
                pushdb.addMessage(msgId, serviceId, topic, message.getPayload(), message.getQos(), ack, currentToken);
                // ack job 저장
                JSONObject ackJson = new JSONObject();
                ackJson.put("msgId", msgId);
                ackJson.put("token", currentToken);
                ackJson.put("ackType", "pma");
                ackJson.put("ackTime", System.currentTimeMillis());
                addAckJob(ackJson);
            }
            JSONObject content;

            switch (msgType) {
                case BuildConfig.SET_KEEP_ALIVE_MESSAGE: // keepAlive 설정변경
                    content = data.getJSONObject("content");
                    DebugLog.d("content = " + content);
                    int keepAlive = content.getInt("keepAlive");
                    // store keepalive
                    preference.put(PushPreference.KEEPALIVE, keepAlive);
                    DebugLog.d("PushServiceClass = " + context.getClass());
                    Intent restart = new Intent(context, /* PushServiceImpl.class */
                            context.getClass());
                    restart.setAction("kr.co.adflow.push.service.RESTART");
                    context.startService(restart);
                    break;
                case BuildConfig.CONTROL_MESSAGE:
                case BuildConfig.USER_MESSAGE:
                    //boolean enable = preference.getValue(PushPreference.REGISTERED_PMC, false);
                    //if (enable) {
                    sendBroadcast(data, currentToken, msgId);
                    //트랜잭션 저장
                    //메시지 수신 트랜잭션 (브로드캐스팅 완료)
                    kr.co.adflow.push.util.TRLogger.i(TAG, "[" + msgId + "] 메시지 브로드캐스팅 완료");
                    //} else {
                    //    DebugLog.d("PMC가 사용가능하지 않습니다.");
                    //}
                    break;
                case BuildConfig.FIRMWARE_UPDATE_MESSAGE:
                case BuildConfig.DIG_ACCOUNT_INFO_MESSAGE:
                    //broadcast 작업추가
                    //int jobID = pushdb.addJob(BROADCAST, "", "{\"msgId\":\"" + msgId + "\"}");
                    //DebugLog.d("broadcast작업이추가되었습니다.jobID = " + jobID);
                    sendBroadcast(data, currentToken, msgId);
                    //트랜잭션 저장
                    //메시지 수신 트랜잭션 (브로드캐스팅 완료)
                    kr.co.adflow.push.util.TRLogger.i(TAG, "[" + msgId + "] 메시지 브로드캐스팅 완료");
                    //broadcast 작업제거
                    //pushdb.deteletJob(jobID);
                    //DebugLog.d("broadcast작업이삭제되었습니다.jobID = " + jobID);
                    break;
                case BuildConfig.SUBSCRIBE: //subscribe
                    content = data.getJSONObject("content");
                    DebugLog.d("content = " + content);
                    String subscribeTopic = content.getString("topic");
                    DebugLog.d("subscribeTopic = " + subscribeTopic);
                    addSubscribeJob(subscribeTopic);

                    // wake up
                    wakeUpWorker();
                    break;
                case BuildConfig.UNSUBSCRIBE: //unsubscribe
                    content = data.getJSONObject("content");
                    DebugLog.d("content = " + content);
                    String unsubscribeTopic = content.getString("topic");
                    DebugLog.d("unsubscribeTopic = " + unsubscribeTopic);
                    addUnsubscribeJob(unsubscribeTopic);

                    // wake up
                    wakeUpWorker();
                    break;
                case BuildConfig.GET_TRLOG_MESSAGE: //get TRLog
                    content = data.getJSONObject("content");
                    DebugLog.d("content = " + content);
                    // add job
                    JSONObject trlogJob = new JSONObject();
                    trlogJob.put("msgId", msgId);
                    trlogJob.put("token", currentToken);
                    trlogJob.put("hostInfo", content.getString("hostInfo"));
                    addJobTRLog(trlogJob);

                    // wake up
                    wakeUpWorker();
                    break;
                case BuildConfig.CHANGE_MQTT_CHANNEL: // mqtt 채널 변경
                    content = data.getJSONObject("content");
                    DebugLog.d("content = " + content);
                    // add job
                    JSONObject changeChannelJob = new JSONObject();
                    changeChannelJob.put("msgId", msgId);
                    changeChannelJob.put("token", currentToken);
                    changeChannelJob.put("mqttCluster", content.getString("mqttCluster"));
                    addJobChangeMqttChannel(changeChannelJob);

                    // wake up
                    wakeUpWorker();
                    break;
                default:
                    DebugLog.e("메시지 타입이 없습니다");
                    break;
            }
        } catch (Exception e) {
            DebugLog.e("메시지 처리중 예외 상황 발생", e);
            ErrLogger.e(TAG, "메시지 처리중 예외 상황 발생", e);
        }
        DebugLog.d("messageArrived 종료()");
    }

    /**
     * @param ackJson
     * @throws Exception
     */
    public void addAckJob(JSONObject ackJson) throws Exception {
        DebugLog.d("addAckJob 시작(ackJson = " + ackJson + ")");
        int ackJob = pushdb.addJob(BuildConfig.JOB_ACK, ACK_TOPIC, ackJson.toString());
        DebugLog.i("ack 작업이 추가 되었습니다 jobID = " + ackJob);
        // wake up
        wakeUpWorker();
        DebugLog.d("addAckJob 종료()");
    }

    /**
     *
     */
    private void wakeUpWorker() {
        // wake up
        if (worker != null) {
            synchronized (worker) {
                DebugLog.d("worker를 깨웁니다");
                worker.notify();
            }
        }
    }

    /**
     * @param topic
     * @throws Exception
     */
    public void addSubscribeJob(String topic) throws Exception {
        DebugLog.d("addSubscribeJob 시작(topic = " + topic + ")");
        int job = pushdb.addJob(BuildConfig.JOB_SUBSCRIBE, topic, null);
        DebugLog.i("subscribe 작업이 추가 되었습니다 jobID = " + job);
        DebugLog.d("addSubscribeJob 종료()");
    }

    /**
     * @param topic
     * @throws Exception
     */
    public void addUnsubscribeJob(String topic) throws Exception {
        DebugLog.d("addUnsubscribeJob 시작(topic = " + topic + ")");
        int job = pushdb.addJob(BuildConfig.JOB_UNSUBSCRIBE, topic, null);
        DebugLog.i("unsubscribe 작업이 추가 되었습니다 jobID = " + job);
        DebugLog.d("addUnsubscribeJob 종료()");
    }

    /**
     * @param data
     * @throws Exception
     */
    public void addJobTRLog(JSONObject data) throws Exception {
        DebugLog.d("addJobTRLog 시작(data = " + data + ")");
        int job = pushdb.addJob(BuildConfig.JOB_GET_TRLOG, null, data.toString());
        DebugLog.i("TRLog 작업이 추가 되었습니다 jobId = " + job);
        DebugLog.d("addJobTRLog 종료()");
    }

    /**
     * @param data
     * @throws Exception
     */
    public void addJobChangeMqttChannel(JSONObject data) throws Exception {
        DebugLog.d("addJobChangeMqttChannel 시작(data = " + data + ")");
        int job = pushdb.addJob(BuildConfig.JOB_CHANGE_MQTT_CHANNEL, null, data.toString());
        DebugLog.i("addJobChangeMqttChannel 작업이 추가 되었습니다 jobId = " + job);
        DebugLog.d("addJobChangeMqttChannel 종료()");
    }

    /**
     * @param data
     * @throws JSONException`
     */
    public void sendBroadcast(JSONObject data, String token, String msgId) throws Exception {
        DebugLog.d("sendBroadcast 시작(data = " + data + ", token = " + token + ", msgId = " + msgId + ")");
        String serviceId = data.getString("serviceId");
        DebugLog.d("serviceId = " + serviceId);

        Intent i = new Intent(serviceId);
        i.putExtra("msgId", msgId);
        i.putExtra("token", token);
        i.putExtra("contentType", data.getString("contentType"));
        i.putExtra("content", data.getString("content"));

        if (data.has("fileName")) {
            i.putExtra("fileName", data.getString("fileName"));
        }

        if (data.has("fileFormat")) {
            i.putExtra("fileFormat", data.getString("fileFormat"));
        }

        if (data.has("issueId")) {
            i.putExtra("issueId", data.getString("issueId"));
        }

        if (data.has("ack")) {
            i.putExtra("ack", data.getInt("ack"));
        } else {
            i.putExtra("ack", 0 /* false */);
        }

        i.putExtra("sender", data.getString("sender"));
        i.putExtra("receiver", data.getString("receiver"));

        if (data.has("resendCount")) {
            i.putExtra("resendCount", data.getInt("resendCount"));
        } else {
            i.putExtra("resendCount", 0);
        }

        DebugLog.d("intent = " + i);
        context.sendBroadcast(i);
        DebugLog.d("sendBroadcast 종료()");
    }

    /**
     * @param eventMsg
     * @param eventCode
     * @throws Exception
     */
    private void sendBroadcast(String eventMsg, int eventCode) throws Exception {
        DebugLog.d("sendBroadcast 시작(eventMsg = " + eventMsg + ", eventCode = " + eventCode + ")");
        Intent i = new Intent(BuildConfig.CONN_STATUS_ACTION);
        i.putExtra("eventMsg", eventMsg);
        i.putExtra("eventCode", eventCode);
        DebugLog.d("event = " + i);
        context.sendBroadcast(i);
        DebugLog.d("sendBroadcast 종료()");
    }

    /**
     * @throws MqttException
     */
    protected synchronized void connect() throws MQConnectException {
        DebugLog.d("connect 시작()");

        if (mqttClient != null && mqttClient.isConnected()) {
            DebugLog.d("token = " + mqttClient.getClientId());
            DebugLog.d("serverInfo = " + mqttClient.getServerURI());
            DebugLog.d("이미 세션이 연결되어 있습니다");
            DebugLog.d("connect 종료()");
            return;
        }

        try {
            //stop mqttClient
            stop();

            //토큰가져오기
            String token = preference.getValue(PushPreference.TOKEN, null);

            DebugLog.d("token = " + token);
            // token이 null일 경우 토큰을발급한다.
            if (token == null || token.equals("")) {
                currentToken = issueToken();
                DebugLog.d("발급된 토큰 = " + currentToken);
                //토큰저장
                preference.put(PushPreference.TOKEN, currentToken);
                sendBroadcast(MQTT_TOKEN_CHANGED_MESSAGE, MQTT_TOKEN_CHANGED);
                // put issue new token
                preference.put(PushPreference.ISSUE_NEW_TOKEN, false);
            } else {
                currentToken = token;
            }

            //서버정보가져오기
            String mqttBrokerInfo = preference.getValue(PushPreference.SERVERURL,
                    null);
            DebugLog.d("mqttBrokerInfo = " + mqttBrokerInfo);
            String mqttBrokerInfoOld = mqttBrokerInfo;

            if (mqttBrokerInfo == null || connectRetry > BuildConfig.CONNECT_FAIL_COUNT_LIMIT
                    || !validateServerInfo(mqttBrokerInfo)) {
                //서버정보가져오기
                mqttBrokerInfo = getServerInfo();
            }

            if (!validateServerInfo(mqttBrokerInfo)) {
                //connectRetry += BuildConfig.CONNECT_FAIL_COUNT_LIMIT;
                throw new ServerInfoException();
            }

            pingSender = new PingSender(context);

            JSONObject data = new JSONObject(mqttBrokerInfo);
            String firstServer = null;

            if (serverConnectToggle) {
                firstServer = data.getJSONArray("mqttbroker").getString(0);
            } else {
                firstServer = data.getJSONArray("mqttbroker").getString(1);
            }

            DebugLog.d("primaryServerInfo = " + firstServer);
            mqttClient = new MqttAsyncClient(firstServer, currentToken,
                    new MemoryPersistence(), pingSender);

            MqttConnectOptions mOpts = new MqttConnectOptions();
            // mOpts.setUserName("testUser");
            // mOpts.setPassword("testPasswd".toCharArray());
            mOpts.setConnectionTimeout(BuildConfig.MQTT_CONNECTION_TIMEOUT);
            int keepAlive = preference.getValue(PushPreference.KEEPALIVE,
                    BuildConfig.DEFAULT_KEEP_ALIVE_TIME_OUT);
            DebugLog.d("keepAlive = " + keepAlive);
            mOpts.setKeepAliveInterval(keepAlive);
            boolean cleanSession = preference.getValue(PushPreference.CLEANSESSION,
                    false);
            DebugLog.d("cleanSession = " + cleanSession);
            mOpts.setCleanSession(cleanSession);
            mOpts.setMqttVersion(MQTTVERSION_3);

            if (serverConnectToggle) {
                //firstServer = data.getJSONArray("mqttbroker").getString(0);
                DebugLog.d("primaryServerInfo = " + data.getJSONArray("mqttbroker").getString(0));
                DebugLog.d("secondaryServerInfo = " + data.getJSONArray("mqttbroker").getString(1));
                mOpts.setServerURIs(new String[]{data.getJSONArray("mqttbroker").getString(0), data.getJSONArray("mqttbroker").getString(1)});
            } else {
                // firstServer = data.getJSONArray("mqttbroker").getString(1);
                DebugLog.d("primaryServerInfo = " + data.getJSONArray("mqttbroker").getString(1));
                DebugLog.d("secondaryServerInfo = " + data.getJSONArray("mqttbroker").getString(0));
                mOpts.setServerURIs(new String[]{data.getJSONArray("mqttbroker").getString(1), data.getJSONArray("mqttbroker").getString(0)});
            }

            // ssl 처리
            if (data.getJSONArray("mqttbroker").getString(0).startsWith("ssl")) {
                mOpts.setSocketFactory(ADFSSLSocketFactory.getSSLSokcetFactory());
            }

            DebugLog.d("연결 옵션 = " + mOpts);
            DebugLog.d("mqttClient = " + mqttClient);
            DebugLog.d("콜백 인스턴스 = " + this);
            mqttClient.setCallback(this);

            DebugLog.d("mqtt 서버에 연결합니다");
            IMqttToken waitToken = mqttClient.connect(mOpts);
            waitToken.waitForCompletion();
            connectRetry = 0;
            DebugLog.d("세션이 연결되었습니다");

            if (!mqttBrokerInfo.equals(mqttBrokerInfoOld)) {
                //기존 서버 정보와 현 서버정보를 비교 후 다르면 마킹 그리고 접속에 성공하면 broadcast
                //서버정보저장
                preference.put(PushPreference.SERVERURL, mqttBrokerInfo);
                //채널변경시 subscribe mms/82XXX
                String phonenum = preference.getValue(PushPreference.PHONENUM, null);
                DebugLog.d("phonenum = " + phonenum);
                if (phonenum != null) {
                    // '+' 제거
                    phonenum = phonenum.replace("+", "");
                    String subscribeTopic = "mms/" + phonenum;
                    DebugLog.d("subscribeTopic = " + subscribeTopic);
                    addSubscribeJob(subscribeTopic);
                }
                sendBroadcast(MQTT_CHANNEL_CHANGED_MESSAGE, MQTT_CHANNEL_CHANGED);
            }

            doFirstJob();
        } catch (ServerInfoException e) {
            throw new MQConnectException(e);
        } catch (MqttException e) {
            throw new MQConnectException(e.getReasonCode(), e);
        } catch (Exception e) {
            throw new MQConnectException(e);
        } finally {

        }
        DebugLog.d("connect 종료()");
    }

    /**
     *
     */
    private void doFirstJob() {
        //sendBroadcast
        boolean firstConn = preference.getValue(PushPreference.FIRSTCONNECTION,
                false);
        try {
            if (firstConn) {
                //부팅후 최초 연결시
                sendBroadcast(FIRST_MQTT_CONNECTED_MESSAGE, FIRST_MQTT_CONNECTED);
                preference.put(PushPreference.FIRSTCONNECTION, false);
                //부팅후 최초 연결시 subscribe mms/82XXX
                String phonenum = preference.getValue(PushPreference.PHONENUM, null);
                DebugLog.d("phonenum = " + phonenum);
                if (phonenum != null) {
                    // '+' 제거
                    phonenum = phonenum.replace("+", "");
                    String subscribeTopic = "mms/" + phonenum;
                    DebugLog.d("subscribeTopic = " + subscribeTopic);
                    addSubscribeJob(subscribeTopic);
                } else {
                    // 전화번호가 이상합니다.
                    throw new Exception("전화번호가 오류로 해당 토픽을 구독할 수 없습니다.");
                }
                String mqttBrokerInfo = getServerInfo();
                DebugLog.d("mqttBrokerInfo = " + mqttBrokerInfo);
            } else {
                sendBroadcast(MQTT_CONNECTED_MESSAGE, MQTT_CONNECTED);
            }
            CONN_LOST_COUNT = 0;
        } catch (Exception e) {
            DebugLog.e("예외 상황 발생", e);
        }
    }

    /**
     * @param topic
     * @param qos
     * @throws MqttException
     */
    public synchronized void subscribe(String topic, int qos) throws Exception {
        DebugLog.d("subScribe 시작(토픽 = " + topic + ", qos = " + qos + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("토픽 구독에 실패하였습니다 (mqttClient가 없거나 연결이 안되어 있습니다)");
        }

        // 토픽구독
        IMqttToken token = mqttClient.subscribe(topic, qos);
        token.waitForCompletion();
        //subscribe 트랜잭션 저장
        kr.co.adflow.push.util.TRLogger.i(TAG, "[" + currentToken + "] subscribe topic = " + topic + " qos = " + qos + " 구독 완료");
        DebugLog.d("토픽 구독을 완료하였습니다");

        //addSubscribeJob(topic);
        DebugLog.d("subscribe 종료()");
    }

    /**
     * @param topic
     * @throws MqttException
     */
    public synchronized void unsubscribe(String topic) throws Exception {
        DebugLog.d("unsubscribe 시작(토픽 = " + topic + ")");

        if (mqttClient == null || !mqttClient.isConnected()) {
            throw new Exception("토픽 구독 취소에 실패하였습니다 (mqttClient가 없거나 연결이 안되어 있습니다)");
        }

        // 토픽구독
        IMqttToken token = mqttClient.unsubscribe(topic);
        token.waitForCompletion();
        //unsubscribe 트랜잭션 저장
        kr.co.adflow.push.util.TRLogger.i(TAG, "[" + currentToken + "] unsubscribe topic = " + topic + " 구독 취소 완료");
        DebugLog.d("토픽 구독을 취소하였습니다");
        DebugLog.d("unsubscribe 종료()");
    }

    /**
     * @param topic
     * @param payload
     * @param qos
     * @param retained
     * @throws Exception
     */
    public synchronized void publish(String topic, byte[] payload, int qos,
                                     boolean retained) throws Exception {
        DebugLog.d("publish 시작(토픽 = " + topic + ", payload = " + new String(payload)
                + ", qos = " + qos + ", retained = " + retained + ")");
        if (mqttClient != null) {
            IMqttDeliveryToken token = mqttClient.publish(topic, payload, qos,
                    retained);
            token.waitForCompletion();
            //트랜잭션 저장
            //메시지 송신 트랜잭션
            kr.co.adflow.push.util.TRLogger.i(TAG, "[" + topic + "] 메시지 송신 qos = " + qos + ", 데이타 = " + new String(payload));
        } else {
            DebugLog.d("mqttClient = " + mqttClient);
            throw new Exception("메시지 전송 실패");
        }

        DebugLog.d("publish 종료()");
        return;
    }

    /**
     * @return
     */
    public boolean isConnected() {
        DebugLog.d("isConnected 시작()");
        boolean value = false;
        if (mqttClient != null) {
            value = mqttClient.isConnected();
        }
        DebugLog.d("isConnected 종료(value = " + value + ")");
        return value;
    }

    /**
     * @return
     */
    public int getLostCount() {
        return CONN_LOST_COUNT;
    }


    /**
     * @param sender
     * @param topic
     * @return
     * @throws Exception
     */
    public void preCheck(String sender, String topic) throws Exception {
        DebugLog.d("preCheck 시작(sender = " + sender + ", topic = " + topic + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        JSONObject data = new JSONObject();
        data.put("sender", sender);
        data.put("receiver", topic);
        DebugLog.d("data = " + data);

        DebugLog.d("X-ApiKey = " + currentToken);
        DebugLog.d("PRECHECK_URL = " + BuildConfig.PRECHECK_URL);
        HttpRequest request = HttpRequest.post(BuildConfig.PRECHECK_URL)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }

        DebugLog.d("preCheck 종료()");
    }

    /**
     * @param url
     * @param userID
     * @param deviceID
     * @return
     * @throws Exception
     */
    public String auth(String url, String userID, String deviceID)
            throws Exception {
        DebugLog.d("auth 시작(url = " + url + ", userID = " + userID + ", deviceID = "
                + deviceID + ")");

        //setMode
        setStrictMode();

        JSONObject data = new JSONObject();
        data.put("userID", userID);
        data.put("deviceID", deviceID);
        // add issue_new_token 2016.12.
        boolean issueNewToken = preference.getValue(PushPreference.ISSUE_NEW_TOKEN, false);
        DebugLog.d("issueNewToken = " + issueNewToken);

        if(issueNewToken){
            data.put("issueNewToken", issueNewToken);
        }

        DebugLog.d("RequestURL = " + url);
        HttpRequest request = HttpRequest.post(url)
                .trustAllCerts() //Accept all certificates // 공인인증서 패스 2016.10.4
                .trustAllHosts() //Accept all hostnames
                        //.header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("auth 종료(response = " + response + ")");
        return response;
    }

    public String getSubscriptions() throws Exception {
        DebugLog.d("getSubscriptions 시작()");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        DebugLog.d("X-ApiKey = " + currentToken);
        DebugLog.d("GET_SUBSCRIPTIONS_URL = " + BuildConfig.GET_SUBSCRIPTIONS_URL + currentToken);
        HttpRequest request = HttpRequest.get(BuildConfig.GET_SUBSCRIPTIONS_URL + currentToken)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json;charset=utf-8");

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("getSubscriptions 종료(response = " + response + ")");
        return response;
    }

    public String getGrpSubscribers(String topic) throws Exception {
        DebugLog.d("getGrpSubscribers 시작(topic = " + topic + ", thread = " + Thread.currentThread() + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        DebugLog.d("X-Application-Key = " + currentToken);
        DebugLog.d("GROUP_TOPIC_SUBSCRIBER_URL = " + BuildConfig.GROUP_TOPIC_SUBSCRIBER_URL + topic);
        HttpRequest request = HttpRequest.get(BuildConfig.GROUP_TOPIC_SUBSCRIBER_URL + topic)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-Application-Key", currentToken)
                .header("Content-Type", "application/json;charset=utf-8");

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("getGrpSubscribers 종료(response = " + response + ")");
        return response;
    }

    /**
     * @param userID
     * @return
     * @throws Exception
     */
    public String existPMAByUserID(String userID) throws Exception {
        DebugLog.d("existPMAByUserID 시작(userID = " + userID + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        JSONObject data = new JSONObject();
        data.put("userID", userID);
        DebugLog.d("data = " + data);

        DebugLog.d("X-ApiKey = " + currentToken);
        DebugLog.d("EXIST_PMA_BY_USERID_URL = " + BuildConfig.EXIST_PMA_BY_USERID_URL);
        HttpRequest request = HttpRequest.post(BuildConfig.EXIST_PMA_BY_USERID_URL)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();

        DebugLog.d("existPMAByUserID 종료(response = " + response + ")");
        return response;
    }

    /**
     * @param ufmi
     * @return
     * @throws Exception
     */
    public String existPMAByUFMI(String ufmi) throws Exception {
        DebugLog.d("existPMAByUFMI 시작(ufmi = " + ufmi + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        JSONObject data = new JSONObject();
        data.put("ufmi", ufmi);
        DebugLog.d("data = " + data);

        DebugLog.d("X-ApiKey = " + currentToken);
        DebugLog.d("EXIST_PMA_BY_UFMI_URL = " + BuildConfig.EXIST_PMA_BY_UFMI_URL);
        HttpRequest request = HttpRequest.post(BuildConfig.EXIST_PMA_BY_UFMI_URL)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("existPMAByUFMI 종료(response = " + response + ")");
        return response;
    }


    public String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String
            content, int contentLength, int expiry, boolean mms) throws Exception {
        DebugLog.d("sendMsgWithOpts 시작(sender = " + sender + ", receiver = " + receiver + ", qos = "
                + qos + ", contentType = " + contentType + ", content = " + content + ", contentLength = " + contentLength + ", expiry = " + expiry + ", thread = " + Thread.currentThread() + ", mms = " + mms + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        JSONObject data = new JSONObject();
        data.put("sender", sender);
        data.put("receiver", receiver);
        data.put("qos", qos);
        data.put("contentType", contentType);
        data.put("content", content);
        data.put("contentLength", contentLength);
        data.put("expiry", expiry);
        if (mms) {
            data.put("mms", mms);
        }
        DebugLog.d("data = " + data);

        DebugLog.d("X-Application-Key = " + currentToken);
        DebugLog.d("USER_MESSAGE_URI = " + BuildConfig.MESSAGE_URI);
        HttpRequest request = HttpRequest.post(BuildConfig.MESSAGE_URI)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-Application-Key", currentToken)
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("sendMsgWithOpts 종료(response = " + response + ")");
        return response;
    }

    /**
     * @param phoneNum
     * @param ufmi
     * @return
     * @throws Exception
     */
    public String updateUFMI(String phoneNum, String ufmi) throws Exception {
        DebugLog.d("updateUFMI 시작(phoneNum = " + phoneNum + ", ufmi = " + ufmi + ")");

        //setMode
        setStrictMode();

        DebugLog.d("currentToken = " + currentToken);
        if (currentToken == null) {
            throw new Exception("토큰이 존재하지 않습니다");
        }

        JSONObject data = new JSONObject();
        data.put("phoneNum", phoneNum);
        data.put("ufmi", ufmi);
        DebugLog.d("data = " + data);

        DebugLog.d("X-ApiKey = " + currentToken);
        DebugLog.d("UPDATEUFMI_URL = " + BuildConfig.UPDATE_UFMI_URL);
        HttpRequest request = HttpRequest.put(BuildConfig.UPDATE_UFMI_URL)
                .trustAllCerts() //Accept all certificates
                .trustAllHosts() //Accept all hostnames
                .header("X-ApiKey", currentToken)
                .header("Content-Type", "application/json;charset=utf-8").send(data.toString());

        DebugLog.d("responseCode = " + request.code());
        if (!request.ok()) {
            throw new IOException("Unexpected code "
                    + request.code());
        }
        String response = request.body();
        DebugLog.d("updateUFMI 종료(response = " + response + ")");
        return response;
    }

    /**
     * @return
     */
    public void setToken(String token) {
        DebugLog.d("setToken 시작(token = " + token + ")");
        currentToken = token;
        DebugLog.d("setToken 종료()");
    }

    /**
     * @return
     */
    public String getToken() {
        DebugLog.d("getToken 시작()");
        DebugLog.d("getToken 종료(token = " + currentToken + ")");
        return currentToken;
    }

    /**
     *
     */
    private void setStrictMode() {
        DebugLog.d("setStrictMode 시작()");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        DebugLog.d("setStrictMode 종료()");
    }
}
