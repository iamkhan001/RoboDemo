package com.csjbot.blackgaga.chat_handle.task;


import com.csjbot.blackgaga.chat_handle.constans.Constants;
import com.csjbot.blackgaga.chat_handle.task.base.RbTask;

/**
 * 自定义回答任务类
 * Created by jingwc on 2017/6/29.
 */

public class RbCustom extends RbTask {

    @Override
    protected void execute() {
        sendMessage("自定义回答", Constants.Scheme.CUSTOM);
    }

    @Override
    protected void disable() {
        sendEmptyMessage(Constants.Scheme.CUSTOM);
    }
}
