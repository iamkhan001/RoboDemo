package com.csjbot.blackgaga.jpush.diaptch.handler;

import android.text.TextUtils;

import com.csjbot.blackgaga.advertisement.manager.AdvertisementManager;
import com.csjbot.blackgaga.jpush.diaptch.CsjPushDispatch;
import com.csjbot.blackgaga.jpush.diaptch.constants.ConstantsId;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jingwc on 2018/3/2.
 */

public class MessageHandler implements Runnable {

    private String json;

    public MessageHandler(String json) {
        this.json = json;
    }

    @Override
    public void run() {
        try {
            String id = new JSONObject(json).getString("msg_id");
            int sid = new JSONObject(json).getInt("sid");
            String data = new JSONObject(json).getJSONObject("data").toString();
            if (id.contains(ConstantsId.GlobalNoticeId.TAG)) {
                CsjPushDispatch.getInstance().pushGlobalNoticeMessage(id, sid, data);
            } else if (id.contains(ConstantsId.ExpressionId.TAG)) {
//                CsjPushDispatch.getInstance().pushExpressionMessage(id,sid,data);
                if (id.equals(ConstantsId.ExpressionId.EXPRESSION_UPDATE)) {
                    ServerFactory.getExpressionInstantce().updateExpression();
                }
            } else if (id.contains(ConstantsId.UserInformationId.TAG)) {
                if (TextUtils.equals(id, ConstantsId.UserInformationId.NOTICE_FACE_INFO_CHANGED)) {
                    ServerFactory.getFaceInstance().faceInfoChanged(new JSONObject(data).getString("faceId"));
                } else if (TextUtils.equals(id, ConstantsId.UserInformationId.NOTICE_FACE_INFO_DELETE)) {
                    ServerFactory.getFaceInstance().faceDelList(new JSONObject(data).getString("faceId"));
                }
            } else if (TextUtils.equals(id, ConstantsId.AllContentTypesChanged.NOTICE_ALL_CONTENT_TYPES_CHANGED)) {
                ProductProxy proxy = com.csjbot.blackgaga.model.http.factory.ServerFactory.createProduct();
                proxy.getContent(null);
            }else if (TextUtils.equals(id,ConstantsId.AdvertisementChanged.NOTICE_ADVERTISEMENT_CHANGED)){
                AdvertisementManager.getInstance().updateData();
            }else if(id.contains(ConstantsId.Scene.TAG)){
                CsjPushDispatch.getInstance().pushSceneMessage(id,sid,data);
            }

        } catch (JSONException e) {
            CsjlogProxy.getInstance().error("e:" + e.toString());
        }
    }
}
