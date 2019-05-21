package com.csjbot.blackgaga.jpush.event;

/**
 * @author Ben
 * @date 2018/2/24
 */

public class JPushEvent {
    private int eventType;
    private Object data;

    public JPushEvent(int eventType, Object data) {
        this.eventType = eventType;
        this.data = data;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
