package com.csjbot.blackgaga.p2pcamera.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.csjbot.blackgaga.p2pcamera.P2PCameraComm;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.peergine.android.livemulti.pgLibLiveMultiCapture;
import com.peergine.plugin.lib.pgLibJNINode;

/**
 * @author Ben
 * @date 2018/1/9
 */

public class LiveService extends Service {

    private LinearLayout mLayout;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    private SurfaceView mView = null;
    private pgLibLiveMultiCapture mLive = new pgLibLiveMultiCapture();

    HandlerThread handlerThread = null;

    Handler handler = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        handlerThread = new HandlerThread("handleMessage");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);

                String[] datas = (String[]) msg.obj;

                if (datas == null || datas.length == 0) {
                    return;
                }

                String sAct = datas[0];
                String sData = datas[1];

                Intent intent = new Intent();
                if (sAct.equals("VideoStatus")) {
//                intent.setAction(P2PCameraComm.ACTION_VIDEO_STATUS);
//                intent.putExtra("VideoStatus",sData);
                } else if (sAct.equals("Notify")) {
                    intent.setAction(P2PCameraComm.ACTION_NOTIFY);
                    intent.putExtra("Notify", sData);
                } else if (sAct.equals("RenderJoin")) {
                    intent.setAction(P2PCameraComm.ACTION_RENDER_JOIN);
                    intent.putExtra("RenderJoin", sData);
                    int i = 0;
                    while (true) {
                        String sRenID1 = mLive.RenderEnum(i);
                        if (sRenID1.equals("")) {
                            break;
                        }
                        i++;
                    }
                } else if (sAct.equals("RenderLeave")) {
                    intent.setAction(P2PCameraComm.ACTION_RENDER_LEAVE);
                    intent.putExtra("RenderLeave", sData);
                    int i = 0;
                    while (true) {
                        String sRenID1 = mLive.RenderEnum(i);
                        if (sRenID1.equals("")) {
                            break;
                        }
                    }
                } else if (sAct.equals("Message")) {
                    intent.setAction(P2PCameraComm.ACTION_MESSAGE);
                    intent.putExtra("Message", sData);
                } else if (sAct.equals("Login")) {

                    if (sData.equals("0")) {
                        intent.setAction(P2PCameraComm.ACTION_LOGIN0);
                        intent.putExtra("Login0", "登陆成功");
                    } else {
                        intent.setAction(P2PCameraComm.ACTION_LOGIN1);
                        intent.putExtra("Login1", "登陆失败");
                    }
                } else if (sAct.equals("Logout")) {
                    intent.setAction(P2PCameraComm.ACTION_LOGOUT);
                    intent.putExtra("Logout", "退出登录");
                } else if (sAct.equals("Connect")) {
                    intent.setAction(P2PCameraComm.ACTION_CONNECT);
                    intent.putExtra("Connect", sData);
                } else if (sAct.equals("Disconnect")) {
                    intent.setAction(P2PCameraComm.ACTION_DISCONNECT);
                    intent.putExtra("Disconnect", sData);
                } else if (sAct.equals("Offline")) {
                    intent.setAction(P2PCameraComm.ACTION_OFFLINE);
                    intent.putExtra("Offline", sData);
                } else if (sAct.equals("LanScanResult")) {
                    intent.setAction(P2PCameraComm.ACTION_LAN_SCAN_RESULT);
                    intent.putExtra("LanScanResult", sData);
                } else if (sAct.equals("ForwardAllocReply")) {
                    intent.setAction(P2PCameraComm.ACTION_FORWARD_ALLOC_REPLY);
                    intent.putExtra("ForwardAllocReply", sData);
                } else if (sAct.equals("ForwardFreeReply")) {
                    intent.setAction(P2PCameraComm.ACTION_FORWARD_FREE_REPLY);
                    intent.putExtra("ForwardFreeReply", sData);
                } else if (sAct.equals("VideoCamera")) {
                    intent.setAction(P2PCameraComm.ACTION_VIDEO_CAMERA);
                    intent.putExtra("VideoCamera", sData);
                } else if (sAct.equals("FilePutRequest")) {

                } else if (sAct.equals("FileGetRequest")) {

                } else if (sAct.equals("FileAccept")) {

                } else if (sAct.equals("FileReject")) {

                } else if (sAct.equals("FileAbort")) {

                } else if (sAct.equals("FileFinish")) {

                } else if (sAct.equals("FileProgress")) {

                } else if (sAct.equals("SvrNotify")) {
                    intent.setAction(P2PCameraComm.ACTION_SVR_NOTIFY);
                    intent.putExtra("SvrNotify", sData);
                }
                LiveService.this.sendBroadcast(intent);
            }
        };

        createFloatWindow();

        IntentFilter intentFilter = new IntentFilter(P2PCameraComm.SEND_MSG_TO_SERVER);
        registerReceiver(sendMessage, intentFilter);
    }

    private void createFloatWindow() {
        if (!CheckPlugin()) {
            return;
        }
        mLive.SetEventListener(m_OnEvent);


        if (mView != null) {
            return;
        }


        if (!P2PCameraComm.ConnectionInfo.ENABLED) {
            CsjlogProxy.getInstance().warn("not allowed use p2p");
            return;
        }

        String user = P2PCameraComm.ConnectionInfo.USER;
        String passwd = TextUtils.isEmpty(P2PCameraComm.ConnectionInfo.PASSWD) ?
                P2PCameraComm.ConnectionInfo.DEFAULT_PASSWD : P2PCameraComm.ConnectionInfo.PASSWD;
        String server = TextUtils.isEmpty(P2PCameraComm.ConnectionInfo.SERVER_ADDRESS) ?
                P2PCameraComm.ConnectionInfo.DEFAULT_SERVER_ADDRESS : P2PCameraComm.ConnectionInfo.SERVER_ADDRESS;


        int err = mLive.Initialize(user, passwd, server, "", 2, "", getApplication());

        CsjlogProxy.getInstance().info("p2p USER:" + user);
        CsjlogProxy.getInstance().info("p2p SERVER_ADDRESS:" + server);

        if (err != 0) {
            Log.e("pgLiveMultiCapture", "LiveStart: Live.Initialize failed! iErr=" + err);
            return;
        }
        mView = mLive.CameraViewGet();

        mLayout = new LinearLayout(getApplication());
        mLayout.addView(mView);

        mView.setVisibility(View.GONE);

        String sVideoParam = "(Code){3}(Mode){3}(Rate){66}"
                + "(Portrait){0}(BitRate){500}(MaxStream){3}";
        mLive.VideoStart(0, sVideoParam, null);

        String sAudioParam = "";
        mLive.AudioStart(0, sAudioParam);

        mParams = new WindowManager.LayoutParams();
        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams.gravity = Gravity.TOP | Gravity.LEFT;

        // 以屏幕左上角为原点，设置x、y初始值
        mParams.x = 0;
        mParams.y = 0;

        //设置悬浮窗口长宽数据
        mParams.width = 1;
        mParams.height = 1;
        mManager.addView(mLayout, mParams);
        mLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveStop();
        unregisterReceiver(sendMessage);
    }

    private void LiveStop() {
        mLive.AudioStop(0);
        mLive.VideoStop(0);

        if (mView != null) {
            mLayout.removeView(mView);
            mLive.CameraViewRelease();
            mLayout = null;
            mView = null;
            mLive.Clean();
        }
    }

    private boolean CheckPlugin() {
        if (pgLibJNINode.Initialize(this)) {
            pgLibJNINode.Clean();
            return true;
        } else {
            Log.e("Error", "Please import 'pgPluginLib' peergine middle ware!");
            return false;
        }
    }

    private pgLibLiveMultiCapture.OnEventListener m_OnEvent = new pgLibLiveMultiCapture.OnEventListener() {

        @Override
        public void event(String sAct, String sData, String sRenID) {
            handler.obtainMessage(0, new String[]{sAct, sData, sRenID}).sendToTarget();

        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int iRotate = 90;
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int rotation = display.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                iRotate += 0;
                break;

            case Surface.ROTATION_90:
                iRotate += -90;
                break;

            case Surface.ROTATION_180:
                iRotate += -180;
                break;

            case Surface.ROTATION_270:
                iRotate += -270;
                break;
            default:
                break;
        }

        iRotate = (iRotate + 360) % 360;

        SetRotate(iRotate);
    }

    public void SetRotate(int iAngle) {
        pgLibJNINode Node = mLive.GetNode();
        if (Node != null) {
            if (Node.ObjectAdd("_vTemp", "PG_CLASS_Video", "", 0)) {
                Node.ObjectRequest("_vTemp", 2, "(Item){2}(Value){" + iAngle + "}", "");
                Node.ObjectDelete("_vTemp");
            }
        }
    }


    public BroadcastReceiver sendMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(P2PCameraComm.SEND_MSG_TO_SERVER)) {
                String json = intent.getStringExtra("json");
                mLive.NotifySend(json);
            }
        }
    };

}
