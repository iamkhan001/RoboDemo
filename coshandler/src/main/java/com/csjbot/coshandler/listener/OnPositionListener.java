package com.csjbot.coshandler.listener;


/**
 * Created by jingwc on 2017/9/21.
 */

public interface OnPositionListener {
    void positionInfo(String json);

    void moveResult(String json);

    /**
     * 移动到某点的消息是否下发成功
     */
    void moveToResult(String json);

    /**
     * 取消任务是否成功
     */
    void cancelResult(String json);
}
