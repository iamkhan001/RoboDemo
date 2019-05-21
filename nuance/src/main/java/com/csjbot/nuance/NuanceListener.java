package com.csjbot.nuance;

/**
 * Created by Administrator on 2018/8/14.
 */

public interface NuanceListener {
    void onStartedRecording();
    void onFinishedRecording();
    void onRecognition(String text);
    void onSuccess();
    void onError(String e);
}
