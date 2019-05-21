package com.csjbot.blackgaga.advertisement.impl;

import com.csjbot.blackgaga.advertisement.event.AudioAction;
import com.csjbot.blackgaga.advertisement.event.AudioEvent;
import com.csjbot.blackgaga.advertisement.listener.AudioActionListener;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Ben
 * @date 2018/3/23
 */

public class AudioActionListenerImpl implements AudioActionListener {


    private AudioActionListenerImpl() {
    }

    public static AudioActionListenerImpl newInstance() {
        return new AudioActionListenerImpl();
    }

    @Override
    public void start() {
        EventBus.getDefault().post(new AudioEvent(AudioAction.START));
    }

    @Override
    public void stop() {
        EventBus.getDefault().post(new AudioEvent(AudioAction.STOP));
    }

    @Override
    public void updateData() {
        EventBus.getDefault().post(new AudioEvent(AudioAction.UPDATE_DATA));
    }

    @Override
    public void isCanShow(boolean isCanShow) {
        if (isCanShow){
            EventBus.getDefault().post(new AudioEvent(AudioAction.CAN_SHOW));
        }else {
            EventBus.getDefault().post(new AudioEvent(AudioAction.CAN_NOT_SHOW));
        }
    }
}
