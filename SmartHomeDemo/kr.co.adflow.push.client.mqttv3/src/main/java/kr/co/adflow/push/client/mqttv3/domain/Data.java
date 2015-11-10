/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.domain;

/**
 * Created by nadir93 on 15. 10. 14..
 */
public class Data {
    public String token;
    public Boolean rightAway;
    public Boolean connected;
    public Boolean ssl;
    public Integer keepAlive;
    public String server;
    public Integer port;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getRightAway() {
        return rightAway;
    }

    public void setRightAway(Boolean rightAway) {
        this.rightAway = rightAway;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public Integer getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Integer keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Data{" +
                "token='" + token + '\'' +
                ", rightAway=" + rightAway +
                ", connected=" + connected +
                ", ssl=" + ssl +
                ", keepAlive=" + keepAlive +
                ", server='" + server + '\'' +
                ", port=" + port +
                '}';
    }
}
