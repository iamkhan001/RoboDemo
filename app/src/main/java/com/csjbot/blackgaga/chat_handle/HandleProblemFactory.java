package com.csjbot.blackgaga.chat_handle;


import android.os.HandlerThread;
import android.text.TextUtils;

import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.listener.IAnswerCallBack;
import com.csjbot.blackgaga.chat_handle.task.RbTimeout;
import com.csjbot.blackgaga.chat_handle.task.base.RbTask;
import com.csjbot.blackgaga.chat_handle.task.task_factory.RbFactory;
import com.csjbot.cosclient.utils.CosLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 问题处理工厂
 * 负责处理传进来的问题
 * 通过并发多源获取返回数据
 * Created by jingwc on 2017/6/29.
 */

public class HandleProblemFactory {

    private volatile static HandleProblemFactory handleFactory;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 消息处理器
     */
    private HandleAnswerMessage handle;

    /**
     * Handle线程类(用于帮助HandleAnswerMessage在子线程工作)
     * HandlerThread继承自Thread在内部创建了Looper
     */
    private HandlerThread handlerThread;


    /**
     * 私有构造(单例模式)
     */
    private HandleProblemFactory() {
        // 实例化线程池
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        // 实例化handle线程类
        if (handlerThread == null) {
            handlerThread = new HandlerThread("handleMessage");
            handlerThread.start();
        }

        // 实例化消息处理器
        if (handle == null) {
            handle = new HandleAnswerMessage(handlerThread.getLooper());
        }
    }

    /**
     * 获取当前类的实例
     */
    public static HandleProblemFactory getInstance() {
        if (handleFactory == null) {
            synchronized (HandleProblemFactory.class) {
                if (handleFactory == null) {
                    handleFactory = new HandleProblemFactory();
                }
            }
        }
        return handleFactory;
    }

    /**
     *
     * @param text  用户说话内容
     * @param answer 语音识别带回的回答内容
     * @param callBack
     */
    public synchronized void getAnswer(String text,String answer,IAnswerCallBack callBack) {
        String sid = String.valueOf(System.currentTimeMillis());
        if (TextUtils.isEmpty(text)) {
            return;
        }
        CosLogger.debug("用户讲话内容-->" + text + "<---语音识别带回的回答内容-->" + answer);

        // 发送初始化消息
        sendInitMessage(sid, callBack);

        // 动态获取任务和执行任务
        for (int type : Constants.Scheme.schemes) {
            // 通过优先级标识从工厂里获取一个实例对象
            RbTask rbTask = RbFactory.createRbTask(type);
            // 初始化该对象
            rbTask.init(sid, text, answer, handle);
            // 执行该任务
            executorService.execute(rbTask);
        }

        // 发送超时消息
        RbTimeout timeout = new RbTimeout();
        timeout.init(sid, text, answer, handle);
        executorService.execute(timeout);
    }


    /**
     * 给消息处理器发送init信息
     */
    private void sendInitMessage(String sid, IAnswerCallBack callBack) {
        if (handle != null) {
            handle.obtainMessage(Constants.Type.INIT_MSG, new Object[]{sid, callBack}).sendToTarget();
        }
    }


}
