package com.csjbot.coshandler.listener;

/**
 * Created by jingwc on 2017/9/5.
 */

public interface OnInitListener {
    void success();
    void faild();
    void timeout();
    void disconnect();
}
