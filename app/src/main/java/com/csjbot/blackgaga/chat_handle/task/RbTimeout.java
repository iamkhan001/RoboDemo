package com.csjbot.blackgaga.chat_handle.task;

import com.csjbot.blackgaga.chat_handle.task.base.RbTask;
import com.csjbot.cosclient.utils.CosLogger;

/**
 * 超时消息任务类
 * Created by jingwc on 2017/6/29.
 */

public class RbTimeout extends RbTask {

    @Override
    protected void execute() {
        try {
            Thread.sleep(2600);
        } catch (InterruptedException e) {
            CosLogger.debug("RbTimeout:e:"+e.toString());
        }
        sendTimeoutMessage();
    }

    @Override
    protected void disable() {
    }
}
