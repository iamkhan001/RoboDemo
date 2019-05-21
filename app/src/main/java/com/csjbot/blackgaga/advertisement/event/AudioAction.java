package com.csjbot.blackgaga.advertisement.event;

/**
 * @author Ben
 * @date 2018/3/24
 */

public enum AudioAction {
    /**
     * 广告播放
     */
    START,
    /**
     * 广告停止
     */
    STOP,
    /**
     * 更新数据
     */
    UPDATE_DATA,
    /**
     * 不可以播放广告
     */
    CAN_NOT_SHOW,
    /**
     * 可以播放广告
     */
    CAN_SHOW
}
