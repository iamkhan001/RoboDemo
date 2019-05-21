
package com.csjbot.blackgaga.push.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.csjbot.blackgaga.push.widget.FloatView;
import com.csjbot.blackgaga.push.widget.HomeWatcherReceiver;


/**
 * Created by  Wql , 2018/2/27 15:32
 */
public class FloatViewService extends Service{
    /**
     * home键监听
     */
    private HomeWatcherReceiver mHomeKeyReceiver;
    private FloatView mFloatView;

    @Override
    public IBinder onBind(Intent intent) {
        return new FloatViewServiceBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mFloatView = new FloatView(this);
        //注册广播接收者
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyReceiver, homeFilter);
        //开启悬浮窗
        showFloat();
    }

    public void showFloat() {
        if ( mFloatView != null ) {
            mFloatView.show();
        }
    }

    public void hideFloat() {
        if ( mFloatView != null ) {
            mFloatView.hide();
        }
    }

    public void destroyFloat() {
        if ( mFloatView != null ) {
            mFloatView.destroy();
        }
        mFloatView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideFloat();
        destroyFloat();
        //注销广播接收者
        if (null != mHomeKeyReceiver) {
            unregisterReceiver(mHomeKeyReceiver);
        }
    }

    public class FloatViewServiceBinder extends Binder {
        public FloatViewService getService() {
            return FloatViewService.this;
        }
    }
}
