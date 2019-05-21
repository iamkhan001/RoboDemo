package com.csjbot.blackgaga.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.csjbot.blackgaga.util.BlackgagaLogger;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xiasuhuei321 on 2017/11/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BPushReceiver extends BroadcastReceiver {
    public static final String TAG = "BPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            BlackgagaLogger.debug(TAG + "JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            BlackgagaLogger.debug(TAG + "接受到推送下来的自定义消息");
            printCustomMessage(bundle);

            BMessageHandler.getInstance().handlerMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            BlackgagaLogger.debug(TAG + "接受到推送下来的通知");
            receivingNotification(context, intent.getExtras());

            BMessageHandler.getInstance().handlerMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            BlackgagaLogger.debug(TAG + "用户点击打开了通知");
        } else {
            BlackgagaLogger.debug(TAG + "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        BlackgagaLogger.debug(TAG + " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        BlackgagaLogger.debug(TAG + "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        BlackgagaLogger.debug(TAG + "extras : " + extras);
    }

    private void printCustomMessage(Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        BlackgagaLogger.debug("自定义消息标题：" + title);
        BlackgagaLogger.debug("自定义消息内容：" + message);
    }
}
