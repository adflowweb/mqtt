/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.client.mqttv3.db;

/**
 * Created by nadir93 on 15. 10. 21..
 */
public class Job {

    public static int PUBLISH = 0;
    public static int SUBSCRIBE = 1;
    public static int UNSUBSCRIBE = 2;
    public static int BROADCAST = 3;
    public static int ACK = 4;

    private int id;
    private int type;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
