package com.csjbot.blackgaga.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.csjbot.blackgaga.BaseApplication;

/**
 * Created by xiasuhuei321 on 2017/10/30.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:推送、长连接有关的通过这个类来管理
 */

public class NetworkManager {
    // 网络是否可用，默认不可用
    public boolean isNetworkAvaible = false;
    private final BroadcastReceiver receiver;
    private final IntentFilter filter;

    private NetworkManager() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(NetworkConstants.NET_WORK_STATE, NetworkConstants.NETWORK_UNAVAILABLE);
                if (state == NetworkConstants.NETWORK_AVAILABLE) {
                    isNetworkAvaible = true;
                } else {
                    isNetworkAvaible = false;
                }
            }
        };
        filter = new IntentFilter(NetworkConstants.SEND_ACTION);
        BaseApplication.getAppContext().registerReceiver(receiver, filter);
    }

    public static class NetworkManagerHolder {
        public static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManagerHolder.INSTANCE;
    }

    /**
     * 不考虑线程安全，只是一个简单的封装
     */
    public void run(INetworker worker) {
        if (isNetworkAvaible) {
            worker.runAvailable();
        } else {
            worker.runUnavailable();
        }
    }

    public void onDestroy() {
        BaseApplication.getAppContext().unregisterReceiver(receiver);
    }

}
