package com.csjbot.blackgaga.push;

import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.google.gson.Gson;

/**
 * Created by xiasuhuei321 on 2017/11/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class BMessageHandler {
    public static final int UPDATE_PRODUCT = 0;
    private PushMessage pushMessage;

    private BMessageHandler() {
    }

    private static class BMessgeHandlerHolder {
        private static BMessageHandler INSTANCE = new BMessageHandler();
    }

    public static BMessageHandler getInstance() {
        return BMessgeHandlerHolder.INSTANCE;
    }

    public void handlerMessage(String msg) {
        try {
            pushMessage = new Gson().fromJson(msg, PushMessage.class);
        } catch (Exception e) {
            BlackgagaLogger.debug("推送消息不符合格式");
        }

        if (pushMessage == null) return;
        if (pushMessage.type == UPDATE_PRODUCT) {
            BlackgagaLogger.debug("收到更新通知，开始更新产品");
            ProductProxy proxy = ServerFactory.createProduct();
            proxy.getRobotMenuList(true);
        }
    }

}
