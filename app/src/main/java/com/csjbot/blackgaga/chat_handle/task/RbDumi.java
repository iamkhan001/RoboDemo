//package com.csjbot.blackgaga.chat_handle.task;
//
//
//import android.text.TextUtils;
//
//import com.baidu.duersdk.DuerSDKFactory;
//import com.baidu.duersdk.datas.DuerMessage;
//import com.baidu.duersdk.message.IReceiveMessageListener;
//import com.baidu.duersdk.message.ISendMessageFinishListener;
//import com.baidu.duersdk.message.SendMessageData;
//import com.csjbot.blackgaga.chat_handle.constans.Constants;
//import com.csjbot.blackgaga.chat_handle.task.base.RbTask;
//import com.csjbot.cosclient.utils.CosLogger;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * 度秘任务类
// * Created by jingwc on 2017/6/29.
// */
//
//public class RbDumi extends RbTask {
//
//    @Override
//    protected void execute() {
////        sendMessage("度秘回答", Constants.Scheme.GOSSIP);
//        getDuerAnwer();
//    }
//
//    @Override
//    protected void disable() {
//        sendEmptyMessage(Constants.Scheme.GOSSIP);
//    }
//
//    private void getDuerAnwer() {
//        DuerSDKFactory.getDuerSDK().getMessageEngine().setReceiveMessageListener(messageListener);
//        SendMessageData sendMessageData = new SendMessageData();
//        //需要查下的 query
//        sendMessageData.setQuery(mText);
//
////        //设置经纬度信息,坐标系名称
////        sendMessageData.setLocalSystemName("wgs84");
////        //经度
////        sendMessageData.setLocalLongitude(LocationUtil.getLongitude().floatValue());
////        //纬度
////        sendMessageData.setLocalLatitude(LocationUtil.getLatitude().floatValue());
//
//        DuerSDKFactory.getDuerSDK().getMessageEngine().sendMessage(sendMessageData, new ISendMessageFinishListener() {
//            @Override
//            public void messageSendStatus(MSG_SENDSTATUS msg_sendstatus, DuerMessage duerMessage, JSONObject jsonObject) {
//                try {
//                    if (msg_sendstatus == ISendMessageFinishListener.MSG_SENDSTATUS.MSG_SENDFAILURE) {
//                    }
//                } catch (Exception e) {
//                }
//            }
//        });
//    }
//
//    final IReceiveMessageListener messageListener = new IReceiveMessageListener() {
//        @Override
//        public void messageReceive(String s) {
//            if (!TextUtils.isEmpty(s)) {
//                CosLogger.debug("度秘回答内容:"+s);
//                try {
//                    String anwer = new JSONObject(s).getJSONObject("result").getJSONObject("speech").getString("content");
//                    sendMessage(anwer, Constants.Scheme.GOSSIP);
//                } catch (JSONException e) {
//                    sendEmptyMessage(Constants.Scheme.GOSSIP);
//                }
//            }
//        }
//    };
//}
