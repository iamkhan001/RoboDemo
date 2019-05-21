package com.csjbot.blackgaga.p2pcamera;

import android.content.Intent;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.Locale;

/**
 * Created by jingwc on 2018/3/8.
 */

public class CustomerService {

    public static final int ROBOT = 0;

    public static final int CUSTOMER = 0;

    /**
     * 开启客服服务
     *
     * @param sn
     */
    public void openCustomerService(String sn) {
        String json = "{\"msg_id\":\"REQ_CALL_HUMAN_SERVICE\",\"sid\":%d,\"sn\":\"%s\"}";
        json = String.format(Locale.getDefault(), json, System.currentTimeMillis(), sn);
        CsjlogProxy.getInstance().debug("openCustomerService  === " + json);
        sendBroadcast(json);
    }

    /**
     * 上传(机器人|顾客)说话文本到人工客服后台
     *
     * @param text
     * @param sn
     */
    public void uploadChatMsg(String text, String sn, int type) {
        String chatJson = "";
        if (type == ROBOT) {
            chatJson = "{\"msg_id\":\"SEND_CHAT_MSG\",\"msg\":\"" + text + "\",\"type\":2,\"sn\":\"" + sn + "\"}";
        } else if (type == CUSTOMER) {
            chatJson = "{\"msg_id\":\"SEND_CHAT_MSG\",\"msg\":\"" + text + "\",\"type\":1,\"sn\":\"" + sn + "\"}";
        }
        sendBroadcast(chatJson);
    }


    private void sendBroadcast(String json) {
        Intent intent = new Intent();
        intent.setAction(P2PCameraComm.SEND_MSG_TO_SERVER);
        intent.putExtra("json", json);
        BaseApplication.getAppContext().sendBroadcast(intent);
    }
}
