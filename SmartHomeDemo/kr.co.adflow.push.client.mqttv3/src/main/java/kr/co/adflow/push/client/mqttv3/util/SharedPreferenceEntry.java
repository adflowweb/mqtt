/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.util;

/**
 * Created by nadir93 on 15. 10. 14..
 */
public class SharedPreferenceEntry {

    // Keys for saving values in SharedPreferences.
    public static final String TOKEN = "token";
    public static final String SERVER = "server";
    public static final String PORT = "port";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String CLEAN_SESSION = "cleanSession";
    public static final String SSL = "ssl";

    /**
     * mqtt 세션 인증 토큰
     */
    private final String token;

    /**
     * IBM WMQ 서버 주소
     */
    private final String server;

    /**
     * IBM WMQ Mqtt Port
     */
    private final int port;

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

    /**
     * 보안 채널 사용
     */
    private final boolean ssl;


    public SharedPreferenceEntry(String token, String server, int port, int keepAlive, boolean cleanSession, boolean ssl) {
        this.token = token;
        this.server = server;
        this.port = port;
        this.keepAlive = keepAlive;
        this.cleanSession = cleanSession;
        this.ssl = ssl;
    }

    public String getToken() {
        return token;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public boolean isSsl() {
        return ssl;
    }

    @Override
    public String toString() {
        return "SharedPreferenceEntry{" +
                "token='" + token + '\'' +
                ", server='" + server + '\'' +
                ", port=" + port +
                ", keepAlive=" + keepAlive +
                ", cleanSession=" + cleanSession +
                ", ssl=" + ssl +
                '}';
    }
}
