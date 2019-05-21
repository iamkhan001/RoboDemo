package com.csjbot.blackgaga.advertisement.manager;


import com.csjbot.blackgaga.advertisement.impl.AudioActionListenerImpl;
import com.csjbot.blackgaga.advertisement.listener.AudioActionListener;

/**
 * @author Ben
 * @date 2018/3/23
 */

public class AdvertisementManager {

    private static final String TAG = "AdvertisementManager";
    private static AdvertisementManager mManager;
    private AudioActionListener mListener;

    private AdvertisementManager() {
        mListener = AudioActionListenerImpl.newInstance();
    }

    public static AdvertisementManager getInstance() {
        return mManager == null ? (mManager = newManager()) : mManager;
    }

    private static AdvertisementManager newManager() {
        return new AdvertisementManager();
    }

    /**
     * 广告播放
     */
    public void start() {
        if (mListener != null) {
            mListener.start();
        }
    }

    /**
     * 广告停止
     */
    public void stop() {
        if (mListener != null) {
            mListener.stop();
        }
    }

    /**
     * 更新广告数据
     */
    public void updateData() {
        if (mListener != null) {
            mListener.updateData();
        }
    }

    /**
     * 设置广告是否可以播放
     *
     * @param isCanShow true:可以播放,false:不可以播放(不管是否无人，都不播放)
     */
    public void isCanShow(boolean isCanShow) {
        if (mListener != null) {
            mListener.isCanShow(isCanShow);
        }
    }
}
