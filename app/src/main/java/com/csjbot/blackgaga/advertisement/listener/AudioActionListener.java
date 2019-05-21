package com.csjbot.blackgaga.advertisement.listener;

/**
 * @author Ben
 * @date 2018/3/23
 */

public interface AudioActionListener {

    /**
     * 广告播放
     */
    void start();

    /**
     * 广告停止
     */
    void stop();

    /**
     * 更新广告数据
     */
    void updateData();

    /**
     * 广告播放是否是可以播放
     *
     * @param isCanShow true:广告播放停止,false:音乐正常播放
     */
    void isCanShow(boolean isCanShow);
}
