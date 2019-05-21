package com.csjbot.blackgaga.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.csjbot.blackgaga.service.BaseService;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.ShellUtils;
import com.csjbot.coshandler.log.CsjlogProxy;

/**
 * Created by xiasuhuei321 on 2017/10/27.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class NetworkListenerService extends BaseService {
    public static final String TAG = "NetworkListenerService";
    private Thread thread;
    private boolean threadRunning = false;
    private boolean runFlag = true;
    private volatile boolean isNetWorkConnected = false;
    private volatile int disConnectCount = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createWorkThread();
        BlackgagaLogger.debug("onCreate");
        thread.start();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(networkStateReceiver, filter);
        new Thread(() -> {
            while (true) {
                ShellUtils.CommandResult result =
                        ShellUtils.execCommand(String.format("ping -c 1 %s", NetworkConstants.HOST), true, true);
                boolean ret = result.result == 0;
                if (ret) {
                    String successMsg = result.successMsg;
                    int beginIndex = successMsg.indexOf("time=");
                    int endIndex = successMsg.indexOf("ms");
                    try {
                        String pingStr = "ping:" + successMsg.substring((beginIndex + 5), (endIndex - 1));
                        CsjlogProxy.getInstance().printPing(pingStr);
                    } catch (StringIndexOutOfBoundsException e) {
//                        CsjlogProxy.getInstance().printPing("error");
                    }
                } else {
                    CsjlogProxy.getInstance().printPing("ping:timeout");
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void createWorkThread() {
        thread = new Thread(networkWorker, "NetWork Thread");

        threadRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isNetWorkConnected = false;
        return super.onStartCommand(intent, flags, startId);
    }

    public void stopThread() {
        runFlag = false;
    }

    boolean isFirst = true;
    Runnable networkWorker = () -> {
        try {
            while (runFlag) {
//                BlackgagaLogger.debug("线程中检查网络连接状态");

                if (ping(null)) {
//                    BlackgagaLogger.debug("网络为可用状态===来自于ping验证");
                    disConnectCount = 0;
                    // ping 通
                    if (!isNetWorkConnected) {
                        // 如果原来网络为不可用，变为可用，发送广播
                        sendBroadcast(NetworkConstants.NETWORK_AVAILABLE);
                        isNetWorkConnected = true;
                    }
                } else {
//                    BlackgagaLogger.debug("网络连接断开===来自于ping验证");
                    disConnectCount++;
                    if (disConnectCount >= 3) {
                        // 没 ping 通，且三次未 ping 通
                        if (isNetWorkConnected || isFirst) {
                            // 如果原来网络可用，变为不可用，发送广播
                            sendBroadcast(NetworkConstants.NETWORK_UNAVAILABLE);
                            BlackgagaLogger.debug("发送网络断开广播");
                            isNetWorkConnected = false;
                            isFirst = false;
                        }
                        disConnectCount = 1;
                    } else if (disConnectCount != 0) {
                        // 如果为0说明有其他的ping通了，不再继续检查
                        BlackgagaLogger.debug("第 " + (disConnectCount - 1) + " 次检测到网络连接断开");
//                        handler.postDelayed(this::checkNetwork, 2000);
                    }
                }

                if (disConnectCount == 0) {
                    Thread.sleep(10000);
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private void sendBroadcast(int state) {
        Intent intent = new Intent();
        intent.setAction(NetworkConstants.SEND_ACTION);
        intent.putExtra(NetworkConstants.NET_WORK_STATE, state);
        sendBroadcast(intent);
    }

    /**
     * 利用 Android 原生的广播
     */
    BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // 不一定准，只留下第三个，监听到都断开，通过 ping 判断一次网络
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                checkNetwork();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                checkNetwork();
            } else {
                checkNetwork();
            }

        }
    };

    public void checkNetwork() {
//        if (ping(null)) {
//            BlackgagaLogger.debug("网络为可用状态===来自于ping验证");
//            disConnectCount = 0;
//            // ping 通
//            if (!isNetWorkConnected) {
//                // 如果原来网络为不可用，变为可用，发送广播
//                sendBroadcast(NetworkConstants.NETWORK_AVAILABLE);
//                isNetWorkConnected = true;
//            }
//        } else {
//            BlackgagaLogger.debug("网络连接断开===来自于ping验证");
//            disConnectCount++;
//            if (disConnectCount >= 3) {
//                // 没 ping 通，且三次未 ping 通
//                if (isNetWorkConnected) {
//                    // 如果原来网络可用，变为不可用，发送广播
//                    sendBroadcast(NetworkConstants.NETWORK_UNAVAILABLE);
//                    isNetWorkConnected = false;
//                }
//            } else if (disConnectCount != 0) {
//                // 如果为0说明有其他的ping通了，不再继续检查
//                BlackgagaLogger.debug("第 " + (disConnectCount - 1) + " 次检测到网络连接断开");
//                handler.postDelayed(this::checkNetwork, 2000);
//            }
//        }
    }

    public boolean ping(String host) {
        if (TextUtils.isEmpty(host)) host = NetworkConstants.HOST;

        ShellUtils.CommandResult result =
                ShellUtils.execCommand(String.format("ping -c 1 %s", host), true, true);
        boolean ret = result.result == 0;
        return ret;
    }

    final Handler handler = new Handler();
}
