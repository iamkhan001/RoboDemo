package com.csjbot.blackgaga.p2pcamera.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.csjbot.blackgaga.p2pcamera.P2PCameraComm;
import com.csjbot.blackgaga.p2pcamera.listener.GetMessageListener;

/**
 * @author Ben
 * @date 2018/1/9
 */

public class LiveBroadcast extends BroadcastReceiver {

    private String mTitle="";
    private String mData = "";
    private GetMessageListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(P2PCameraComm.ACTION_VIDEO_STATUS)) {
//            mTitle="VideoStatus";
//            mData = intent.getStringExtra("VideoStatus");
        } else if (action.equals(P2PCameraComm.ACTION_NOTIFY)) {
            mTitle="Notify";
            mData = intent.getStringExtra("Notify");
        } else if (action.equals(P2PCameraComm.ACTION_RENDER_JOIN)) {
            mTitle="RenderJoin";
            mData = intent.getStringExtra("RenderJoin");
        } else if (action.equals(P2PCameraComm.ACTION_RENDER_LEAVE)) {
            mTitle="RenderLeave";
            mData = intent.getStringExtra("RenderLeave");
        } else if (action.equals(P2PCameraComm.ACTION_MESSAGE)) {
            mTitle="Message";
            mData = intent.getStringExtra("Message");
        } else if (action.equals(P2PCameraComm.ACTION_LOGIN)) {

        } else if (action.equals(P2PCameraComm.ACTION_LOGIN0)) {
            mTitle="Login1";
            mData = intent.getStringExtra("Login0");
        } else if (action.equals(P2PCameraComm.ACTION_LOGIN1)) {
            mTitle="Login0";
            mData = intent.getStringExtra("Login1");
        } else if (action.equals(P2PCameraComm.ACTION_LOGOUT)) {
            mTitle="Logout";
            mData = intent.getStringExtra("Logout");
        } else if (action.equals(P2PCameraComm.ACTION_CONNECT)) {
            mTitle="Connect";
            mData = intent.getStringExtra("Connect");
        } else if (action.equals(P2PCameraComm.ACTION_DISCONNECT)) {
            mTitle="Disconnect";
            mData = intent.getStringExtra("Disconnect");
        } else if (action.equals(P2PCameraComm.ACTION_OFFLINE)) {
            mTitle="Offline";
            mData = intent.getStringExtra("Offline");
        } else if (action.equals(P2PCameraComm.ACTION_LAN_SCAN_RESULT)) {
            mTitle="LanScanResult";
            mData = intent.getStringExtra("LanScanResult");
        } else if (action.equals(P2PCameraComm.ACTION_FORWARD_ALLOC_REPLY)) {
            mTitle="ForwardAllocReply";
            mData = intent.getStringExtra("ForwardAllocReply");
        } else if (action.equals(P2PCameraComm.ACTION_FORWARD_FREE_REPLY)) {
            mTitle="ForwardFreeReply";
            mData = intent.getStringExtra("ForwardFreeReply");
        } else if (action.equals(P2PCameraComm.ACTION_VIDEO_CAMERA)) {
            mTitle="VideoCamera";
            mData = intent.getStringExtra("VideoCamera");
        } else if (action.equals(P2PCameraComm.ACTION_SVR_NOTIFY)) {
            mTitle="SvrNotify";
            mData = intent.getStringExtra("SvrNotify");
        }

        if (listener != null) {
            if (!mTitle.equals(null)&&!mData.equals(null)) {
                listener.getMsg(mTitle,mData);
            }
        }
    }

    public void setListener(GetMessageListener listener) {
        this.listener = listener;
    }

}
