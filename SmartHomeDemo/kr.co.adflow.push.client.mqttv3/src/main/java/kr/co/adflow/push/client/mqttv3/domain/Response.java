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
public class Response {
    /*{
        "status": "ok",
        "code": 101200,
        "message": "토큰 등록이 완료되었습니다"
    }*/
    public String status;
    public Integer code;
    public String message;
    public String explaination;
    public String msgId;
    public String token;
    public String content;
    public String contentType;
    public Integer ack;
    public String sender;
    public String receiver;
    public Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", explaination='" + explaination + '\'' +
                ", msgId='" + msgId + '\'' +
                ", token='" + token + '\'' +
                ", content='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                ", ack=" + ack +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", data=" + data +
                '}';
    }
}
