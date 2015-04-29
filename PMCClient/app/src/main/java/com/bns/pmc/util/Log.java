package com.bns.pmc.util;

public class Log {

    public static boolean enableLog = true;

    public static String getClassNameLine(String msg) {
        StringBuffer stacktrace = new StringBuffer();
        String result = null;
        try {
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            stacktrace.append(stackTrace[2].getFileName());
            stacktrace.append(".");
            stacktrace.append(stackTrace[2].getLineNumber());
            stacktrace.append(": ");
            stacktrace.append(msg);
            result = stacktrace.toString();
        } catch (Exception e) {
            result = msg;
        }
        return result;
    }

    public static String getTag(String tag) {
        return "BNS|" + tag;
    }

    public static void v(String tag, String msg) {
        if (enableLog)
            android.util.Log.v(getTag(tag), getClassNameLine(msg));
    }

    public static void d(String tag, String msg) {
        if (enableLog)
            android.util.Log.d(getTag(tag), getClassNameLine(msg));
    }

    public static void i(String tag, String msg) {
        if (enableLog)
            android.util.Log.i(getTag(tag), getClassNameLine(msg));
    }

    public static void w(String tag, String msg) {
        if (enableLog)
            android.util.Log.w(getTag(tag), getClassNameLine(msg));
    }

    public static void e(String tag, String msg) {
        if (enableLog)
            android.util.Log.e(getTag(tag), getClassNameLine(msg));
    }
}
