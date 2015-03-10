package kr.co.adflow.push.exception;

/**
 * Created by nadir93 on 15. 3. 10..
 */
public class  MsgSizeLimitException extends Exception {

    public static String errorMsg = "메시지크기가초과되었습니다.";
    public static int errorCode = 3000;

    public MsgSizeLimitException() {
        super();
    }
}
