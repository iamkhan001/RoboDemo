package com.csjbot.blackgaga;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.advertisement.service.AudioService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.jpush.manager.CsjJPushManager;
import com.csjbot.blackgaga.jpush.service.GetMessageService;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.logo.LogoServiceImpl;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.BaseBackstageListener;
import com.csjbot.blackgaga.network.CheckEthernetService;
import com.csjbot.blackgaga.network.NetworkListenerService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.service.NuanceService;
import com.csjbot.blackgaga.service.UpdateAppService;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2018/4/4.
 */

public class SplashActivity extends BaseModuleActivity {

    public static String START = "START_MODE";

    // 冷启动
    public static final int COLD_START = 0;

    // 热启动
    public static final int HEAT_START = 1;

    // 启动模式
    private int mStartMode = COLD_START;

    // 点击次数
    private int mClickCount;

    // 主页是否获取完毕
    private boolean isHomePageSuccess, isLoading;


    @BindView(R.id.bt_default_home)
    Button bt_default_home;

    @BindView(R.id.splash_image)
    ImageView splash_image;

    @BindView(R.id.showInfoTextView)
    TextView showInfoTextView;

    private Handler mHandler = new Handler();

    private Runnable mShowRb = () -> {
        bt_default_home.setVisibility(View.VISIBLE);
    };


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        super.init();
        if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
            Glide.with(this).load(R.drawable.splash_plus).into(splash_image);
        } else {
            Glide.with(this).load(R.drawable.splash).into(splash_image);
        }

        // 检查网络Service
        startService(new Intent(this, CheckEthernetService.class));

        //消息推送Service
        startService(new Intent(this, GetMessageService.class));
        CsjJPushManager.getInstance().setAlias((int) System.currentTimeMillis(), Robot.SN);
        //检查App更新Service
        startService(new Intent(this, UpdateAppService.class));
        //广告Service
        if (!(BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
            startService(new Intent(this, AudioService.class));
        } else {
            startService(new Intent(this, PlusVideoService.class));
        }

        initInternational();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mStartMode = getIntent().getIntExtra(START, COLD_START);
        mStartMode = SharedPreUtil.getInt(SharedKey.STARTMODE, SharedKey.STARTMODE, 0);
        if (mStartMode == COLD_START) {// 冷启动
            // 初始化配置
            initConfig();
            mHandler.postDelayed(mShowRb, 1000 * 60 * 2);
        } else if (mStartMode == HEAT_START) {// 热启动
            // 由主页切换语言功能跳转而来,直接跳转主页即可
            new LogoServiceImpl().getLogo(Robot.SN);
            jumpHomePage();
        }
    }

    boolean skinTest = false;
    private String testTheme = "cheguansuo";

    private void initConfig() {
//        if (skinTest) {
//            BRouter.toTestHome(testTheme);
//            return;
//        }
        // sn
//        ConfInfoUtil.init();
        Robot.initSN();

        // logo
//        Logo.getLogo(ConfInfoUtil.getSN());

        // 音量恢复
        setVoice();

        // 导航数据初始化
        NaviAction.getInstance().initData();

        // 电量信息初始化
        Constants.Charging.initCharging();


        // 网络检查服务
        startService(new Intent(this, NetworkListenerService.class));

        // 数据加载
//        loopCheck();
        isLoading = true;
        loadProductData();

        Constants.UnknownProblemAnswer.initUnknownProblemAnswer();


        Constants.isOpenChatView = SharedPreUtil.getBoolean(SharedKey.CHAT_VIEW, SharedKey.CHAT_VIEW_IS_OPEN, true);
    }

    private void initInternational() {
        int international = SharedPreUtil.getInt(SharedKey.INTERNATIONALIZATION, SharedKey.INTERNATIONALIZATION, 0);

        if (international == 0) {
            Constants.isOpenNuance = false;
        } else if (international == 1) {
            Constants.isOpenNuance = true;
            startService(new Intent(this, NuanceService.class));
        }
        CsjlogProxy.getInstance().info("international:" + international);
    }

    private void setVoice() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volume = SharedPreUtil.getInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, 8);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 检查Wifi如果是关闭状态则开启Wifi
     */
    public void checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 循环检查主页是否获取完毕
     */
    private void loopCheck() {
        new Thread(() -> {
            boolean isLoop = true;
            while (isLoop) {
                if (Constants.HomePage.isHomePageLoadSuccess) {
                    isLoop = false;
                    jumpHomePage();
                }
            }
        }).start();
    }

    /**
     * 主页跳转
     */
    private void jumpHomePage() {
        int languageMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
        refreshLanguage(languageMode);
        BRouter.toHome();
    }

    /**
     * 刷新本地语言
     *
     * @param mode
     */
    private void refreshLanguage(int mode) {
        Configuration config = getApplication().getResources().getConfiguration();
        switch (mode) {
            case Constants.Language.CHINESE:
                config.locale = Locale.CHINA;
                break;
            case Constants.Language.ENGELISH_US:
                config.locale = Locale.US;
                break;
            case Constants.Language.ENGELISH_UK:
                config.locale = Locale.UK;
                break;
            case Constants.Language.KOREAN:
                config.locale = Locale.KOREAN;
                break;
            case Constants.Language.FRANCH_FRANCE:
                config.locale = new Locale("fr", "FR");
                break;
            case Constants.Language.SPANISH_SPAIN:
                config.locale = new Locale("es", "ES");
                break;
            case Constants.Language.PORTUGUESE_PORTUGAL:
                config.locale = new Locale("pt", "PT");
                break;
            case Constants.Language.INDONESIA:
                config.locale = new Locale("in", "ID");
                break;
            case Constants.Language.RUSSIAN:
                config.locale = new Locale("ru", "RU");
                break;
            default:
                break;

        }
        getApplication().getResources().updateConfiguration(config, getApplication().getResources().getDisplayMetrics());
    }


    private void setInfo(String info) {
        runOnUiThread(() -> showInfoTextView.append(info + "\n"));
    }

    /**
     * 加载产品数据
     */
    private void loadProductData() {
        ProductProxy proxy = ServerFactory.createProduct();
        proxy.getRobotMenuList(true);
        proxy.getScene(new BaseBackstageListener() {
            @Override
            public void loadSuccess(String name) {
                isHomePageSuccess = true;
                isLoading = false;
                if (getTopActivity(getApplicationContext()).contains(SplashActivity.class.getSimpleName())) {
                    jumpHomePage();
                }
//                if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
//                        || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
//                    startService(new Intent(SplashActivity.this, PlusVideoService.class));
//                }
            }

            @Override
            public void cacheToMore() {
                isLoading = false;
            }

            @Override
            public void loadFailed(String name) {
                isHomePageSuccess = false;
                isLoading = false;
            }

            @Override
            public void loadAllSuccess() {
                isLoading = false;
            }

            @Override
            public void loadError500() {
                isLoading = false;
                isHomePageSuccess = false;
            }
        });
//        proxy.downLoadLogo();
        proxy.getContent(null);
        new LogoServiceImpl().getLogo(Robot.SN);
    }

    @OnClick(R.id.bt_default_home)
    public void goDefaultHomePage() {
        BRouter.toHome();
    }

    @OnClick(R.id.rl_root)
    public void rl_root() {
        // 点击6次进入默认主页面
        mClickCount++;
        if (mClickCount == 6) {
            BRouter.toHome();
            mClickCount = 0;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // onPause状态中直接销毁该页面
        SplashActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public void networkAvailability() {
        super.networkAvailability();
        if (!isHomePageSuccess && !isLoading) {
            CsjlogProxy.getInstance().warn("2222 isHomePageSuccess");
            loadProductData();
        }
    }

    /**
     * 获得栈中最顶层的Activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else
            return "";
    }
}
