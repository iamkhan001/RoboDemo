package com.csjbot.blackgaga.jpush.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.csjbot.blackgaga.jpush.constant.JPushConstant;
import com.csjbot.blackgaga.jpush.event.JPushEvent;
import com.csjbot.blackgaga.jpush.event.JPushMessageEvent;
import com.csjbot.blackgaga.jpush.event.JPushTagsAliasEvent;
import com.csjbot.blackgaga.jpush.factory.CsjMessageFactory;
import com.csjbot.blackgaga.jpush.listener.JPushEventListener;
import com.csjbot.blackgaga.jpush.listener.JPushListener;
import com.csjbot.blackgaga.jpush.listener.JPushMessageListener;
import com.csjbot.blackgaga.jpush.listener.JPushTagsAliasListener;
import com.csjbot.blackgaga.jpush.receiver.JPushGetMessageReceiver;
import com.csjbot.blackgaga.jpush.receiver.JPushGetTagAliasReceiver;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * @author Ben
 * @date 2018/2/9
 */

public class CsjJPushManager implements JPushMessageListener, JPushTagsAliasListener {

    private static final String TAG = "CsjJPushManager";
    private Context mContext;

    private JPushListener mListener;
    private JPushGetMessageReceiver mReceiver = null;
    private JPushGetTagAliasReceiver mGetMessageReceiver = null;
    private JPushEventListener mListener_Event;

    private CsjJPushManager() {
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final CsjJPushManager mManager = new CsjJPushManager();
    }

    /**
     * 初始化CsjMessageManager
     *
     * @return
     */
    public static CsjJPushManager getInstance() {
        return Holder.mManager;
    }

    /**
     * 先初始化Manager
     *
     * @param context 应用的 ApplicationContext
     */
    public void initManager(Context context) {
        this.mContext = context;
        if (mListener == null) {
            mListener = CsjMessageFactory.newInstance(context);
        }
        initReceiver();
    }

    /**
     * 初始化JPush，建议在Application中进行初始化
     * 可以在需要调用的地方初始化
     */
    public void init() {
        mListener.init();
    }

    /**
     * 初始化广播
     */
    private void initReceiver() {
        if (mReceiver == null) {
            Log.e(TAG, "initReceiver: 初始化广播");
            mReceiver = new JPushGetMessageReceiver();
            mReceiver.setListener(this);
            IntentFilter mFilter = new IntentFilter();
            mFilter.addAction(JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION);
            mFilter.addAction(JPushInterface.ACTION_NOTIFICATION_RECEIVED);
            mFilter.addAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
            mFilter.addAction(JPushInterface.ACTION_CONNECTION_CHANGE);
            mFilter.addAction(JPushInterface.ACTION_MESSAGE_RECEIVED);
            mFilter.addAction(JPushInterface.ACTION_REGISTRATION_ID);
            mFilter.addCategory(JPushConstant.CATEGORY);
            mContext.registerReceiver(mReceiver, mFilter);
        }

        if (mGetMessageReceiver == null) {
            mGetMessageReceiver = new JPushGetTagAliasReceiver();
            mGetMessageReceiver.setListener(this);
            IntentFilter mGetMessageFilter = new IntentFilter();
            mGetMessageFilter.addAction("cn.jpush.android.intent.RECEIVE_MESSAGE");
            mGetMessageFilter.addCategory(JPushConstant.CATEGORY);
            mContext.registerReceiver(mGetMessageReceiver, mGetMessageFilter);
        }
    }

    /**
     * 注销广播
     */
    public void unregisterReceiver() {
        if (mReceiver != null && mContext != null) {
            mContext.unregisterReceiver(mReceiver);
            Log.e(TAG, "unregisterReceiver: 注销广播成功");
        }
        if (mGetMessageReceiver != null && mContext != null) {
            mContext.unregisterReceiver(mGetMessageReceiver);
        }
    }

    /**
     * 停止接收推送服务
     */
    public void stopPush() {
        mListener.stopPush();
    }

    /**
     * 恢复接收推送服务
     * 重新恢复接收推送服务，调用init()方法无效，需调用resumePush()
     */
    public void resumePush() {
        mListener.resumePush();
    }

    /**
     * 推送服务的状态
     *
     * @return 是否停止，是，返回true，否，返回false
     */
    public boolean isPushStopped() {
        return mListener.isPushStopped();
    }

    /**
     * 获取getRegistrationId
     *
     * @return
     */
    public String getRegistrationId() {
        return mListener.getRegistrationId();
    }

    /**
     * 设置Tags,如果存在Tag,会把以前的Tag覆盖掉
     *
     * @param sequence
     * @param set
     */
    public void setTags(int sequence, Set<String> set) {
        mListener.setTags(sequence, set);
    }

    /**
     * 获取所有Tags
     *
     * @param sequence
     */
    public void getAllTags(int sequence) {
        mListener.getAllTags(sequence);
    }

    /**
     * 设置Alias，如果存在Alias,会把以前的Alias覆盖掉
     *
     * @param sequence
     * @param alias
     */
    public void setAlias(int sequence, String alias) {
        mListener.setAlias(sequence, alias);
    }

    /**
     * 获取Alias
     *
     * @param sequence
     */
    public void getAlias(int sequence) {
        mListener.getAlias(sequence);
    }

    public void setJPushEventListener(JPushEventListener listener) {
        this.mListener_Event = listener;
    }

    @Override
    public void onTagOperatorResult(JPushMessage message) {
        if (mListener_Event != null && message != null) {
            mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_TAGS_ALIAS,
                    new JPushTagsAliasEvent(JPushConstant.onTagOperatorResult, message)));
        }
    }

    @Override
    public void onCheckTagOperatorResult(JPushMessage message) {
        if (mListener_Event != null && message != null) {
            mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_TAGS_ALIAS,
                    new JPushTagsAliasEvent(JPushConstant.onCheckTagOperatorResult, message)));
        }
    }

    @Override
    public void onAliasOperatorResult(JPushMessage message) {
        if (mListener_Event != null && message != null) {
            mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_TAGS_ALIAS,
                    new JPushTagsAliasEvent(JPushConstant.onAliasOperatorResult, message)));
        }
    }

    @Override
    public void onMobileNumberOperatorResult(JPushMessage message) {
        if (mListener_Event != null && message != null) {
            mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_TAGS_ALIAS,
                    new JPushTagsAliasEvent(JPushConstant.onMobileNumberOperatorResult, message)));
        }
    }

    /**
     * 获取JPush 返回的数据
     *
     * @param action
     * @param bundle
     */
    @Override
    public void getJPushCallBack(String action, Bundle bundle) {
        if (mListener_Event != null && !TextUtils.isEmpty(action) && bundle != null) {
            if (TextUtils.equals(action, JPushInterface.ACTION_REGISTRATION_ID)) {
                mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_MESSAGE,
                        new JPushMessageEvent(JPushConstant.ACTION_REGISTRATION_ID,
                                bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID))));
            } else if (TextUtils.equals(action, JPushInterface.ACTION_MESSAGE_RECEIVED)) {
                mListener_Event.onEvent(new JPushEvent(JPushConstant.JPUSH_MESSAGE,
                        new JPushMessageEvent(JPushConstant.ACTION_MESSAGE_RECEIVED,
                                bundle.getString(JPushInterface.EXTRA_MESSAGE))));
            }
        }
    }
}
