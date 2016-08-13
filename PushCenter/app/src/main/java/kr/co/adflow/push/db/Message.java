package kr.co.adflow.push.db;

import java.util.Arrays;

/**
 * Created by nadir93 on 15. 1. 23..
 */
public class Message {

    private String msgID;
    private String serviceID;
    private String topic;
    private byte[] payload;
    private int qos;
    private int ack;
    private int broadcast;
    private int acked;
    private int broadcasted;
    private String receivedate;
    private String token;

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

    public int getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(int broadcast) {
        this.broadcast = broadcast;
    }

    public String getReceivedate() {
        return receivedate;
    }

    public void setReceivedate(String receivedate) {
        this.receivedate = receivedate;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public int getAcked() {
        return acked;
    }

    public void setAcked(int acked) {
        this.acked = acked;
    }

    public int getBroadcasted() {
        return broadcasted;
    }

    public void setBroadcasted(int broadcasted) {
        this.broadcasted = broadcasted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgID='" + msgID + '\'' +
                ", serviceID = '" + serviceID + '\'' +
                ", topic = '" + topic + '\'' +
                ", payload = " + Arrays.toString(payload) +
                ", qos = " + qos +
                ", ack = " + ack +
                ", broadcast = " + broadcast +
                ", acked = " + acked +
                ", broadcasted = " + broadcasted +
                ", receivedate = '" + receivedate + '\'' +
                ", token = '" + token + '\'' +
                '}';
    }
}
