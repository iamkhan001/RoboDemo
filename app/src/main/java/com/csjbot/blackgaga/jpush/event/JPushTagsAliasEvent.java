package com.csjbot.blackgaga.jpush.event;

import cn.jpush.android.api.JPushMessage;

/**
 *
 * @author Ben
 * @date 2018/2/24
 */

public class JPushTagsAliasEvent {
    private int type;
    private JPushMessage message;

    public JPushTagsAliasEvent(int type, JPushMessage message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JPushMessage getMessage() {
        return message;
    }

    public void setMessage(JPushMessage message) {
        this.message = message;
    }
}
