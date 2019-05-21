package com.csjbot.blackgaga.p2pcamera.listener;

/**
 *
 * @author Ben
 * @date 2018/1/10
 */

public interface GetMessageListener {
    /**
     *
     * @param title  网页返回内容的主题
     * @param message 网页返回内容
     */
    void getMsg(String title, String message);
}
