package com.csjbot.blackgaga.jpush.diaptch.event;

/**
 * Created by jingwc on 2018/3/2.
 */

public interface IEvent {
    void pushEvent(String id, int sid, String data);
}
