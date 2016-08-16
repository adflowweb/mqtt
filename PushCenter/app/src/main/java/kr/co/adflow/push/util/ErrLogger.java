/*
 * Copyright (C) ADFlow, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by nadir93 <typark@adflow.co.kr>, October 2015
 */

package kr.co.adflow.push.util;

import android.os.Binder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import kr.co.adflow.push.service.impl.PushServiceImpl;


/**
 * Created by nadir93 on 15. 11. 11..
 */
public class ErrLogger {

    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "ADFErrLog%g.log";
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            DebugLog.d("ErrLog저장위치=" + PushServiceImpl.getInstance().getFilesDir());
            fileHandler = new FileHandler(PushServiceImpl.getInstance().getFilesDir() + "/" +
                    LOG_FILE_NAME, LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());

                    StringBuilder ret = new StringBuilder(80);
                    ret.append(formatter.format(date));
                    ret.append(r.getMessage());
                    return ret.toString();
                }
            });

            logger = Logger.getLogger(ErrLogger.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            DebugLog.d("ErrLogger가 초기화 되었습니다");
        } catch (IOException e) {
            DebugLog.e("ErrLogger 초기화에 실패하였습니다", e);
        }
    }


    public static void i(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("I/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }

        DebugLog.i(msg);
    }

    public static void e(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.SEVERE, String.format("E/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.e(msg);
    }

    public static void e(String tag, String msg, Throwable th) {
        if (logger != null) {
            logger.log(Level.SEVERE, String.format("E/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg + '\n' + getStackTraceString(th)));
        }
        DebugLog.e(msg, th);
    }

    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.v(msg);
    }

    public static void d(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("D/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.d(msg);
    }

    public static void w(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("W/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.w(msg);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
