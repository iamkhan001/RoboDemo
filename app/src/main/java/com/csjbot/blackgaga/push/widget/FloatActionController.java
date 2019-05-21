package com.csjbot.blackgaga.push.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.csjbot.blackgaga.push.service.FloatViewService;

/**
 *Created by  Wql , 2018/2/27 15:32
 * Des:与悬浮窗交互的控制类，
 */
public class FloatActionController {

    private FloatViewService mFloatViewService = new FloatViewService();

    private Intent intent;

    public FloatActionController() {
    }

    public static FloatActionController getInstance() {
        return LittleMonkProviderHolder.sInstance;
    }

    // 静态内部类
    private static class LittleMonkProviderHolder {
        private static final FloatActionController sInstance = new FloatActionController();
    }

    /**
     * 开启服务悬浮窗
     */
    public void startMonkServer(Context context) {
            Intent intent = new Intent(context,FloatViewService.class);
            context.startService(intent);
            context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 关闭悬浮窗
     */
    public void stopMonkServer(Context context) {
        Intent intent = new Intent(context, FloatViewService.class);
        context.stopService(intent);
    }

    /**
     * 悬浮窗的显示
     */
    public void show() {
        if ( mFloatViewService != null ) {
            mFloatViewService.showFloat();
        }
    }

    /**
     * 悬浮窗的隐藏
     */
    public void hide() {
        if ( mFloatViewService != null ) {
            mFloatViewService.hideFloat();
        }
    }
    /**
     * 释放PJSDK数据
     */
    public void destroy() {
        try {
            mFloatViewService.stopService(intent);
            mFloatViewService.unbindService(mServiceConnection);
        } catch (Exception e) {
        }
    }
    /**
     * 连接到Service
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mFloatViewService = ((FloatViewService.FloatViewServiceBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mFloatViewService = null;
        }
    };
}
