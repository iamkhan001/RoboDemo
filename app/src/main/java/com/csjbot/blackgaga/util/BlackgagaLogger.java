package com.csjbot.blackgaga.util;

import com.csjbot.cosclient.utils.CosLogger;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BlackgagaLogger extends CosLogger implements Serializable {
    private static final long serialVersionUID = 1245457866563106956L;
    private static final Logger DEFAULT_LOGGER = Logger.getLogger("Blackgaga");

    public static void debug(String msg) {
        DEFAULT_LOGGER.log(Level.INFO, msg + getLineOut());
    }


    public static void debug(String format, Object... arguments) {
        DEFAULT_LOGGER.log(Level.INFO, format + getLineOut(), arguments);
    }

    // =============== info================/
    // =============== info================/
    public static void info(String msg) {
        DEFAULT_LOGGER.info(msg + getLineOut());
    }


    public static void info(String format, Object... arguments) {
        DEFAULT_LOGGER.log(Level.INFO, format + getLineOut(), arguments);
    }

    // =============== warn================/
    // =============== warn================/
    public static void warn(String msg) {
        DEFAULT_LOGGER.log(Level.WARNING,msg + getLineOut());
    }

    public static void warn(String msg, Throwable t) {
        DEFAULT_LOGGER.log(Level.WARNING,msg + getLineOut(), t);
    }

    public static void warn(String format, Object... arguments) {
        DEFAULT_LOGGER.log(Level.WARNING,format + getLineOut(), arguments);
    }

    // =============== error================/
    // =============== error================/
    public static void error(String msg) {
        DEFAULT_LOGGER.log(Level.SEVERE,msg + getLineOut());
    }

    public static void error(Throwable t) {
        DEFAULT_LOGGER.log(Level.SEVERE,"error", t);
    }

    public static void error(String msg, Throwable t) {
        DEFAULT_LOGGER.log(Level.SEVERE,msg + getLineOut(), t);
    }

    public static void error(String format, Object... arguments) {
        DEFAULT_LOGGER.log(Level.SEVERE,format + getLineOut(), arguments);
    }

    // =============== Debug Line================/
    // =============== Debug Line================/


    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private static String getLineOut() {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
                .getStackTrace();
        int currentIndex = -1;
        for (int i = 0; i < stackTraceElement.length; i++) {
            if (stackTraceElement[i].getMethodName().compareTo("getLineOut") == 0) {
                currentIndex = i + 2;
                break;
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(" [")
                .append(stackTraceElement[currentIndex].getMethodName())
                .append("] (")
                .append(stackTraceElement[currentIndex].getFileName())
                .append(":")
                .append(stackTraceElement[currentIndex].getLineNumber())
                .append(")");
        return builder.toString();
    }
}
