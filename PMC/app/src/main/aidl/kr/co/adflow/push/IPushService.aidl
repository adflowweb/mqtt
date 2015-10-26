// IPushService.aidl
package kr.co.adflow.push;

// Declare any non-default types here with import statements

interface IPushService {
    	String preCheck(String sender, String topic);
    	String getSubscriptions();
    	boolean isConnected();
    	String unsubscribe(String topic);
    	String subscribe(String topic, int qos);
        String ack(String msgID, String tokenID);
        String existPMAByUFMI(String ufmi);
        String existPMAByUserID(String userID);
        String sendMsg(String sender, String receiver, String contentType, String content);
        String sendMsgWithOpts(String sender, String receiver, int qos, String contentType, String content, int contentLength, int expiry, boolean mms);
        String updateUFMI(String ufmi);
        String getGrpSubscribers(String topic);
        String getToken();
}
