package com.csjbot.blackgaga.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.manager.AdvertisementManager;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.dialog.NewRetailEdittextDialog;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviActivityNew;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.jpush.diaptch.CsjPushDispatch;
import com.csjbot.blackgaga.jpush.diaptch.constants.ConstantsId;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.BaseBackstageListener;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.proxy.SpeakProxy;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.network.ShellUtil;
import com.csjbot.blackgaga.push.widget.FloatActionController;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.SharePreferenceTools;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.global.CmdConstants;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnDetectPersonListener;
import com.csjbot.coshandler.listener.OnHeadTouchListener;
import com.csjbot.coshandler.listener.OnInitListener;
import com.csjbot.coshandler.listener.OnLinuxRobotTypeListener;
import com.csjbot.coshandler.listener.OnShutdownListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.coshandler.tts.GoogleSpechImpl;
import com.csjbot.coshandler.tts.SpeechFactory;
import com.iflytek.cloud.SpeechError;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

/**
 * HomeService,主要作用:
 * 负责与底层通信的连接
 * 接受底层返回的数据分发处理
 */
public class HomeService extends BaseService {

    /* 启动Activity标识 */
    public static final int START_ACTIVITY = 10;

    public static final int LINUX_CONNECTED = 0;
    public static final int LINUX_DISCONNECT = 1;


    public static boolean IS_SWITCH_LANGUAGE;

    /* 广播接收者 */
    private HomeBroadcast mHomeBroadcast;

    private volatile boolean speechRecognition = false;

    public static int autoCloseSpeechTime;

    private static class MyHandler extends Handler {
        WeakReference<HomeService> myService;

        MyHandler(HomeService service) {
            myService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case START_ACTIVITY:/* 启动指定的Activity */
                    myService.get().startActivity(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }

    }

    /* Handle消息处理 */
    private Handler mHandle = new MyHandler(this);

