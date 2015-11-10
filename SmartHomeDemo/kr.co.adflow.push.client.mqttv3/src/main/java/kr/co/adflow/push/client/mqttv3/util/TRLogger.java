package kr.co.adflow.push.client.mqttv3.util;

import android.app.Application;
import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import kr.co.adflow.push.client.mqttv3.service.impl.PushServiceImpl;

/**
 * Created by nadir93 on 15. 11. 9..
 */
public class TRLogger {

    private static final String TAG = "LogWrapper";
    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "ADFTRLog%g.txt";
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            DebugLog.d("저장소위치" + PushServiceImpl.getInstance().getFilesDir());
            fileHandler = new FileHandler(PushServiceImpl.getInstance().getFilesDir() +
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

            logger = Logger.getLogger(TRLogger.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            DebugLog.d("TRLogger가 초기화 되었습니다");
        } catch (IOException e) {
            DebugLog.e("TRLogger 초기화에 실패하였습니다");
            e.printStackTrace();
        }
    }


    public static void i(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }

        DebugLog.i(msg);
    }

    public static void e(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.e(msg);
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
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.d(msg);
    }

    public static void w(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.w(msg);
    }
}
