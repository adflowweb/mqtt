package kr.co.adflow.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 *
 */
public class PushPreference {

    public static final String TAG = "PushPreference";

    public static final String TOKEN = "token";
    public static final String SERVERURL = "serverURL";
    public static final String KEEPALIVE = "keepAlive";
    public static final String CLEANSESSION = "cleanSession";
    public static final String MSGID = "msgID";

    private final String PREF_NAME = "kr.co.adflow.push";

    static Context context;

    /**
     * @param cxt
     */
    public PushPreference(Context cxt) {
        Log.d(TAG, "PushPreference생성자시작(context=" + cxt + ")");
        context = cxt;
        Log.d(TAG, "PushPreference생성자종료)");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        Log.d(TAG, "put시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
        Log.d(TAG, "put종료()");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        Log.d(TAG, "put시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
        Log.d(TAG, "put종료()");
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, int value) {
        Log.d(TAG, "put시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
        Log.d(TAG, "put종료()");
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public String getValue(String key, String value) {
        Log.d(TAG, "getValue시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            String val = pref.getString(key, value);
            Log.d(TAG, "getValue종료(value=" + val + ")");
            return val;
        } catch (Exception e) {
            Log.d(TAG, "getValue종료(해당키가존재하지않습니다. return=" + value + ")");
            return value;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public int getValue(String key, int value) {
        Log.d(TAG, "getValue시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            int val = pref.getInt(key, value);
            Log.d(TAG, "getValue종료(value=" + val + ")");
            return val;
        } catch (Exception e) {
            Log.d(TAG, "getValue종료(해당키가존재하지않습니다. return=" + value + ")");
            return value;
        }
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public boolean getValue(String key, boolean value) {
        Log.d(TAG, "getValue시작(key=" + key + ", value=" + value + ")");
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            boolean val = pref.getBoolean(key, value);
            Log.d(TAG, "getValue종료(value=" + val + ")");
            return val;
        } catch (Exception e) {
            Log.d(TAG, "getValue종료(해당키가존재하지않습니다. return=" + value + ")");
            return value;
        }
    }
}
