package com.csjbot.blackgaga.jpush.listener;

import android.os.Bundle;

/**
 * @author Ben
 * @date 2018/2/8
 */

public interface JPushMessageListener {

    /**
     * 获取JPush 返回的数据
     *
     * @param action
     * @param bundle
     */
    void getJPushCallBack(String action, Bundle bundle);
}
