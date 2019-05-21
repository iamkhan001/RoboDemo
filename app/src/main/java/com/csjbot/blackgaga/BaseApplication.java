package com.csjbot.blackgaga;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.csjbot.blackgaga.customer_service.CustomerHelpService;
import com.csjbot.blackgaga.jpush.manager.CsjJPushManager;
import com.csjbot.blackgaga.model.http.factory.RetrofitFactory;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.util.CustomSDCardLoader;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShellUtils;
import com.csjbot.blackgaga.util.locationutil.LocationUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.log.Csjlogger;
import com.iflytek.cloud.SpeechUtility;

import cn.jpush.android.api.JPushInterface;
import skin.support.SkinCompatManager;
import skin.support.design.SkinMaterialManager;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:初始化的各种操作
 */

public class BaseApplication extends Application {

    static Application app;
    public static boolean isFirstComing = true;

    public volatile static boolean isToAppGoHome = false;

    public volatile static boolean isPushSkinEnd = false;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        /*换肤初始化*/
        SkinMaterialManager.init(this);
        SkinCompatManager.withoutActivity(this).addStrategy(new CustomSDCardLoader());
        SkinCompatManager.init(this).loadSkin();
        /* 初始化retrofit */
        RetrofitFactory.initClient();
//        /* 第一次加载产品数据 */
//        firstGetData();
        /* 科大讯飞appid */
        SpeechUtility.createUtility(this, "appid=" + "Please replace it with your key");
//        /* 初始化度秘 */
//        initDuer();
//        /* 初始化推送 */
//        BPush.init(this);
        BRouter.init(this, BuildConfig.DEBUG);

        setUncaughtExceptionHandler();
        initBaiduMap();
//        ConfInfoUtil.init();


//        CrashReport.initCrashReport(getApplicationContext(), "99de59fd2e", false);
//        String sn;
//        if (!TextUtils.isEmpty(ConfInfoUtil.getSN())) {
//            sn = ConfInfoUtil.getSN();
//        } else {
//            sn = ProductProxy.SN;
//        }
//        Logo.getLogo(sn);

//        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("for_test")) {
//            CrashReport.setUserId(this, sn + " te");
//        } else {
//            CrashReport.setUserId(this, sn);
//        }

//        setVoice();//关机后恢复之前设置的音量
//        NaviAction.getInstance().initData();
//        Constants.Charging.initCharging();

//        JPushInterface.setDebugMod(true);
        JPushInterface.init(getApplicationContext());
        CsjJPushManager.getInstance().initManager(getApplicationContext());
//        CsjJPushManager.getInstance().setAlias((int) System.currentTimeMillis(), ConfInfoUtil.getSN());


        // 百度统计

        // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
        // 查看方法：adb logcat -s sdkstat

        if (BuildConfig.BUILD_TYPE.equals("debug") || BuildConfig.BUILD_TYPE.equals("for_test")) {
//        StatService.setDebugOn(true);
        }

        startService(new Intent(this, CustomerHelpService.class));


        SharedPreUtil.putInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 0);//冷启动
        CsjlogProxy.getInstance().info("冷启动");
    }

    private void setVoice() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = SharedPreUtil.getInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, 8);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    private void setUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            // 输出异常！
            throwable.printStackTrace();
            CsjlogProxy.getInstance().error(throwable.toString());
            if (!BuildConfig.DEBUG) {
                Csjlogger.debug("-异常重启-");
                new Thread(() -> {
//                    com.csjbot.blackgaga.model.tcp.factory.ServerFactory.getSpeakInstance().startSpeaking("程序异常,正在重启!", null);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    ShellUtils.execCommand("am force-stop com.csjbot.blackgaga\nam start -n com.csjbot.blackgaga/com.csjbot.blackgaga.SplashActivity", true, false);

                }).start();
            }
        });

    }


    /**
     * 第一次加载产品数据
     */
    private void firstGetData() {
        ProductProxy proxy = ServerFactory.createProduct();
        proxy.getRobotMenuList(true);
        proxy.getScene(null);
        proxy.downLoadLogo();
        proxy.getContent(null);
    }

    public static Application getAppContext() {
        return app;
    }


    /**
     * 初始化百度地图和定位
     */
    private void initBaiduMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LocationUtil.init(this);
        LocationUtil.start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
