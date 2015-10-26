package com.bns.pmc.exception;

/**
 * Created by nadir93 on 15. 7. 3..
 */
public class FileSizeLimitException extends Exception {

    public static String errorMsg = "파일크기가초과되었습니다";
    public static int errorCode = 3000;

    public FileSizeLimitException() {
        super();
    }
}