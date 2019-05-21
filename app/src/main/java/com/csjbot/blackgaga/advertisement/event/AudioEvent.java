package com.csjbot.blackgaga.advertisement.event;

/**
 *
 * @author Ben
 * @date 2018/3/23
 */

public class AudioEvent {
    /**
     * 音频动作
     */
    private AudioAction action;


    public AudioEvent() {

    }

    public AudioEvent(AudioAction action) {
        this.action = action;
    }

    public AudioAction getAction() {
        return action;
    }

    public void setAction(AudioAction action) {
        this.action = action;
    }
}
