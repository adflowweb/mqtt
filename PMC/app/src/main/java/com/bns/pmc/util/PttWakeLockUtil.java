package com.bns.pmc.util;

import android.content.Context;
import android.os.PowerManager;

public abstract class PttWakeLockUtil {

    private static final String TAG = "PttWakeLockUtil";

    private static PowerManager.WakeLock wakeLock = null;

    public static void acquire(Context context) {

        if (wakeLock != null) {
            wakeLock.release();
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            return;
        }
        // wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
         wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        // wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        //wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.setReferenceCounted(true);
        wakeLock.acquire();

        Log.d(TAG, "wakeLock acquire!");
    }

    public static void release(Context context) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;

        Log.d(TAG, "wakeLock release!");
    }

    public static boolean getHeld() {
        if (wakeLock == null) {
            return false;
        }
        return wakeLock.isHeld();
    }
}
