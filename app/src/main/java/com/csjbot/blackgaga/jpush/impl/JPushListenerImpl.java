package com.csjbot.blackgaga.jpush.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.blackgaga.jpush.listener.JPushListener;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Ben
 * @date 2018/2/8
 */

public class JPushListenerImpl implements JPushListener {

    private static final String TAG = "JPushListenerImpl";
    private Context mContext = null;

    /**
     * 初始化JPush
     *
     * @param context 需要传入getApplicationContext();
     */
    private JPushListenerImpl(Context context) {
        this.mContext = context;
    }

    public static JPushListenerImpl newInstance(Context context) {
        return new JPushListenerImpl(context);
    }


    @Override
    public void init() {
        if (mContext != null) {
            JPushInterface.init(mContext);
        } else {
            Log.e(TAG, "JPushInterface.init(mContext),失败");
        }
    }

    @Override
    public void stopPush() {
        if (mContext != null) {
            JPushInterface.stopPush(mContext);
        } else {
            Log.e(TAG, "JPushInterface.stopPush(mContext),失败");
        }
    }

    @Override
    public void resumePush() {
        if (mContext != null) {
            JPushInterface.resumePush(mContext);
        } else {
            Log.e(TAG, "JPushInterface.resumePush(mContext),失败");
        }
    }

    @Override
    public boolean isPushStopped() {
        return mContext != null && JPushInterface.isPushStopped(mContext);
    }

    @Override
    public String getRegistrationId() {
        if (mContext != null) {
            if (!TextUtils.isEmpty(JPushInterface.getRegistrationID(mContext))) {
                return JPushInterface.getRegistrationID(mContext);
            } else {
                return "";
            }
        }
        return "";
    }

    @Override
    public void setTags(int sequence, Set<String> set) {
        if (mContext!=null){
            JPushInterface.setTags(mContext,sequence,set);
        }else {
            Log.e(TAG, "JPushInterface.setTags(mContext,sequence,set),失败");
        }
    }

    @Override
    public void getAllTags(int sequence) {
        if (mContext!=null){
            JPushInterface.getAllTags(mContext,sequence);
        }else {
            Log.e(TAG, "JPushInterface.getAllTags(mContext,sequence),失败");
        }
    }

    @Override
    public void setAlias(int sequence, String alias) {
        if (mContext!=null){
            JPushInterface.setAlias(mContext,sequence,alias);
        }else {
            Log.e(TAG, "JPushInterface.setAlias(mContext,sequence,alias),失败");
        }
    }

    @Override
    public void getAlias(int sequence) {
        if (mContext!=null){
            JPushInterface.getAlias(mContext,sequence);
        }else {
            Log.e(TAG, "JPushInterface.getAlias(mContext,sequence),失败");
        }
    }
}
