package com.csjbot.blackgaga.chat_handle.task;


import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.task.base.RbTask;

/**
 * 智齿任务类
 * Created by jingwc on 2017/6/29.
 */

public class RbZhichi extends RbTask {

    @Override
    protected void execute() {
        sendMessage("智齿回答", Constants.Scheme.PROFESSIONAL);
    }

    @Override
    protected void disable() {
        sendEmptyMessage(Constants.Scheme.PROFESSIONAL);
    }
}
