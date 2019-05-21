package com.csjbot.blackgaga.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.proxy.SpeakProxy;
import com.csjbot.blackgaga.widget.BatteryView;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnChargetStateListener;
import com.csjbot.coshandler.listener.OnRobotStateListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.iflytek.cloud.SpeechError;


/**
 * 电量检测服务
 * Created by jingwc on 2017/12/21.
 */

public class BatteryService extends BaseService {

    /**
     * 未充电状态
     */
    public static final int NO_CHARGING = 0;
    /**
     * 正在充电状态
     */
    public static final int CHARGING = 1;
    /**
     * 已充满状态
     */
    public static final int BATTERY_FULL = 2;

    /**
     * 当前充电状态
     */
    public volatile static int state = NO_CHARGING;

    volatile boolean isGoHome;

    volatile boolean isLoop;

    WindowManager mWindowManager;

    WindowManager.LayoutParams wmBatteryParams;

    BatteryView mBateryView;

    RobotManager robotManager;

    Handler handler;

    Dialog dialog = null;

    int batterFullCount;


    @Override
    public void onCreate() {
        super.onCreate();
        createBatteryView();
        init();
        startQuery();

    }

    /**
     * 启动电量查询
     */
    private void startQuery() {
        new Thread(() -> {
            while (isLoop) {
                if (state == NO_CHARGING) {
                    robotManager.robot.reqProxy.getBattery();
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "GetBetteryThread").start();
    }

    /**
     * 停止电量查询
     */
    private void stopQuery() {
        isLoop = false;
    }

    /**
     * 初始化
     */
    private void init() {
        isLoop = true;
        isGoHome = false;
        handler = new Handler();
        robotManager = RobotManager.getInstance();
        robotManager.addListener(new OnChargetStateListener() {
            @Override
            public void response(int state) {
                BatteryService.state = state;
                if (state == BATTERY_FULL) {
                    if(batterFullCount > 2){
                        return;
                    }
                    ServerFactory.getSpeakInstance().startSpeaking(getString(R.string.electricity_is_full), null);
                    batterFullCount++;
                } else if (state == CHARGING) {
                    batterFullCount = 0;
                    ServerFactory.getExpressionInstantce().lightning();
                    handler.post(() -> mBateryView.setBatteryImage(R.drawable.charge));
                } else if (state == NO_CHARGING) {
                    batterFullCount = 0;
                    ServerFactory.getExpressionInstantce().normal();
                    robotManager.robot.reqProxy.getBattery();
                }
            }
        });
        robotManager.addListener(new OnRobotStateListener() {
            @Override
            public void getBattery(int battery) {
                // 如果电量状态是未充电
                if (state == NO_CHARGING) {
                    isGoHome = false;
                    // 更新电量显示
                    handler.post(() -> batteryShow(battery));

                    // 判断是否达到回去充电的低电量标准,以及判断是否开启自动充电
                    CsjlogProxy.getInstance().info("battery is {}, LowElectricity is {}, ChargingPile is {}, AutoCharging is {}",
                            battery, Constants.Charging.getLowElectricity(), Constants.Charging.getChargingPile(), Constants.Charging.getAutoCharging());

                    if (dialog != null && dialog.isShowing()) {
                        return;
                    }

                    if (Constants.Charging.isGoCharging(battery) && !isGoHome) {

                        ServerFactory.getExpressionInstantce().sleepiness();

                        handler.post(() -> showDialog());

                        if (!Constants.Charging.getAutoCharging()) {
                            ServerFactory.getSpeakInstance().startSpeaking(getResources().getString(R.string.pow_to_low), null);
                            return;
                        }

                        SpeakProxy.getInstance().startSpeaking(getApplicationContext().getString(R.string.charge_to_low), new OnSpeakListener() {
                            @Override
                            public void onSpeakBegin() {
                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                                NaviAction.getInstance().isPause = true;
                                sendLowElectricity();
                                new Thread(() -> {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    /**
                                     * 判断是否使用充电桩
                                     */
                                    if (Constants.Charging.getChargingPile()) {
                                        // 回到开机点,并寻找充电桩
                                        ServerFactory.getChassisInstance().goHome();
                                    } else {
                                        // 回到开机点,不寻找充电桩
                                        String json = "{" + "\"x\":0," + "\"y\":0," + "\"z\":0," + "\"rotation\":0" + "}";
                                        ServerFactory.getChassisInstance().navi(json);
                                    }
                                    isGoHome = true;
                                }).start();

                            }
                        });
                    }
                }
            }
        });
    }

    private void showDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            final View view = View.inflate(getApplicationContext(), R.layout.dialog_hint, null);
            TextView message = view.findViewById(R.id.tv_message);
            Button l = view.findViewById(R.id.button);
            message.setText(R.string.pow_to_low);
            builder.setView(view)
                    .setCancelable(false);
            dialog = builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
            l.setOnClickListener(v -> {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
            });
        }
    }

    /**
     * 发送低电量回充广播
     * (通知所有对正在移动的功能终止移动)
     */
    public void sendLowElectricity() {
        Intent intent = new Intent("com.example.BROADCAST");
        getApplicationContext().sendBroadcast(intent);
    }

    private void batteryShow(int battery) {
        if (battery <= 20) {
            mBateryView.setBatteryImage(R.drawable.battery20);
        } else if (battery > 20 && battery <= 40) {
            mBateryView.setBatteryImage(R.drawable.battery40);
        } else if (battery > 40 && battery <= 60) {
            mBateryView.setBatteryImage(R.drawable.battery60);
        } else if (battery > 60 && battery <= 80) {
            mBateryView.setBatteryImage(R.drawable.battery80);
        } else if (battery > 80 && battery <= 100) {
            mBateryView.setBatteryImage(R.drawable.battery100);
        }
    }

    private void createBatteryView() {
        mBateryView = new BatteryView(getApplicationContext());
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
        wmBatteryParams.gravity = Gravity.RIGHT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmBatteryParams.x = 0;
        wmBatteryParams.y = 0;

        // 设置悬浮窗口长宽数据
        wmBatteryParams.width = 80;
        wmBatteryParams.height = 50;
        mWindowManager.addView(mBateryView, wmBatteryParams);
    }


    private void removeBatteryView() {
        if (mBateryView != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mBateryView);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopQuery();
        removeBatteryView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
