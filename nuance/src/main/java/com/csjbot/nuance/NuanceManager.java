package com.csjbot.nuance;

import android.content.Context;
import android.content.Intent;

/**
 *
 */

public class NuanceManager {

    private volatile static NuanceManager nuanceManager;

    public static NuanceManager getInstance() {
        if (nuanceManager == null) {
            synchronized (NuanceManager.class) {
                if (nuanceManager == null) {
                    nuanceManager = new NuanceManager();
                }
            }
        }
        return nuanceManager;
    }

    static final String RECOGNIZE = "RECOGNIZE";
    static final String STOP_RECORDING = "STOP_RECORDING";
    static final String CANCEL = "CANCEL";

    public static String LANGUAGE = Languages.CHINESE;

    private NuanceListener nuanceListener;

    public void setNuanceListener(NuanceListener listener) {
        nuanceListener = listener;
    }

    void pushRecognitionText(String text) {
        if (nuanceListener != null) {
            nuanceListener.onRecognition(text);
        }
    }

    void pushStart() {
        if (nuanceListener != null) {
            nuanceListener.onStartedRecording();
        }
    }

    void pushFinish() {
        if (nuanceListener != null) {
            nuanceListener.onFinishedRecording();
        }
    }

    void pushSuccess() {
        if (nuanceListener != null) {
            nuanceListener.onSuccess();
        }
    }

    void pushError(String e) {
        if (nuanceListener != null) {
            nuanceListener.onError(e);
        }
    }

    private NuanceManager() {

    }

    public void startNuanceService(Context context) {
        context.startService(new Intent(context, NuanceService.class));
    }

    public void stopNuanceService(Context context) {
        context.stopService(new Intent(context, NuanceService.class));
    }

    public void recognize(Context context) {
        context.sendBroadcast(new Intent(RECOGNIZE));
    }

    public void stopRecording(Context context) {
        context.sendBroadcast(new Intent(STOP_RECORDING));
    }

    public void cancel(Context context) {
        context.sendBroadcast(new Intent(CANCEL));
    }

    public static class Config {

    }

    public static class Languages {
        public static final String CHINESE = "cmn-CHN";
        public static final String JAPANESE = "jpn-JPN";
        public static final String ENGLISH_US = "eng-USA";
        public static final String KOREAN = "kor-KOR";
        public static final String FRENCH = "fr_rFR";
        public static final String SPANISH = "es_rES";
        public static final String PORTUGUESE = "pt-rPT";
        public static final String INDONESIA = "in-rID";
        public static final String RUSSIAN = "ru-rRU";
    }

}
