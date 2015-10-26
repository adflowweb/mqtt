package kr.co.adflow.push.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.service.impl.PushServiceImpl;

/**
 * @author nadir93
 */
public class PushReceiver extends BroadcastReceiver {

    // Debug TAG
    private static final String TAG = "PushReceiver";
    private static final int wakeLockHoldTime = 10000; // 웨이크락홀드타임 10초

    public static final int USIM_NOT_FOUND = 2001;
    public static final String USIM_NOT_FOUND_MESSAGE = "USIM이 없습니다.";
    public static final int USIM_CHANGED = 2002;
    public static final String USIM_CHANGED_MESSAGE = "USIM이 변경되었습니다.";
    public static final String KR_CO_KTPOWERTEL_PUSH_CONN_STATUS = "kr.co.ktpowertel.push.connStatus";

    public PushReceiver() {
        super();
    }

    //	// PTT LOGOUT
    //public static final String BNS_PMC_PTT_REG_STATE_ACTION_LOGOUT = "com.bns.pw.action.REG_STATE";
    //public static final String BNS_PMC_PTT_REG_STATE_EXTRA_VALUE = "reg";

    /*
     * (non-Javadoc)
     *
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive시작(context=" + context + ",intent=" + intent + ")");
        try {
            if (intent == null || intent.getAction() == null) {
                Log.e(TAG, "action이 존재하지않습니다.");
                return;
            }

            if (intent.getAction().equals(
                    "android.intent.action.BOOT_COMPLETED")) {
                Log.d(TAG, "부팅완료작업을시작합니다.");

                PushPreference preference = new PushPreference(context);
                //부팅후 첫커넥션 표시
                preference.put(PushPreference.FIRSTCONNECTION, true);
                String oldPhoneNum = preference.getValue(PushPreference.PHONENUM, "");
                Log.d(TAG, "저장된전화번호=" + oldPhoneNum);

                //유심변경 체크
                TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String newPhoneNumber = tMgr.getLine1Number();
                Log.d(TAG, "전화번호=" + newPhoneNumber);

                if (newPhoneNumber == null || newPhoneNumber.equals("")) {
                    Log.d(TAG, "USIM이없습니다.");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    Log.d(TAG, "토큰을삭제했습니다.");
                    //sendBroadcast
                    sendBroadcast(context, USIM_NOT_FOUND_MESSAGE, USIM_NOT_FOUND, null);
                } else if (oldPhoneNum == null || oldPhoneNum.equals("")) {
                    Log.d(TAG, "이전부팅때저장된전화번호가없습니다.");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    Log.d(TAG, "토큰을삭제했습니다.");
                    //sendBroadcast
                    sendBroadcast(context, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
                } else if (oldPhoneNum.equals(newPhoneNumber)) {
                    //같은유심
                    Log.d(TAG, "이전부팅전화번호와같습니다.");
                } else {
                    //유심변경
                    Log.d(TAG, "이전부팅전화번호와다릅니다.");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    Log.d(TAG, "토큰을삭제했습니다.");
                    //sendBroadcast
                    sendBroadcast(context, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
                }

                //서버정보저장
                preference.put(PushPreference.SERVERURL, PushHandler.MQTT_SERVER_URL);
                //set cleanSession
                preference.put(PushPreference.CLEANSESSION, PushHandler.CLEAN_SESSION);

                //알람설정
                Log.d(TAG, "keepAlive알람을설정합니다.");
                AlarmManager service = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, PushReceiver.class);
                i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);
                service.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 1000,
                        1000 * PushHandler.ALARM_INTERVAL, pending);
                Log.d(TAG, "keepAlive알람이설정되었습니다");
                Log.d(TAG, "부팅완료작업을종료합니다.");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                // keepalive
                Log.d(TAG, "keepAlive체크를시작합니다.");
                //PushPreference preference = new PushPreference(context);
                //boolean pttLogin = preference.getValue(PushPreference.PTT_LOGIN, false);
                //Log.d(TAG, "pttLogin=" + pttLogin);
                //if (pttLogin) {
                Bundle bundle = intent.getExtras();
                for (String key : bundle.keySet()) {
                    Log.d(TAG, key + " is a key in the bundle");
                    Log.d(TAG, "value=" + bundle.get(key));
                }

                // get wakelock
                PowerManager pm = (PowerManager) context
                        .getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = pm.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK, "KTPWakeLock");
                PushServiceImpl.setWakeLock(wakeLock);
                Log.d(TAG, "웨이크락상태=" + wakeLock.isHeld());
                if (!wakeLock.isHeld()) {
                    wakeLock.acquire(wakeLockHoldTime);
                    // ..screen will stay on during this section..
                    Log.d(TAG, "웨이크락=" + wakeLock);
                    Intent i = new Intent(context, PushServiceImpl.class);
                    i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                    context.startService(i);
                    // wakeLock.release();
                }
                //}
                Log.d(TAG, "keepAlive체크를종료합니다.");
            }
//            else if (intent.getAction().equals("com.bns.pw.action.REG_STATE")) {
//                Log.d(TAG, "PTT로그인상태처리");
//                boolean login = intent.getBooleanExtra("reg", false);
//                Log.d(TAG, "login=" + login);
//
//                //PushPreference preference = new PushPreference(context);
//                AlarmManager service = (AlarmManager) context
//                        .getSystemService(Context.ALARM_SERVICE);
//
//                if (login) {
//
//                    Log.d(TAG, "keepAlive알람을설정합니다.");
//
//                    Intent i = new Intent(context, PushReceiver.class);
//                    i.setAction("kr.co.adflow.push.service.KEEPALIVE");
//                    PendingIntent pending = PendingIntent.getBroadcast(context, 0,
//                            i, PendingIntent.FLAG_UPDATE_CURRENT);
//                    Calendar now = Calendar.getInstance();
//                    service.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), 1000 * PushHandler.ALARM_INTERVAL, pending);
//                    Log.d(TAG, "keepAlive알람이설정되었습니다");
//
//
////                    //preference.put(PushPreference.PTT_LOGIN, true);
////                    // get wakelock
////                    PowerManager pm = (PowerManager) context
////                            .getSystemService(Context.POWER_SERVICE);
////                    WakeLock wakeLock = pm.newWakeLock(
////                            PowerManager.PARTIAL_WAKE_LOCK, "KTPWakeLock");
////                    PushServiceImpl.setWakeLock(wakeLock);
////                    Log.d(TAG, "웨이크락상태=" + wakeLock.isHeld());
////                    if (!wakeLock.isHeld()) {
////                        wakeLock.acquire(wakeLockHoldTime);
////                        // ..screen will stay on during this section..
////                        Log.d(TAG, "웨이크락=" + wakeLock);
////                        Intent keepAliveIntent = new Intent(context, PushServiceImpl.class);
////                        keepAliveIntent.setAction("kr.co.adflow.push.service.KEEPALIVE");
////                        context.startService(keepAliveIntent);
////                        // wakeLock.release();
////                    }
//                }
//                else {
//                    //PTT 로그인 정보
//                    //preference.put(PushPreference.PTT_LOGIN, false);
//
//                    //알람제거
//                    Intent keepAliveIntent = new Intent(context, PushReceiver.class);
//                    keepAliveIntent.setAction("kr.co.adflow.push.service.KEEPALIVE");
//                    PendingIntent pending = PendingIntent.getBroadcast(context, 0,
//                            keepAliveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    service.cancel(pending);
//
//                    //stop service
//                    Intent i = new Intent(context, PushServiceImpl.class);
//                    i.setAction("kr.co.adflow.push.service.STOP");
//                    context.startService(i);
//                }
//            }
            else {
                Log.e(TAG, "적절한처리핸들러가없습니다.");
            }
        } catch (Exception e) {
            Log.e(TAG, "예외상황발생", e);
        }
        Log.d(TAG, "onReceive종료()");
    }

    /**
     * @param context
     * @param eventMsg
     * @param eventCode
     * @throws Exception
     */
    private void sendBroadcast(Context context, String eventMsg, int eventCode, String eventInfo) throws Exception {
        Log.d(TAG, "sendBroadcast시작(eventMsg=" + eventMsg + ", eventCode=" + eventCode + ", eventInfo=" + eventInfo + ")");
        Intent i = new Intent(KR_CO_KTPOWERTEL_PUSH_CONN_STATUS);
        i.putExtra("eventMsg", eventMsg);
        i.putExtra("eventCode", eventCode);
        if (eventInfo != null) {
            i.putExtra("eventInfo", eventInfo);
        }
        Log.d(TAG, "intent =" + i);
        context.sendBroadcast(i);
        Log.d(TAG, "sendBroadcast종료()");
    }
}
