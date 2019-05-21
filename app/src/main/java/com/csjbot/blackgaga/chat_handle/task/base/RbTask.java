package com.csjbot.blackgaga.chat_handle.task.base;

import com.csjbot.blackgaga.chat_handle.HandleAnswerMessage;
import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.cosclient.utils.CosLogger;

/**
 * 任务父类
 * Created by jingwc on 2017/6/30.
 */

public abstract class RbTask implements Runnable {

    /* 此次问答的问题sid */
    protected String mSid;

    /* 问题文本 */
    protected String mText;

    /* 语音识别自带语义结果 */
    protected String mAnswer;

    /* 消息处理器实例 */
    protected HandleAnswerMessage mHandle;

    /* 当前任务是否禁用 */
    protected boolean mDisable;

    public RbTask() {

    }

    @Override
    public void run() {
        if(mDisable){
            disable();
        }else{
            execute();
        }
    }

    public void setDisable(boolean disable){
        this.mDisable = disable;
    }

    protected abstract void execute();

    protected abstract void disable();

    /**
     * 默认发送普通消息
     *
     * @param retJson 问题返回的回答结果
     * @param type    标识是否发出的结果(type对应处理消息的方案)
     */
    protected void sendMessage(String retJson, int type) {
        Object[] objects = new Object[]{mSid, retJson};
        mHandle.obtainMessage(Constants.Type.NORML_MSG, type, 0, objects).sendToTarget();
    }

    /**
     * 发送空消息
     * @param type 标识是否发出的结果(type对应处理消息的方案)
     */
    protected void sendEmptyMessage(int type) {
        Object[] objects = new Object[]{mSid, ""};
        mHandle.obtainMessage(Constants.Type.NORML_MSG, type, 0, objects).sendToTarget();
    }

    /**
     * 发送超时信息
     */
    protected void sendTimeoutMessage() {
        mHandle.obtainMessage(Constants.Type.TIMEOUT_MSG, mSid).sendToTarget();
    }


    /**
     * 初始化
     *
     * @param sid            此次问答的问题sid
     * @param mAnswer       语音识别自带返回语义结果
     * @param handle         消息处理器实例
     */
    public void init(String sid, String mText, String mAnswer, HandleAnswerMessage handle) {
        this.mSid = sid;
        this.mHandle = handle;
        this.mText = mText;
        this.mAnswer = mAnswer;
        CosLogger.debug("此次问答的问题sid---->" + sid + "<---问题文本-->" + mText);
    }

}
