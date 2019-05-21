package com.csjbot.blackgaga.chat_handle.task;


import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.task.base.RbTask;

/**
 * 阿里云任务类
 * Created by jingwc on 2017/6/29.
 */

public class RbCloud extends RbTask {

    @Override
    protected void execute() {
        sendMessage("阿里云回答", Constants.Scheme.CLOUD);
    }

    @Override
    protected void disable() {
        sendEmptyMessage(Constants.Scheme.CLOUD);
    }
}
