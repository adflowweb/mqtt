/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.util;

import android.content.SharedPreferences;

import kr.co.adflow.push.client.mqttv3.BuildConfig;

/**
 * Created by nadir93 on 15. 10. 14..
 */
public class SharedPreferencesHelper {


    // The injected SharedPreferences implementation to use for persistence.
    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
     * @param sharedPreferenceEntry
     * @return
     */
    public boolean saveMqttInfo(SharedPreferenceEntry sharedPreferenceEntry) {

        // Start a SharedPreferences transaction.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SharedPreferenceEntry.TOKEN, sharedPreferenceEntry.getToken());
        editor.putString(SharedPreferenceEntry.SERVER, sharedPreferenceEntry.getServer());
        editor.putInt(SharedPreferenceEntry.PORT, sharedPreferenceEntry.getPort());
        editor.putInt(SharedPreferenceEntry.KEEP_ALIVE, sharedPreferenceEntry.getKeepAlive());
        editor.putBoolean(SharedPreferenceEntry.CLEAN_SESSION, sharedPreferenceEntry.isCleanSession());
        editor.putBoolean(SharedPreferenceEntry.SSL, sharedPreferenceEntry.isSsl());

        // Commit changes to SharedPreferences.
        return editor.commit();
    }

    /**
     * @return
     */
    public SharedPreferenceEntry getMqttInfo() {
        // Get data from the SharedPreferences.
        String token = mSharedPreferences.getString(SharedPreferenceEntry.TOKEN, null);
        String server =
                mSharedPreferences.getString(SharedPreferenceEntry.SERVER, null);
        int port = mSharedPreferences.getInt(SharedPreferenceEntry.PORT, BuildConfig.DEFAULT_PORT);
        int keepAlive = mSharedPreferences.getInt(SharedPreferenceEntry.KEEP_ALIVE, BuildConfig.DEFAULT_KEEP_ALIVE);
        boolean cleanSession = mSharedPreferences.getBoolean(SharedPreferenceEntry.CLEAN_SESSION, false);
        boolean ssl = mSharedPreferences.getBoolean(SharedPreferenceEntry.SSL, false);

        // Create and fill a SharedPreferenceEntry model object.
        return new SharedPreferenceEntry(token, server, port, keepAlive, cleanSession, ssl);
    }

    /**
     * @param token
     * @return
     */
    public boolean saveToken(String token) {
        // Start a SharedPreferences transaction.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SharedPreferenceEntry.TOKEN, token);
        // Commit changes to SharedPreferences.
        return editor.commit();
    }

    /**
     * @param keepAlive
     * @return
     */
    public boolean saveKeepAlive(int keepAlive) {
        // Start a SharedPreferences transaction.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(SharedPreferenceEntry.KEEP_ALIVE, keepAlive);
        // Commit changes to SharedPreferences.
        return editor.commit();
    }
}
