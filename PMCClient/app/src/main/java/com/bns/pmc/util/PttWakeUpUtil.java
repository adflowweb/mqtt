package com.bns.pmc.util;

import android.content.Context;
import android.os.PowerManager;

public abstract class PttWakeUpUtil {

    private static final String TAG = "PttWakeUpUtil";

    private static PowerManager.WakeLock wakeUp = null;

    public static void acquire(Context context) {

//        if (wakeUp != null) {
//            wakeUp.release();
//        }


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeUp = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, TAG);
        //wakeUp.acquire();
        wakeUp.acquire(1000);
        Log.d(TAG, "wakeUp acquire!");
    }

    public static void release() {
        if (wakeUp != null) {
            wakeUp.release();
        }
        wakeUp = null;
        Log.d(TAG, "wakeUp release!");
    }
}
