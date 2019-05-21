package com.csjbot.blackgaga.manual_control.event;

/**
 * @author ShenBen
 * @date 2018/11/17 9:58
 * @email 714081644@qq.com
 */

public class ServerEvent {

    private int tag;
    private Object msg;

    public ServerEvent(int tag, Object msg) {
        this.tag = tag;
        this.msg = msg;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
