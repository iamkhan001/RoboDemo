package com.csjbot.blackgaga.model.tcp.proxy;

import android.content.Context;

import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.coshandler.listener.OnSpeakListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by jingwc on 2017/9/21.
 */

public class SpeakProxy implements ISpeak {

    private static class SpeakHolder {
        private static final SpeakProxy INSTANCE = new SpeakProxy();
    }

    public static final SpeakProxy getInstance() {
        return SpeakHolder.INSTANCE;
    }

    public ISpeak speak;

    private SpeakProxy() {
        speak = ServerFactory.getSpeakInstance();
    }

    @Override
    public void startSpeaking(String text, OnSpeakListener listener) {
        speak.startSpeaking(text, listener);
    }

    @Override
    public void stopSpeaking() {
        speak.stopSpeaking();
    }

    @Override
    public void pauseSpeaking() {
        speak.pauseSpeaking();
    }

    @Override
    public void resumeSpeaking() {
        speak.resumeSpeaking();
    }

    @Override
    public boolean isSpeaking() {
        return speak.isSpeaking();
    }

    @Override
    public void initSpeak(Context context, int speechType) {
        speak.initSpeak(context, speechType);
    }

    @Override
    public boolean setSpeakerName(String name) {
        return speak.setSpeakerName(name);
    }

    @Override
    public boolean setLanguage(Locale language) {
        return speak.setLanguage(language);
    }

    /**
     * 返回tts的发声人列表
     *
     * @param language 语言
     * @param country  国家或者地区
     * @return tts的发声人列表，只会返回空，永不为null
     */
    @Override
    public ArrayList<String> getSpeakerNames(String language, String country) {
        ArrayList<String> list = speak.getSpeakerNames(language, country);
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public void setVolume(float volume) {
        speak.setVolume(volume);
    }
}
