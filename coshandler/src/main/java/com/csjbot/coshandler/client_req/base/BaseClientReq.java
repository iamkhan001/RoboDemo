package com.csjbot.coshandler.client_req.base;

import android.text.TextUtils;

import com.csjbot.cosclient.CosClientAgent;
import com.csjbot.cosclient.entity.CommonPacket;
import com.csjbot.cosclient.entity.MessagePacket;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.List;


/**
 * 请求基类
 * Created by jingwc on 2017/8/14.
 */

public abstract class BaseClientReq {
    /**
     * 发送请求
     *
     * @param json
     */
    protected void sendReq(String json) {
        if (TextUtils.isEmpty(json)) return;
        CsjlogProxy.getInstance().info(json);
        try {
            MessagePacket packet = new CommonPacket(json.getBytes());
            CosClientAgent.getRosClientAgent().sendMessage(packet);
        } catch (Exception e) {
            CosLogger.error("BaseClientReq:sendReq:e:" + e.toString());
        }
    }

    /**
     * 转换只包含msgid字段的json
     *
     * @param msgId
     * @return
     */
    protected String getJson(String msgId) {
        String json = "{\"msg_id\":\"" + msgId + "\"}";
        return json;
    }

    /**
     * 转换包含msgid和一个字段的json
     *
     * @param msgId
     * @param field
     * @param value
     * @return
     */
    protected String getJson(String msgId, String field, String value) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"" + field + "\":\"" + value + "\"}";
        return json;
    }

    protected String getJsonFromJsonContent(String msgId, String field, String jsonContent) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"" + field + "\":" + jsonContent + "}";
        return json;
    }

    /**
     * 转换包含msgid和一个字段的json
     *
     * @param msgId
     * @param field
     * @param value
     * @return
     */
    protected String getJson(String msgId, String field, int value) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"" + field + "\":" + value + "}";
        return json;
    }

    /**
     * 转换包含msgid和一个字段的json
     *
     * @param msgId
     * @param field
     * @param value
     * @return
     */
    protected String getJson(String msgId, String field, float value) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"" + field + "\":" + value + "}";
        return json;
    }

    /**
     * 转换包含msgid和一个json的json
     *
     * @param msgId
     * @param field
     * @param json
     * @return
     */
    protected String getChassisJson(String msgId, String field, String json) {
        CosLogger.debug("{\"msg_id\":\"" + msgId + "\",\"" + field + "\":" + json + "}");
        return "{\"msg_id\":\"" + msgId + "\",\"" + field + "\":" + json + "}";
    }

    /**
     * 转换肢体动作的json
     *
     * @param msgId
     * @param bodyPart
     * @param action
     * @return
     */
    protected String getBodyActionJson(String msgId, int bodyPart, int action) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"body_part\":" + bodyPart + ",\"action\":" + action + "}";
        return json;
    }

    protected String getExpressionJson(String msgId, int expression, int once, int time) {
        String json = "{\"msg_id\":\"" + msgId + "\",\"expression\":" + expression + ",\"once\":" + once + ",\"time\":" + time + "}";
        return json;
    }


    protected String getHotWordJson(String msgId, List<String> hotwords) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hotwords.size(); i++) {
            sb.append("\"").append(hotwords.get(i)).append("\"");
            if (i != hotwords.size() - 1) {
                sb.append(",");
            }
        }
        String json = "{\"msg_id\":\"" + msgId + "\",\"words\":[" + sb.toString() + "]}";
        CosLogger.debug("setHotWordJson " + json);
        return json;
    }

}
