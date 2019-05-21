package com.csjbot.blackgaga.jpush.event;

/**
 *
 * @author Ben
 * @date 2018/2/24
 */

public class JPushMessageEvent {
    private int type;
    private String msgContent;

    public JPushMessageEvent(int type, String msgContent) {
        this.type = type;
        this.msgContent = msgContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
