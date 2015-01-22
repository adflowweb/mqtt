// IPushService.aidl
package kr.co.adflow.push;

// Declare any non-default types here with import statements

interface IPushService {

        //mqttSession 연결
        //String connect(String userID, String deviceID, String ufmi);
        //preCheck
    	String preCheck(String sender, String topic);
    	//서브스크립션정보가져오기
    	String getSubscriptions();
        //mqttSession연결검사
    	boolean isConnected();
        //unsubscribe
    	String unsubscribe(String topic);
        //subscribe
    	String subscribe(String topic, int qos);
    	//ack
        String ack(String msgID, String tokenID);
        //existPMAByUFMI
        String existPMAByUFMI(String ufmi);
        //existPMAByUserID
        String existPMAByUserID(String userID);
        //sendMsg
        String sendMsg(String sender, String receiver, int qos, String contentType, String content, int expiry);
}
