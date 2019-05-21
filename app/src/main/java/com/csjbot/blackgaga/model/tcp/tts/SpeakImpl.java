package com.csjbot.blackgaga.model.tcp.tts;

import android.content.Context;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;
import com.csjbot.coshandler.listener.OnSpeakListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jingwc on 2017/9/21.
 */

public class SpeakImpl extends BaseImpl implements ISpeak {

    @Override
    public void startSpeaking(String text, OnSpeakListener listener) {
        robotManager.robot.startSpeaking(text, listener);
    }

    @Override
    public void stopSpeaking() {
        robotManager.robot.stopSpeaking();
    }

    @Override
    public void pauseSpeaking() {
        robotManager.robot.pauseSpeaking();
    }

    @Override
    public void resumeSpeaking() {
        robotManager.robot.resumeSpeaking();
    }

    @Override
    public boolean isSpeaking() {
        return robotManager.robot.isSpeaking();
    }

    @Override
    public void initSpeak(Context context, int speechType) {
        robotManager.robot.initSpeak(context, speechType);
    }

    @Override
    public boolean setSpeakerName(String name) {
        return robotManager.robot.setSpeakerName(name);
    }

    @Override
    public boolean setLanguage(Locale language) {
        return robotManager.robot.setLanguage(language);
    }

    @Override
    public ArrayList<String> getSpeakerNames(String language, String country) {
        return robotManager.robot.getSpeakerNames(language, country);
    }

    @Override
    public void setVolume(float volume) {
        robotManager.robot.setVolume(volume);
    }
}
