package com.csjbot.coshandler.service;


import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.handle_msg.MessageHandleFactory;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * 处理消息service
 * Created by jingwc on 2017/8/11.
 */

public class HandlerMsgService extends BaseReceiveService {

    @Override
    protected void handleMessage(String json) {
        CsjlogProxy.getInstance().debug("class:HandlerMsgService method:handleMessage json:" + json);
        MessageHandleFactory.getInstance().startWork(json);
    }

    @Override
    protected void connectStatus(int type) {
        Robot.getInstance().pushInit(type);
        CsjlogProxy.getInstance().debug("class:HandlerMsgService method:connectStatus type:" + type);
    }

    @Override
    protected void sendFailed() {
        CsjlogProxy.getInstance().debug("class:HandlerMsgService method:sendFailed");
    }
}