    /**
     * 根据className启动对应的activity
     *
     * @param className
     */
    private void startActivity(String className) {
        BlackgagaLogger.error("chenqi 是获取到了什么className" + className);
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
        }
        if (c != null) {
            startActivity(new Intent(this, c).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public HomeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CsjlogProxy.getInstance().debug("class:HomeService method:onCreate");
        /* 初始化机器人管理类 */
        initRobot();
        /* 注册广播接收器 */
        regBroadcast();
        //打开远程更新
//        new UpdateApkManagerUtil(getApplicationContext(), false).checkUpdateInfo();

//        //二维码
        FloatActionController.getInstance().startMonkServer(this);

        /**
         * 注册场景监听回调
         */
        CsjPushDispatch.getInstance().addSceneEvent((id, sid, data) -> {
            CsjlogProxy.getInstance().info("SceneEvent:id" + id);
            if (id.equals(ConstantsId.Scene.SCENE_CHANGE)) {
                CsjlogProxy.getInstance().info("SceneEvent:SCENE_CHANGE");
                new ProductProxy().getScene(new BaseBackstageListener() {
                    @Override
                    public void loadSuccess(String name) {

                        ServerFactory.getSpeakInstance().startSpeaking("正在切换场景!机器人应用即将重启！", new OnSpeakListener() {
                            @Override
                            public void onSpeakBegin() {

                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                            }
                        });

                        mHandle.postDelayed(() -> ShellUtil.execCmd("am force-stop com.csjbot.blackgaga\n  " +
                                        "am start -n com.csjbot.blackgaga/com.csjbot.blackgaga.SplashActivity",
                                true, false), 5000);

                    }

                    @Override
                    public void cacheToMore() {

                    }

                    @Override
                    public void loadFailed(String name) {

                    }

                    @Override
                    public void loadAllSuccess() {

                    }

                    @Override
                    public void loadError500() {

                    }
                });
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CsjlogProxy.getInstance().debug("class:HomeService method:onStartCommand");
        if (IS_SWITCH_LANGUAGE) {
            initSpeech();
            switchLanguage();
            IS_SWITCH_LANGUAGE = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void switchLanguage() {
        // 切换语音识别语言
        switch (Constants.Language.CURRENT_LANGUAGE) {
            case Constants.Language.CHINESE:
                CsjlogProxy.getInstance().info("切换到中文语言识别环境");
                String accent = SharedPreUtil.getString(SharedKey.CHINESE_LANGUAGE_TYPE,SharedKey.CHINESE_LANGUAGE_TYPE);
                if(TextUtils.isEmpty(accent)){
                    ServerFactory.getSpeechInstance().setLanguage(CmdConstants.LanguageType.ZH_CN);
                }else {
                    ServerFactory.getSpeechInstance().setLanguageAndAccent(CmdConstants.LanguageType.ZH_CN,accent);
                }
                break;
            case Constants.Language.ENGELISH_US:
            case Constants.Language.ENGELISH_UK:
            case Constants.Language.ENGELISH_AUSTRALIA:
            case Constants.Language.ENGELISH_INDIA:
                CsjlogProxy.getInstance().info("切换到英文语言识别环境");
                ServerFactory.getSpeechInstance().setLanguage(CmdConstants.LanguageType.EN_US);
                break;
            case Constants.Language.JAPANESE:
                CsjlogProxy.getInstance().info("切换到日语语言识别环境");
                ServerFactory.getSpeechInstance().setLanguage(CmdConstants.LanguageType.JA_JP);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化语音合成功能
     */
    private void initSpeech() {
        switch (Constants.Language.CURRENT_LANGUAGE) {
            case Constants.Language.CHINESE: {
                SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.IFLY);
                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                    SpeakProxy.getInstance().setSpeakerName("nannan");
                }

                String storedSpeaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, SharedKey.SPEAKER_KEY, SharedKey.DEFAULT_SPEAKER);
                SpeakProxy.getInstance().setSpeakerName(storedSpeaker);
            }
            break;

            case Constants.Language.JAPANESE:
                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                    SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.JAPAN_SNOW);
                    break;
                }
            case Constants.Language.ENGELISH_US:
            case Constants.Language.ENGELISH_UK:
            case Constants.Language.ENGELISH_AUSTRALIA:
            case Constants.Language.ENGELISH_INDIA:
            case Constants.Language.KOREAN:
            default: {
                SpeakProxy.getInstance().initSpeak(this, SpeechFactory.SpeechType.GOOGLE);

                String language_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_LANGUAGE_NAME;
                String country_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_COUNTRY_NAME;
                String speaker_key = Constants.Language.getISOLanguage() + GoogleSpechImpl.TTS_VOICE_NAME;
                String language = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, language_key, Constants.Language.getISOLanguage());
                String country = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, country_key, "");
                String speaker = SharedPreUtil.getString(SharedKey.SPEAKERVOICE, speaker_key, "");


                CsjlogProxy.getInstance().info("language:"+language);
                CsjlogProxy.getInstance().info("country:"+country);
                CsjlogProxy.getInstance().info("speaker:"+speaker);

                Locale lang;

                // 如果语言是空，就设置成 en_US
                if (TextUtils.isEmpty(language)) {
                    lang = new Locale(Constants.Language.getISOLanguage(), "");
                } else {
                    lang = new Locale(language, country);
                }
                mHandle.postDelayed(() -> {
                    SpeakProxy.getInstance().setLanguage(lang);
                    SpeakProxy.getInstance().setSpeakerName(speaker);
                },1000);
            }
            break;
        }

        float ttsVolume = SharedPreUtil.getFloat(SharedKey.TTS_VOLUME,SharedKey.TTS_VOLUME,1.0f);
        CsjlogProxy.getInstance().info("ttsVolume:"+ttsVolume);
        new Handler().postDelayed(() -> {
            SpeakProxy.getInstance().setVolume(ttsVolume);
        },2000);


    }

    /**
     * 初始化机器人功能
     */
    private void initRobot() {

        autoCloseSpeechTime = SharedPreUtil.getInt(SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, SharedKey.AUTO_SPEECH_RECOGNITION_CLOSE_TIME, 30);

        initSpeech();

        /* 获取机器人管理类的实例 */
        RobotManager robotManager = RobotManager.getInstance();
        /* 初始化监听 */
        robotManager.addListener(new OnInitListener() {
            @Override
            public void success() {
                mHandle.post(() -> {
                    sendBroadcast(LINUX_CONNECTED);
                });
                CsjlogProxy.getInstance().debug("class:HomeService message:linux success");

                /* 唤醒机器人 */
//                ServerFactory.getSpeechInstance().openMicro();

                if(!Constants.isOpenNuance){
                    switchLanguage();
                }

//                ServerFactory.getConfigInstance().setMicroVolume(5);

                if(!Constants.isOpenNuance){
                    // 开启语音多次识别
                    if (!Constants.Scene.CurrentScene.equals(Constants.Scene.JiuDianScene)) {
                        ServerFactory.getSpeechInstance().startIsr();
                    }
                }



                ServerFactory.getRobotState().getRobotHWVersion();
                ServerFactory.getRobotState().getLinuxRobotType();

                mHandle.postDelayed(() -> RobotManager.getInstance().robot.reqProxy.setExpression(REQConstants.Expression.SMILE, REQConstants.Expression.YES, REQConstants.Expression.NO), 10000);

                robotManager.addListener(new OnDetectPersonListener() {
                    @Override
                    public void response(int state) {
                        boolean boo = SharedPreUtil.getBoolean(SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, false);
                        CsjlogProxy.getInstance().info("boo:" + boo);
                        if (boo) {
                            CsjlogProxy.getInstance().info("检测是否有人出现:" + state);
                            if (state == 0) {
                                CsjlogProxy.getInstance().info("发送延时关闭语音识别任务,延时:" + autoCloseSpeechTime + "秒");
                                mHandle.postDelayed(runnableCloseSpeechRecognition, 1000 * autoCloseSpeechTime);
                            } else if (state == 1) {
                                if (speechRecognition) {
                                    CsjlogProxy.getInstance().info("发送取消延时关闭语音识别任务");
                                    mHandle.removeCallbacks(runnableCloseSpeechRecognition);
                                } else {
                                    openSpeechRecognition();
                                }

                            }
                        }
                    }
                });

                if(Constants.isOpenNuance){
                    ServerFactory.getSpeechInstance().stopIsr();
                }

            }

            @Override
            public void faild() {
            }

            @Override
            public void timeout() {
            }

            @Override
            public void disconnect() {
                CsjlogProxy.getInstance().debug("class:HomeService message:linux disconnect");
            }
        });

        /**
         * 唤醒通知
         */
        robotManager.addListener((OnWakeupListener) (angle) -> {

            if(Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)){
                String topActStr = getTopActivity(HomeService.this);
                if(!(topActStr.contains(NaviActivity.class.getSimpleName())
                        || topActStr.contains(NaviActivityNew.class.getSimpleName()))
                        ){
                    if (angle > 0 && angle < 360) {
                        if (angle <= 180) {
                            CsjlogProxy.getInstance().debug("向左转:+" + angle);
                            if(BatteryService.state == BatteryService.NO_CHARGING){
                                RobotManager.getInstance().robot.reqProxy.moveAngle(angle);
                            }
                        } else if (angle > 180) {
                            CsjlogProxy.getInstance().debug("向右转:-" + (360 - angle));
                            if(BatteryService.state == BatteryService.NO_CHARGING){
                                RobotManager.getInstance().robot.reqProxy.moveAngle(-(360 - angle));
                            }

                        }
                    }
                }
            }

            boolean wakeupStop = SharedPreUtil.getBoolean(SharedKey.WAKEUP_STOP, SharedKey.WAKEUP_STOP, false);
            if(!wakeupStop){
                return;
            }

            AdvertisementManager.getInstance().stop();
            sendWakeupBroadcast();
            CsjlogProxy.getInstance().debug("class:HomeService message:wakeup");
            ISpeak speak = ServerFactory.getSpeakInstance();
            if (speak.isSpeaking()) {
                speak.stopSpeaking();
            }
        });

        robotManager.addListener((OnHeadTouchListener) () -> {
            ServerFactory.getSpeakInstance()
                    .startSpeaking(getString(R.string.do_not_touch_head), null);
        });


        robotManager.addListener((OnLinuxRobotTypeListener) type -> {
            String linuxRobotType;
            switch (BuildConfig.robotType) {
                case BuildConfig.ROBOT_TYPE_DEF_SNOW:
                    linuxRobotType = "snow";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS:
                    linuxRobotType = "alicebig";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS:
                    linuxRobotType = "amybig";
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_ALICE:
                default:
                    linuxRobotType = "alice";
                    break;
            }

            if (!type.equals(linuxRobotType)) {
                ServerFactory.getRobotState().setLinuxRobotType(linuxRobotType);
            }
        });

//        robotManager.addListener(new OnDetectPersonListener() {
//            @Override
//            public void response(int state) {
//                CsjlogProxy.getInstance().info("DetectPerson:state"+state);
//                if(state == 1){
//                    // 开启语音多次识别
//                    ServerFactory.getSpeechInstance().startIsr();
//                }else if(state == 0){
//                    // 关闭语音多次识别
//                    ServerFactory.getSpeechInstance().stopIsr();
//                }
//            }
//        });


        robotManager.addListener((OnShutdownListener) () -> {

            new Handler(Looper.myLooper()).post(() -> {

                NewRetailEdittextDialog dialog = new NewRetailEdittextDialog(HomeService.this);
                dialog.setTitle("关机提示");
                dialog.setHintText("请输入密码");
                dialog.setListener(new NewRetailEdittextDialog.OnDialogClickListener() {
                    @Override
                    public void yes(String text) {

                        SharePreferenceTools sharePreferenceTools = new SharePreferenceTools(HomeService.this);
                        if (sharePreferenceTools.getString("pwd_word") != null) {
                            if (sharePreferenceTools.getString("pwd_word").equals(text.trim())) {
                                ServerFactory.getRobotState().shutdown();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(HomeService.this, HomeService.this.getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (text.trim().equals("csjbot")) {
                                ServerFactory.getRobotState().shutdown();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(HomeService.this, HomeService.this.getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void no() {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            });

        });

        /* 连接底层通信 */
        robotManager.connect(this);
    }

    private void openSpeechRecognition() {
        CsjlogProxy.getInstance().info("打开语音识别");
        speechRecognition = true;
        ServerFactory.getSpeechInstance().startIsr();
    }

    private void closeSpeechRecognition() {
        CsjlogProxy.getInstance().info("关闭语音识别");
        speechRecognition = false;
        ServerFactory.getSpeechInstance().stopIsr();
    }

    Runnable runnableCloseSpeechRecognition = this::closeSpeechRecognition;


    @Override
    public void onDestroy() {
        super.onDestroy();
        CsjlogProxy.getInstance().debug("class:service method:onDestroy");
        RobotManager.getInstance().disconnect(this);
        unregBroadcast();
    }

    /**
     * 注册广播
     */
    private void regBroadcast() {
        mHomeBroadcast = new HomeBroadcast(mHandle);
        IntentFilter filter = new IntentFilter();
        filter.addAction(HomeBroadcast.ACTION_NAME);
        registerReceiver(mHomeBroadcast, filter);
    }

    /**
     * 取消广播
     */
    private void unregBroadcast() {
        unregisterReceiver(mHomeBroadcast);
    }

    /**
     * 广播接收者
     */
    public static class HomeBroadcast extends BroadcastReceiver {

        public static final String ACTION_NAME = HomeBroadcast.class.getSimpleName();
        public static final String TYPE = "TYPE";
        public static final String VALUE = "VALUE";

        private Handler mHandle;

        public HomeBroadcast(Handler handler) {
            this.mHandle = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                Bundle bundle = intent.getExtras();
                int type = bundle.getInt(TYPE);
                String value = bundle.getString(VALUE);
                mHandle.obtainMessage(type, value).sendToTarget();
            }
        }
    }

    public void sendBroadcast(int state) {
        Intent intent = new Intent();
        intent.setAction(Constants.CONNECT_LINUX_BROADCAST);
        intent.putExtra(Constants.LINUX_CONNECT_STATE, state);
        sendBroadcast(intent);
    }

    public void sendWakeupBroadcast() {
        Intent intent = new Intent();
        intent.setAction(Constants.WAKE_UP);
        sendBroadcast(intent);
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else
            return null;
    }
}
