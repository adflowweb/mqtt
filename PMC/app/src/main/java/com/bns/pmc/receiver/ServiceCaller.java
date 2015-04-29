package com.bns.pmc.receiver;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.bns.pmc.util.Log;
import com.bns.pmc.util.PMCType;

public class ServiceCaller {
    private static final String LOG_NAME = ServiceCaller.class.getSimpleName();

    private static ServiceCaller instance;
    private AlarmManager am;
    private Intent intent;
    private PendingIntent sender;
    private Intent screenIntent;
    private PendingIntent screenSender;
    private long interval = 5000;

    private ServiceCaller() {}
    public static synchronized ServiceCaller getInstance() {
        if (instance == null) {
            instance = new ServiceCaller();
        }
        return instance;
    }

/*    private static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/
    
    public static class ServiceCallBR extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Background Kill
        	ServiceCaller.getInstance().registScreenOnBR(context.getApplicationContext());

            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                ServiceCaller.getInstance().startCall(context);
            } else {
                Intent i = new Intent(context, PMCService.class);
                i.setAction(PMCType.BNS_PMC_SERVICE_REPEAT_ACTION);
                context.startService(i);
            }
        }
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void startCall(Context context) {
    	am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, ServiceCallBR.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, sender);
    }

    public void stopCall(Context context) {
    	am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, ServiceCallBR.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(sender);
        sender.cancel();
    }

    public boolean isCall(Context context) {
    	intent = new Intent(context, ServiceCallBR.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return sender != null;
    }

    /**
     * Check Screen on
     * @param context - must get ApplicationContext
     */
    public void registScreenOnBR(Context context) {
    	screenIntent = new Intent(context, screenOnBR.getClass());
        screenSender = PendingIntent.getBroadcast(context, 0, screenIntent, PendingIntent.FLAG_NO_CREATE);
        if (screenSender == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            context.registerReceiver(screenOnBR, filter);
        }
    }
    BroadcastReceiver screenOnBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Log.d(LOG_NAME, "ACTION_SCREEN_ON");
                ServiceCaller.getInstance().startCall(context);

            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Log.d(LOG_NAME, "ACTION_SCREEN_OFF");
                ServiceCaller.getInstance().startCall(context);
            }
        }
    };
}

