package kr.co.adflow.push.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

public class MyReceiver extends BroadcastReceiver {

    public static String TAG = "MyReceiver";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive 시작 (context=" + context + ",intent=" + intent + ")");
        try {
            Bundle bundle = intent.getExtras();
            for (String key : bundle.keySet()) {
                Log.d(TAG, key + " is a key in the bundle");
                if (key.equals("data")) {
                    JSONObject obj = new JSONObject((String) bundle.get(key));
                    Log.d(TAG, "data=" + obj);
                } else {
                    Log.d(TAG, "value=" + bundle.get(key));
                }

            }

            if (bundle.getBoolean("ack")) {
                Log.d(TAG, "MainActivity.mainActivity.mBinder=" + MainActivity.mainActivity.mBinder);
                if (MainActivity.mainActivity.mBinder != null) {
                    MainActivity.mainActivity.mBinder.ack(bundle.getString("msgID"), bundle.getString("token"));
                }
            }


        } catch (Exception e) {
            Log.e(TAG, " 예외상황발생 ", e);
        }
        Log.d(TAG, "onReceive 종료 ()");
    }
}