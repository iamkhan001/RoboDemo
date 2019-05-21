package com.csjbot.blackgaga.jpush.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.csjbot.blackgaga.jpush.listener.JPushMessageListener;


/**
 * @author Ben
 * @date 2018/2/1
 */

public class JPushGetMessageReceiver extends BroadcastReceiver {

    private JPushMessageListener mListener;

    public void setListener(JPushMessageListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (mListener != null && !TextUtils.isEmpty(action) && bundle != null) {
            mListener.getJPushCallBack(action, bundle);
        }
    }
}
