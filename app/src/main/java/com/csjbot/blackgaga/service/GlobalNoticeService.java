package com.csjbot.blackgaga.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.WindowManager;

import com.csjbot.blackgaga.jpush.diaptch.CsjPushDispatch;
import com.csjbot.blackgaga.jpush.diaptch.constants.ConstantsId;
import com.csjbot.blackgaga.jpush.diaptch.event.GlobalNoticeEvent;
import com.csjbot.blackgaga.widget.NoticeView;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 全局公告服务
 * Created by jingwc on 2018/2/27.
 */

public class GlobalNoticeService extends BaseService implements GlobalNoticeEvent{

    public static final String ACTION_CMD = "ACTION_CMD";

    public static final String ACTION_SHOW = "SHOW";

    public static final String ACTION_HIDE = "HIDE";

    MyBroadcast mBroadcast;

    WindowManager mWindowManager;

    WindowManager.LayoutParams wmBatteryParams;

    NoticeView mNoticeView;

    boolean isShow;

    Handler mHander;

    @Override
    public void onCreate() {
        super.onCreate();
        CsjPushDispatch.getInstance().addGlobalNoticeEvent(this);
        mHander = new Handler();
        createBatteryView();

        IntentFilter filter = new IntentFilter(ACTION_CMD);
        mBroadcast = new MyBroadcast();
        registerReceiver(mBroadcast,filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String noticeText = intent.getStringExtra("noticeText");
            long showTime = intent.getLongExtra("showTime",0);
            CsjlogProxy.getInstance().info("onStartCommand  noticeText:"+noticeText);
            CsjlogProxy.getInstance().info("onStartCommand  showTime:"+showTime);
            if(!TextUtils.isEmpty(noticeText) && showTime > 0){
                showBatteryView();
                if(mNoticeView != null){
                    mNoticeView.setNotice(noticeText);
                }
                mHander.postDelayed(runnable,showTime);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void createBatteryView() {
        mNoticeView = new NoticeView(getApplicationContext());
        wmBatteryParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmBatteryParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmBatteryParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmBatteryParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmBatteryParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmBatteryParams.x = 0;
        wmBatteryParams.y = 150;

        // 设置悬浮窗口长宽数据
        wmBatteryParams.width = 800;
        wmBatteryParams.height = 120;
//        mWindowManager.addView(mNoticeView, wmBatteryParams);
    }

    private void showBatteryView(){
        if(!isShow) {
            if (mNoticeView == null) {
                createBatteryView();
            } else {
                mWindowManager.addView(mNoticeView, wmBatteryParams);
            }
            isShow = true;
        }

    }


    private void removeBatteryView() {
        if(isShow) {
            if (mNoticeView != null) {
                //移除悬浮窗口
                mWindowManager.removeView(mNoticeView);
                isShow = false;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removeBatteryView();
        CsjPushDispatch.getInstance().removeGlobalNoticeEvent(this);
        unregisterReceiver(mBroadcast);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void pushEvent(String id, int sid, String data) {
        switch (id){
            case ConstantsId.GlobalNoticeId.NOTICE_GLOBAL_OPEN:
                if(mNoticeView != null){
                    mNoticeView.setNotice("");
                }
                showBatteryView();
                break;
            case ConstantsId.GlobalNoticeId.NOTICE_GLOBAL_CLOSE:
                removeBatteryView();
                break;
            case ConstantsId.GlobalNoticeId.NOTICE_GLOBAL_MESSAGE:
                try {
                    mHander.removeCallbacks(runnable);
                    String text = new JSONObject(data).getString("text");
                    String endTimeStr = new JSONObject(data).getString("endTime");

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(endTimeStr);
                    } catch (ParseException e) {
                        CsjlogProxy.getInstance().error("e:"+e.toString());
                    }
                    long endTime = date.getTime();
                    if(System.currentTimeMillis() < endTime){
                        long showTime = endTime - System.currentTimeMillis();
                        if(mNoticeView != null){
                            mNoticeView.setNotice(text);
                        }
                        mHander.postDelayed(runnable,showTime);
                    }
                } catch (JSONException e) {
                    CsjlogProxy.getInstance().error("e:"+e.toString());
                }
                break;
            default:
                break;
        }
    }

    Runnable runnable = () -> {
        removeBatteryView();
    };

    public class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getStringExtra(ACTION_CMD);
                if(action.equals(ACTION_SHOW)){
                    showBatteryView();
                }else if(action.equals(ACTION_HIDE)){
                    removeBatteryView();
                }
            }
        }
    }
}
