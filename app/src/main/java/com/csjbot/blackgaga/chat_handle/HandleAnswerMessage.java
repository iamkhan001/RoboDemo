package com.csjbot.blackgaga.chat_handle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.entity.AnswerMessage;
import com.csjbot.blackgaga.chat_handle.listener.IAnswerCallBack;
import com.csjbot.cosclient.utils.CosLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 消息处理器
 * 接受问题返回的所有答案
 * 按照规则进行选择回复
 * Created by jingwc on 2017/6/29.
 */

public class HandleAnswerMessage extends Handler {

    public HandleAnswerMessage(Looper looper) {
        super(looper);
    }

    /*  消息队列（用于存储所有回答数据） */
    private Map<Integer, AnswerMessage> messages = new HashMap();

    /* 消息处理模式(默认普通模式) */
    private int mode;

    /* 当前问题是否处理完毕 */
    private boolean isComplete;

    /*  问题的sid(以保证问题和回答一致性) */
    private String sid = "";

    /*  问题答案回调 */
    private IAnswerCallBack callBack;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        switch (msg.what) {
            case Constants.Type.INIT_MSG:// 初始化消息
                CosLogger.debug("--INIT:MSG--");
                handleInit(msg);
                break;
            case Constants.Type.NORML_MSG:// 普通消息(正常问题返回的结果消息)
                CosLogger.debug("--NORML:MSG--");
                handleNorml(msg);
                break;
            case Constants.Type.TIMEOUT_MSG:// 超时消息
                CosLogger.debug("--TIMEOUT:MSG--");
                handleTimeout(msg);
                break;
        }

    }

    /**
     * 处理初始化消息
     *
     * @param msg
     */
    private void handleInit(Message msg) {
        Object[] objects = (Object[]) msg.obj;
        if (objects != null && objects.length > 0) {
            // 获取当前问答的sid
            if (!TextUtils.isEmpty(objects[0].toString())) {
                sid = objects[0].toString();
            }
            // 获取监听事件
            callBack = (IAnswerCallBack) objects[1];
        }

        // 默认为普通模式(优先级模式)
        mode = Constants.Mode.NORML_MODE;
        // 是否完成为false
        isComplete = false;
        // 清楚队列中所有的消息
        if (messages != null && messages.size() > 0) {
            messages.clear();
        }

    }

    /**
     * 处理超时消息
     *
     * @param msg
     */
    private void handleTimeout(Message msg) {
        // 获取当前问答的sid
        if (!TextUtils.isEmpty(msg.obj.toString())) {
            // 判断发出的超时信息sid正否和此次问题的sid一致
            // 不一致则时上一次问题发出的超时消息,不予处理
            if (!msg.obj.toString().equals(sid)) {
                return;
            } else {
                // 如果当前消息已经处理完毕,则不处理超时消息
                if (isComplete) {
                    return;
                }
            }
        }
        // 用来标识超时模式下的问题是否处理完毕
        boolean handleComplete = false;
        // 遍历消息队列
        Iterator iterator = messages.entrySet().iterator();
        CosLogger.debug("TIMEOUT:messages:size" + messages.size());
        while (iterator.hasNext()) {
            Map.Entry<Integer, AnswerMessage> entry = (Map.Entry<Integer, AnswerMessage>) iterator.next();
            // 如果消息队列中存在数据则处理消息
            CosLogger.debug("TIMEOUT:messages:value" + entry.getValue().getAnswerText());
            if (entry.getValue() != null && !TextUtils.isEmpty(entry.getValue().getAnswerText())) {
                // 处理消息
                handlerMsg(entry.getValue());
                break;
            }
        }
        // 如果超时模式情况下的方案也没有完成问题的处理完毕,则切换自由模式
        if (!handleComplete) {// 切换自由模式
            mode = Constants.Mode.FREE_MODE;
        }
    }
    /**
     * 处理完成
     */
    private void handlerComplete() {
        isComplete = true;
    }

    /**
     * 处理消息,通过回调返回
     * @param message
     */
    private void handlerMsg(AnswerMessage message){
        if(callBack != null){
            callBack.reponse(message);
            handlerComplete();
        }
    }

    /**
     * 下一个处理
     * @param message
     */
    private void nextHandler(AnswerMessage message){
        AnswerMessage aMsg = messages.get(Constants.Priority.getNextPriority(message.getPriority()));
        if (aMsg != null && !TextUtils.isEmpty(aMsg.getAnswerText())) {
            handlerMsg(message);
            CosLogger.debug("NORML_MODE:next handle");
        }
    }

    /**
     * 消息入队
     * @param message
     */
    private void enqueueMessage(AnswerMessage message){
        // 当前方案完成搜索
        message.setSearchComplete(true);
        // 添加到队列
        messages.put(message.getPriority(), message);
    }

    /**
     * 处理普通消息
     *
     * @param msg
     */
    private void handleNorml(Message msg) {

        Object[] objects = (Object[]) msg.obj;
        AnswerMessage message = new AnswerMessage();
        if (objects != null && objects.length > 0) {
            message.setSid(objects[0].toString());
            message.setAnswerText(objects[1].toString());
        }
        message.setTag(msg.arg1);
        message.setPriority(msg.arg1);

        // 问题与答案不在一次会话内,不予处理
        if (!message.getSid().equals(sid)) {
            return;
        }
        // 问题已经处理完毕,不必继续处理
        if (isComplete) {
            return;
        }
        CosLogger.debug("NORML_MODE:priority::" + message.getPriority() + ";answerText:" + message.getAnswerText());
        switch (mode) {
            case Constants.Mode.NORML_MODE: // 普通模式（优先级模式）
                if (message.getPriority() == Constants.Priority.ONE) {// 如果优先级为最高,则直接处理
                    if (!TextUtils.isEmpty(message.getAnswerText())) {
                        handlerMsg(message);
                        CosLogger.debug("NORML_MODE:current handle");
                    } else {
                        // 此方案无处理结果,获取下一级方案在队列中的数据
                        nextHandler(message);
                    }
                    // 入队
                    enqueueMessage(message);
                } else {
                    // 从队列中获取上级列表的方案判断是否全部处理完毕
                    // messages队列的key对应tags的值，tags的值也代表当前方案的优先级
                    boolean complete = true;
                    for (int key = Constants.Priority.ONE; key > message.getPriority(); key--) {
                        if (!(messages.get(key) != null && messages.get(key).getSearchComplete())) {
                            complete = false;
                            break;
                        }
                    }
                    if (complete) {
                        if (!TextUtils.isEmpty(message.getAnswerText())) {
                            // 处理问题
                            handlerMsg(message);
                            CosLogger.debug("NORML_MODE:current handle");
                        } else {
                            // 判断此次返回的消息方案是否为最后一个方案,如果是最后一个方案就没必要去获取下一级了
                            if (!Constants.Priority.isLastPriority(message.getPriority())) {
                                nextHandler(message);
                            }
                        }
                    }
                    // 入队
                    enqueueMessage(message);
                }
                break;

            case Constants.Mode.FREE_MODE: // 自由模式
                if (!TextUtils.isEmpty(message.getAnswerText())) {
                    // 处理问题
                    CosLogger.debug("FREE_MODE:priority:" + message.getPriority());
                    CosLogger.debug("FREE_MODE:answerText:" + message.getAnswerText());
                    handlerMsg(message);
                }
                break;
            default:
                break;
        }
    }
}
