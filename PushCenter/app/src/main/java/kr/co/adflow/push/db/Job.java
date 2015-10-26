package kr.co.adflow.push.db;

/**
 * Created by nadir93 on 15. 2. 11..
 */
public class Job {

    public static int PUBLISH = 0;
    public static int SUBSCRIBE = 1;
    public static int UNSUBSCRIBE = 2;
    public static int BROADCAST = 3;
    public static int ACK = 4;

    private int id;
    private int type;
    private String topic;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
