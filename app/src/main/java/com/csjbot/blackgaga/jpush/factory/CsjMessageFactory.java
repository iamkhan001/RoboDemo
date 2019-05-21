package com.csjbot.blackgaga.jpush.factory;

import android.content.Context;

import com.csjbot.blackgaga.jpush.impl.JPushListenerImpl;
import com.csjbot.blackgaga.jpush.listener.JPushListener;


/**
 * @author Ben
 * @date 2018/2/8
 */

public class CsjMessageFactory {

    public static JPushListener newInstance(Context context) {
        return initJPush(context);
    }

    private static JPushListener initJPush(Context context) {
        return JPushListenerImpl.newInstance(context);
    }
}
