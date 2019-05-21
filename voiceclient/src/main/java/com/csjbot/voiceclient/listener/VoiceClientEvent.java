package com.csjbot.voiceclient.listener;

import com.csjbot.voiceclient.constant.VoiceClientConstant;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com
 * <p>
 * 用户层的事件，所有的用户事件都是这个类
 */
public class VoiceClientEvent {

    /**
     * @see VoiceClientConstant
     */
    public int eventType;

    public Object data;

    public VoiceClientEvent(int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public VoiceClientEvent(int eventType) {
        this.eventType = eventType;
        this.data = null;
    }
}
