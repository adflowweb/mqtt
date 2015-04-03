package kr.co.adflow.push.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import kr.co.adflow.push.PushPreference;
import kr.co.adflow.push.handler.PushHandler;
import kr.co.adflow.push.receiver.PushReceiver;

/**
 * Created by nadir93 on 15. 1. 7..
 */
public class MainActivity extends Activity {
    // TAG for debug
    public static final String TAG = "PushMainActivity";
    public static final String KR_CO_KTPOWERTEL_PUSH_CONN_STATUS = "kr.co.ktpowertel.push.connStatus";
    public static final int USIM_NOT_FOUND = 2001;
    public static final String USIM_NOT_FOUND_MESSAGE = "USIM이 없습니다.";
    public static final int USIM_CHANGED = 2002;
    public static final String USIM_CHANGED_MESSAGE = "USIM이 변경되었습니다.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PushPreference preference = new PushPreference(this);
        //첫커넥션 표시
        preference.put(PushPreference.FIRSTCONNECTION, true);
        String oldPhoneNum = preference.getValue(PushPreference.PHONENUM, "");
        Log.d(TAG, "저장된전화번호=" + oldPhoneNum);

        //유심변경 체크
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String newPhoneNumber = tMgr.getLine1Number();
        Log.d(TAG, "전화번호=" + newPhoneNumber);

        if (newPhoneNumber == null || newPhoneNumber.equals("")) {
            Log.d(TAG, "USIM이없습니다.");
            preference.put(PushPreference.PHONENUM, newPhoneNumber);
            //remove token
            preference.remove(PushPreference.TOKEN);
            Log.d(TAG, "토큰을삭제했습니다.");
            //sendBroadcast
            try {
                sendBroadcast(this, USIM_NOT_FOUND_MESSAGE, USIM_NOT_FOUND, null);
            } catch (Exception e) {
                Log.e(TAG, "예외상황발생", e);
            }
        } else if (oldPhoneNum == null || oldPhoneNum.equals("")) {
            Log.d(TAG, "이전부팅때저장된전화번호가없습니다.");
            preference.put(PushPreference.PHONENUM, newPhoneNumber);
            //remove token
            preference.remove(PushPreference.TOKEN);
            Log.d(TAG, "토큰을삭제했습니다.");
            //sendBroadcast
            try {
                sendBroadcast(this, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
            } catch (Exception e) {
                Log.e(TAG, "예외상황발생", e);
            }
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
            try {
                sendBroadcast(this, USIM_CHANGED_MESSAGE, USIM_CHANGED, "{\"oldPhone\":\"" + oldPhoneNum + "\",\"newPhone\":\"" + newPhoneNumber + "\"}");
            } catch (Exception e) {
                Log.e(TAG, "예외상황발생", e);
            }
        }

        //서버정보저장
        preference.put(PushPreference.SERVERURL, PushHandler.MQTT_SERVER_URL);
        //set cleanSession
        preference.put(PushPreference.CLEANSESSION, PushHandler.CLEAN_SESSION);


        Log.d(TAG, "keepAlive알람을설정합니다.");
        AlarmManager service = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, PushReceiver.class);
        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
        PendingIntent pending = PendingIntent.getBroadcast(this, 0,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        service.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                1000 * PushHandler.ALARM_INTERVAL, pending);
        Log.d(TAG, "keepAlive알람이설정되었습니다");

//        //기본정보세팅
//        PushPreference preference = new PushPreference(this);
//        String oldPhoneNum = preference.getValue(PushPreference.PHONENUM, null);
//        Log.d(TAG, "저장된전화번호=" + oldPhoneNum);
//
//        //유심변경 체크
//        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String newPhoneNumber = tMgr.getLine1Number();
//        Log.d(TAG, "전화번호=" + newPhoneNumber);
//
//        if (oldPhoneNum == null || oldPhoneNum.equals("")) {
//            Log.d(TAG, "이전부팅때저장된전화번호가없습니다.");
//            preference.put(PushPreference.PHONENUM, newPhoneNumber);
//            //remove token
//            preference.remove(PushPreference.TOKEN);
//            Log.d(TAG, "토큰을삭제했습니다.");
//        } else if (oldPhoneNum.equals(newPhoneNumber)) {
//            //같은유심
//            Log.d(TAG, "이전부팅전화번호와같습니다.");
//        } else {
//            //유심변경
//            Log.d(TAG, "이전부팅전화번호와다릅니다.");
//            preference.put(PushPreference.PHONENUM, newPhoneNumber);
//            //remove token
//            preference.remove(PushPreference.TOKEN);
//            Log.d(TAG, "토큰을삭제했습니다.");
//        }
//
//        //서버정보저장
//        preference.put(PushPreference.SERVERURL, PushHandler.MQTT_SERVER_URL);
//        //set cleanSession
//        preference.put(PushPreference.CLEANSESSION, PushHandler.CLEAN_SESSION);
//
//        Log.d(TAG, "keepAlive알람을설정합니다.");
//        AlarmManager service = (AlarmManager) this
//                .getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(this, PushReceiver.class);
//        i.setAction("kr.co.adflow.push.service.KEEPALIVE");
//        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        service.setRepeating(AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + 1000,
//                1000 * PushHandler.ALARM_INTERVAL, pending);
//        Log.d(TAG, "keepAlive알람이설정되었습니다");

        // display toast
        Toast toast = Toast.makeText(getApplicationContext(),
                "푸시서비스가 시작되었습니다.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        // finish
        this.finish();
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
