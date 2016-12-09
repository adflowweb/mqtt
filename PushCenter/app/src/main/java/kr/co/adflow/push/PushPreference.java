package kr.co.adflow.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import kr.co.adflow.push.util.DebugLog;

/**
 *
 */
public class PushPreference {

    //public static final String TAG = "PushPreference";

    public static final String TOKEN = "token";
    public static final String SERVERURL = "serverURL";
    public static final String KEEPALIVE = "keepAlive";
    public static final String CLEANSESSION = "cleanSession";
    public static final String MSGID = "msgID";
    public static final String PHONENUM = "phoneNumber";
    public static final String FIRSTCONNECTION = "firstConnection";
    public static final String REGISTERED_PMC = "registerdPMC";
    public static final String ISSUE_NEW_TOKEN = "issueNewToken";

    private final String PREF_NAME = "kr.co.adflow.push";

    static Context context;

    /**
     * @param cxt
     */
    public PushPreference(Context cxt) {
        DebugLog.d("PushPreference 생성자 시작(context = " + cxt + ")");
        context = cxt;
        DebugLog.d("PushPreference 생성자 종료()");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        DebugLog.d("put 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
        DebugLog.d("put 종료()");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        DebugLog.d("put 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
        DebugLog.d("put 종료()");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, int value) {
        DebugLog.d("put 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
        DebugLog.d("put 종료()");
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public String getValue(String key, String value) {
        DebugLog.d("getValue 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            String val = pref.getString(key, value);
            DebugLog.d("getValue 종료(value = " + val + ")");
            return val;
        } catch (Exception e) {
            DebugLog.d("getValue 종료(해당키가 존재하지 않습니다 return = " + value + ")");
            return value;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public int getValue(String key, int value) {
        DebugLog.d("getValue 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            int val = pref.getInt(key, value);
            DebugLog.d("getValue 종료(value = " + val + ")");
            return val;
        } catch (Exception e) {
            DebugLog.d("getValue 종료(해당키가 존재하지 않습니다 return = " + value + ")");
            return value;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public boolean getValue(String key, boolean value) {
        DebugLog.d("getValue 시작(key = " + key + ", value = " + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            boolean val = pref.getBoolean(key, value);
            DebugLog.d("getValue 종료(value = " + val + ")");
            return val;
        } catch (Exception e) {
            DebugLog.d("getValue 종료(해당키가 존재하지 않습니다 return = " + value + ")");
            return value;
        }
    }

    public void remove(String key) {
        DebugLog.d("remove 시작(key = " + key + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
        DebugLog.d("remove 종료()");
    }
}
