package com.bns.pmc.exception;

/**
 * Created by nadir93 on 15. 7. 3..
 */
public class AuthFailException extends Exception {

    public static String errorMsg = "인증에실패하였습니다";
    public static int errorCode = 3001;

    public AuthFailException(String detailMessage) {
        super(detailMessage);
    }
}
