/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.db;

import java.util.Arrays;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class Message {

    private String msgId;
    private String serviceId;
    private String topic;
    private byte[] payload;
    private int qos;
    private int ack;
    private String receivedate;
    private String token;
    private int agentAck;
    private int appAck;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(String receivedate) {
        this.receivedate = receivedate;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAgentAck() {
        return agentAck;
    }

    public void setAgentAck(int agentAck) {
        this.agentAck = agentAck;
    }

    public int getAppAck() {
        return appAck;
    }

    public void setAppAck(int appAck) {
        this.appAck = appAck;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId='" + msgId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", topic='" + topic + '\'' +
                ", payload=" + Arrays.toString(payload) +
                ", qos=" + qos +
                ", ack=" + ack +
                ", receivedate='" + receivedate + '\'' +
                ", token='" + token + '\'' +
                ", agentAck=" + agentAck +
                ", appAck=" + appAck +
                '}';
    }
}
