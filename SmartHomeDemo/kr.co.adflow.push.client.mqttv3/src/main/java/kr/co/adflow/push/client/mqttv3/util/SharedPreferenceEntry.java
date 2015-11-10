/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.util;

import java.util.ArrayList;

/**
 * Created by nadir93 on 15. 10. 14..
 */
public class SharedPreferenceEntry {

    // Keys for saving values in SharedPreferences.
    public static final String TOKEN = "token";
    public static final String SERVER_URIS = "serverURIs";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String CLEAN_SESSION = "cleanSession";

    /**
     * mqtt 세션 인증 토큰
     */
    private final String token;

    /**
     * IBM WMQ 서버 주소
     */
    private final ArrayList<String> serverURIs;

    /**
     * mqtt 서버와 헬스 체크 주기
     * default : 240초 (4분)
     */
    private final int keepAlive;

    /**
     * 세션 관리 정책
     * default : false
     */
    private final boolean cleanSession;


    public SharedPreferenceEntry(String token, ArrayList<String> serverURIs, int keepAlive, boolean cleanSession) {
        this.token = token;
        this.serverURIs = serverURIs;
        this.keepAlive = keepAlive;
        this.cleanSession = cleanSession;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<String> getServerURIs() {
        return serverURIs;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    @Override
    public String toString() {
        return "SharedPreferenceEntry{" +
                "token='" + token + '\'' +
                ", serverURIs=" + serverURIs +
                ", keepAlive=" + keepAlive +
                ", cleanSession=" + cleanSession +
                '}';
    }
}
