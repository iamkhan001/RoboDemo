package com.csjbot.blackgaga.event;

/**
 * Created by jingwc on 2017/8/14.
 */
public interface IBus {
    void register(Object object);

    void unregister(Object object);

    void post(IEvent event);

    void postSticky(IEvent event);


    interface IEvent {
        int getTag();
    }
}
