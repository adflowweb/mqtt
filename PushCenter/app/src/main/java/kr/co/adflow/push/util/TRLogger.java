package kr.co.adflow.push.util;

import android.content.Context;
import android.os.Binder;

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
 * Created by nadir93 on 15. 11. 9..
 */
public class TRLogger {

    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "ADFTRLog%g.log";
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    private static boolean initialized = false;

    static {
        try {
            if (PushServiceImpl.getInstance() != null) {
                init(PushServiceImpl.getInstance());
            }
        } catch (Exception e) {
            DebugLog.e("TRLogger 초기화에 실패하였습니다", e);
        }
    }

    public static void init(Context context) throws Exception {
        DebugLog.d("TRLog저장위치=" + context.getFilesDir());
        fileHandler = new FileHandler(context.getFilesDir() + "/" +
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
        initialized = true;
        DebugLog.d("TRLogger가 초기화 되었습니다");
    }

    /**
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("I/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }

        DebugLog.i(msg);
    }

    /**
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("E/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.e(msg);
    }

    /**
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.v(msg);
    }

    /**
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("D/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.d(msg);
    }

    /**
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format("W/%s(%d): %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        DebugLog.w(msg);
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
