package com.csjbot.blackgaga.model.tcp.speech;

import com.csjbot.blackgaga.model.tcp.base.BaseImpl;

/**
 * Created by jingwc on 2017/9/21.
 */

public class SpeechImpl extends BaseImpl implements ISpeech {
    @Override
    public void openVideo() {
        robotManager.robot.reqProxy.openVideo();
    }

    @Override
    public void closeVideo() {
        robotManager.robot.reqProxy.closeVideo();
    }

    @Override
    public void startSpeechService() {
        robotManager.robot.reqProxy.startSpeechService();
    }

    @Override
    public void closeSpeechService() {
        robotManager.robot.reqProxy.closeSpeechService();
    }

    @Override
    public void startIsr() {
        robotManager.robot.reqProxy.startIsr();
    }

    @Override
    public void stopIsr() {
        robotManager.robot.reqProxy.stopIsr();
    }

    @Override
    public void openMicro() {
        robotManager.robot.reqProxy.openMicro();
    }

    @Override
    public void speak(String content) {
        robotManager.robot.reqProxy.speak(content);
    }

    @Override
    public void stopSpeak() {
        robotManager.robot.reqProxy.stopSpeak();
    }

    @Override
    public void setLanguage(String language) {
        robotManager.robot.reqProxy.setLanguage(language);
    }

    @Override
    public void setLanguageAndAccent(String language, String accent) {
        robotManager.robot.reqProxy.setLanguageAndAccent(language,accent);
    }

    @Override
    public void getResult(String text) {
        robotManager.robot.reqProxy.getResult(text);
    }
}
