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
//import android.util.Log;

import kr.co.adflow.push.BuildConfig;
import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.service.impl.PushServiceImpl;
import kr.co.adflow.push.util.DebugLog;
import kr.co.adflow.push.util.TRLogger;

/**
 * @author nadir93
 */
public class PushReceiver extends BroadcastReceiver {

    public static final int USIM_NOT_FOUND = 2001;
    public static final String USIM_NOT_FOUND_MESSAGE = "USIM이 없습니다";
    public static final int USIM_CHANGED = 2002;
    public static final String USIM_CHANGED_MESSAGE = "USIM이 변경되었습니다";
    public static final String KR_CO_KTPOWERTEL_PUSH_CONN_STATUS = "kr.co.ktpowertel.push.connStatus";
    // Debug TAG
    private static final String TAG = "PushReceiver";
    private static final int wakeLockHoldTime = 10000; // 웨이크락홀드타임 10초

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
        DebugLog.d("onReceive 시작(context = " + context + ",intent = " + intent + ")");
        try {
            if (intent == null || intent.getAction() == null) {
                DebugLog.e("action이 존재하지 않습니다");
                return;
            }

            if (intent.getAction().equals(
                    "android.intent.action.BOOT_COMPLETED")) {
                DebugLog.d("부팅 완료 작업을 시작합니다");
                //부팅 메시지
                TRLogger.i(TAG, "부팅 완료 작업을 시작합니다");

                PushPreference preference = new PushPreference(context);
                //부팅후 첫커넥션 표시
                preference.put(PushPreference.FIRSTCONNECTION, true);
                String oldPhoneNum = preference.getValue(PushPreference.PHONENUM, "");
                DebugLog.d("저장된 전화번호 = " + oldPhoneNum);
                //부팅 메시지
                TRLogger.i(TAG, "저장된 전화번호 = " + oldPhoneNum);

                //유심변경 체크
                TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String newPhoneNumber = tMgr.getLine1Number();
                DebugLog.d("전화번호 = " + newPhoneNumber);
                //부팅 메시지
                TRLogger.i(TAG, "전화번호 = " + newPhoneNumber);

                if (newPhoneNumber == null || newPhoneNumber.equals("")) {
                    DebugLog.d("USIM이 없습니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "USIM이 없습니다");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    DebugLog.d("토큰을 삭제했습니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "토큰을 삭제했습니다");
                    //sendBroadcast
                    sendBroadcast(context, USIM_NOT_FOUND_MESSAGE, USIM_NOT_FOUND, null);
                } else if (oldPhoneNum == null || oldPhoneNum.equals("")) {
                    DebugLog.d("이전 부팅때 저장된 전화번호가 없습니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "이전 부팅때 저장된 전화번호가 없습니다");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    DebugLog.d("토큰을 삭제했습니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "토큰을 삭제했습니다");
                    //sendBroadcast
                    sendBroadcast(context, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
                } else if (oldPhoneNum.equals(newPhoneNumber)) {
                    //같은유심
                    DebugLog.d("이전 부팅 전화번호와 같습니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "이전 부팅 전화번호와 같습니다");
                } else {
                    //유심변경
                    DebugLog.d("이전 부팅 전화번호와 다릅니다");
                    //부팅 메시지
                    TRLogger.i(TAG, "이전 부팅 전화번호와 다릅니다");
                    preference.put(PushPreference.PHONENUM, newPhoneNumber);
                    //remove token
                    preference.remove(PushPreference.TOKEN);
                    DebugLog.d("토큰을 삭제했습니다");

                    // TODO: 2016. 12. 1.
                    // 유심 변경 시나리오 추가
                    // remove server url info
                    preference.remove(PushPreference.SERVERURL);
                    DebugLog.d("서버접속 정보를 삭제했습니다");
                    // mark 강제적 신규 토큰 발급
                    preference.put(PushPreference.ISSUE_NEW_TOKEN, true);
                    DebugLog.d("강제적 토큰발급을 설정합니다");
                    //END

                    //부팅 메시지
                    TRLogger.i(TAG, "토큰을 삭제했습니다");
                    //sendBroadcast
                    sendBroadcast(context, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
                }

                //set cleanSession
                preference.put(PushPreference.CLEANSESSION, BuildConfig.CLEAN_SESSION);

                //알람설정
                DebugLog.d("keepAlive 알람을 설정합니다");
                AlarmManager service = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(context, PushReceiver.class);
                i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);
                service.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 1000,
                        1000 /** PushHandler.ALARM_INTERVAL*/, pending);
                DebugLog.d("keepAlive 알람이 설정되었습니다");
                TRLogger.i(TAG, "부팅 작업 완료");
                DebugLog.d("부팅 완료 작업을 종료합니다");
            } else if (intent.getAction().equals(
                    "kr.co.adflow.push.service.KEEPALIVE")) {
                // keepalive
                DebugLog.d("keepAlive 체크를 시작합니다");
                //PushPreference preference = new PushPreference(context);
                //boolean pttDebugLogin = preference.getValue(PushPreference.PTT_DebugLogIN, false);
                //DebugLog.d("pttDebugLogin = " + pttDebugLogin);
                //if (pttDebugLogin) {
                Bundle bundle = intent.getExtras();
                for (String key : bundle.keySet()) {
                    DebugLog.d(key + " is a key in the bundle");
                    DebugLog.d("value = " + bundle.get(key));
                }

                // get wakelock
                PowerManager pm = (PowerManager) context
                        .getSystemService(Context.POWER_SERVICE);
                WakeLock wakeLock = pm.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK, "KTPWakeLock");
                PushServiceImpl.setWakeLock(wakeLock);
                DebugLog.d("웨이크락 상태 = " + wakeLock.isHeld());
                if (!wakeLock.isHeld()) {
                    wakeLock.acquire(wakeLockHoldTime);
                    // ..screen will stay on during this section..
                    DebugLog.d("웨이크락 = " + wakeLock);
                    Intent i = new Intent(context, PushServiceImpl.class);
                    i.setAction("kr.co.adflow.push.service.KEEPALIVE");
                    context.startService(i);
                    // wakeLock.release();
                }
                //}
                DebugLog.d("keepAlive 체크를 종료합니다");
            }
//            else if (intent.getAction().equals("com.bns.pw.action.REG_STATE")) {
//                DebugLog.d("PTT로그인상태처리");
//                boolean DebugLogin = intent.getBooleanExtra("reg", false);
//                DebugLog.d("DebugLogin = " + DebugLogin);
//
//                //PushPreference preference = new PushPreference(context);
//                AlarmManager service = (AlarmManager) context
//                        .getSystemService(Context.ALARM_SERVICE);
//
//                if (DebugLogin) {
//
//                    DebugLog.d("keepAlive알람을설정합니다.");
//
//                    Intent i = new Intent(context, PushReceiver.class);
//                    i.setAction("kr.co.adflow.push.service.KEEPALIVE");
//                    PendingIntent pending = PendingIntent.getBroadcast(context, 0,
//                            i, PendingIntent.FLAG_UPDATE_CURRENT);
//                    Calendar now = Calendar.getInstance();
//                    service.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), 1000 * PushHandler.ALARM_INTERVAL, pending);
//                    DebugLog.d("keepAlive알람이설정되었습니다");
//
//
////                    //preference.put(PushPreference.PTT_DebugLogIN, true);
////                    // get wakelock
////                    PowerManager pm = (PowerManager) context
////                            .getSystemService(Context.POWER_SERVICE);
////                    WakeLock wakeLock = pm.newWakeLock(
////                            PowerManager.PARTIAL_WAKE_LOCK, "KTPWakeLock");
////                    PushServiceImpl.setWakeLock(wakeLock);
////                    DebugLog.d("웨이크락상태 = " + wakeLock.isHeld());
////                    if (!wakeLock.isHeld()) {
////                        wakeLock.acquire(wakeLockHoldTime);
////                        // ..screen will stay on during this section..
////                        DebugLog.d("웨이크락 = " + wakeLock);
////                        Intent keepAliveIntent = new Intent(context, PushServiceImpl.class);
////                        keepAliveIntent.setAction("kr.co.adflow.push.service.KEEPALIVE");
////                        context.startService(keepAliveIntent);
////                        // wakeLock.release();
////                    }
//                }
//                else {
//                    //PTT 로그인 정보
//                    //preference.put(PushPreference.PTT_DebugLogIN, false);
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
                DebugLog.e("적절한 처리 핸들러가 없습니다");
            }
        } catch (Exception e) {
            DebugLog.e("예외 상황 발생", e);
        }
        DebugLog.d("onReceive 종료()");
    }

    /**
     * @param context
     * @param eventMsg
     * @param eventCode
     * @throws Exception
     */
    private void sendBroadcast(Context context, String eventMsg, int eventCode, String eventInfo) throws Exception {
        DebugLog.d("sendBroadcast 시작(eventMsg = " + eventMsg + ", eventCode = " + eventCode + ", eventInfo = " + eventInfo + ")");
        Intent i = new Intent(KR_CO_KTPOWERTEL_PUSH_CONN_STATUS);
        i.putExtra("eventMsg", eventMsg);
        i.putExtra("eventCode", eventCode);
        if (eventInfo != null) {
            i.putExtra("eventInfo", eventInfo);
        }
        DebugLog.d("intent  = " + i);
        context.sendBroadcast(i);
        DebugLog.d("sendBroadcast 종료()");
    }
}
