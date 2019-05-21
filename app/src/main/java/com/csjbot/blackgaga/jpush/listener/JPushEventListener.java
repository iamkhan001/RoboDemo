package com.csjbot.blackgaga.jpush.listener;


import com.csjbot.blackgaga.jpush.event.JPushEvent;

/**
 * @author Ben
 * @date 2018/2/24
 */

public interface JPushEventListener {
    /**
     * 得到事件
     * @param event
     */
    void onEvent(JPushEvent event);
}
