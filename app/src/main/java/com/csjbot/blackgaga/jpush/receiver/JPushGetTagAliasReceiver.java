package com.csjbot.blackgaga.jpush.receiver;

import android.content.Context;

import com.csjbot.blackgaga.jpush.listener.JPushTagsAliasListener;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 *
 * @author Ben
 * @date 2018/2/1
 */

public class JPushGetTagAliasReceiver extends JPushMessageReceiver {

    private JPushTagsAliasListener mListener;

    public void setListener(JPushTagsAliasListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        if (mListener != null && jPushMessage != null) {
            mListener.onTagOperatorResult(jPushMessage);
        }

    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        if (mListener != null && jPushMessage != null) {
            mListener.onCheckTagOperatorResult(jPushMessage);
        }

    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        if (mListener != null && jPushMessage != null) {
            mListener.onAliasOperatorResult(jPushMessage);
        }
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
        if (mListener != null && jPushMessage != null) {
            mListener.onMobileNumberOperatorResult(jPushMessage);
        }
    }
}
