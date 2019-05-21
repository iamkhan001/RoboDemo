package com.csjbot.blackgaga.push;

import android.content.Context;

import com.csjbot.blackgaga.BuildConfig;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by xiasuhuei321 on 2017/11/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BPush {
    public static Context context;
    public static BPushReceiver receiver;

    public static void init(Context context) {
        BPush.context = context;
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(context);

        receiver = new BPushReceiver();
    }

    /**
     * 设置别名，该操作是覆盖操作，非增量操作
     *
     * @param alias 别名
     */
    public static void setAlias(String alias) {
        int current = (int) System.currentTimeMillis();
        JPushInterface.setAlias(context, current, alias);

    }

    public static class BPushReceiver extends JPushMessageReceiver {
        /**
         * alias相关的操作会在此方法中回调结果。
         *
         * @param message alias相关操作返回的消息结果体
         */
        @Override
        public void onAliasOperatorResult(Context context, JPushMessage message) {
            int errorCode = message.getErrorCode();
            if (errorCode == 0) {
                // 操作成功
            } else {
                // 尝试重试
            }
        }
    }

}
