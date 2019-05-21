package com.csjbot.blackgaga;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.ApiUrl;
import com.csjbot.blackgaga.model.http.bean.SceneBean;
import com.csjbot.blackgaga.network.NetworkListenerService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.service.UpdateAppService;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.SharedPreferencesSDUtil;
import com.csjbot.blackgaga.util.StrUtil;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = BRouterPath.MAIN)
public class MainActivity extends BaseFullScreenActivity {

    private static boolean isFirstIn = true;

    @BindView(R.id.bt_english)
    Button mBtEnglish;
    @BindView(R.id.bt_chinese)
    Button mBtChinese;
    @BindView(R.id.bt_japanese)
    Button mBtJapanese;
    @BindView(R.id.bt_english_selected)
    Button mBtEnglishSelected;
    @BindView(R.id.bt_chinese_selected)
    Button mBtChineseSelected;
    @BindView(R.id.bt_japanese_selected)
    Button mBtJapaneseSelected;
    private String tempURL;
    private SceneBean sceneBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    /**
     * 是否停留在当前页面
     */
    public static final String STAY = "stay";

    @Override
    public void init() {
        super.init();
        startService(new Intent(this, UpdateAppService.class));

        getTitleView().setBackVisibility(View.VISIBLE);
        getTitleView().setBackListener(() -> {
            BRouter.toHome();
        });
        if(!BaseApplication.isFirstComing) {
            if (!getIntent().getBooleanExtra(STAY, false)) {
                if (isGoHome()) {
                    return;
                }
            }
        }
        mMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
        showLanguage(mMode, false);
        if (BaseApplication.isFirstComing) {
            BaseApplication.isFirstComing = false;
            if(BaseApplication.isToAppGoHome){
                this.finish();
            }
            new Thread(() -> {
                boolean isLoop = true;
                while(isLoop){
                    if(BaseApplication.isPushSkinEnd){
                        isLoop = false;
                        if(BaseApplication.isToAppGoHome){
                            runOnUiThread(()->{
                                MainActivity.this.finish();
                            });
                        }
                    }
                }
            }).start();
//            recreate();
        }
    }

    private boolean isGoHome() {
        int i = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, -1);
        if (i != -1) {
            BRouter.toHome();
            this.finish();
            return true;
        }else{
            getTitleView().setBackVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setLanguageVisibility(View.GONE);

        startService(new Intent(MainActivity.this, NetworkListenerService.class));

        checkWifi();
    }


    private void selectBgChange(int mode) {
        switch (mode) {
            case 0:
                mBtEnglish.setVisibility(View.GONE);
                mBtEnglishSelected.setVisibility(View.VISIBLE);
                break;
            case 1:
                mBtChinese.setVisibility(View.GONE);
                mBtChineseSelected.setVisibility(View.VISIBLE);
                break;

            case 2:
                mBtJapanese.setVisibility(View.GONE);
                mBtJapaneseSelected.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }


    @Override
    public boolean isOpenChat() {
        return true;
    }

    @OnClick({R.id.bt_english, R.id.bt_english_selected, R.id.bt_japanese, R.id.bt_japanese_selected, R.id.bt_chinese, R.id.bt_chinese_selected})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_chinese_selected:
            case R.id.bt_chinese:
                showLanguage(Constants.Language.CHINESE, true);
                SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
                BRouter.toHome();
                finish();
                break;
            case R.id.bt_english_selected:
            case R.id.bt_english:
                showLanguage(Constants.Language.ENGELISH_US, true);
                SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.ENGELISH_US);
                BRouter.toHome();
                finish();
                break;
            case R.id.bt_japanese_selected:
            case R.id.bt_japanese:
                showLanguage(Constants.Language.JAPANESE, true);
                SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.ENGELISH_US);
                BRouter.toHome();
                finish();
                break;
        }
//        ShellUtil.execCmd("am force-stop com.csjbot.blackgaga\nam start -n com.csjbot.blackgaga/com.csjbot.blackgaga.MainActivity", true, false);
    }

//    private void toHome() {
//
//    }

    //    private void toHome() {
    //        //        shangsha yinhang shipin zhanguan qiche gongshang chezhan xingzheng jiaoyu fayuan jiadian chaoshi
    //        //        jianchayuan gongan fuzhuang shuiwu yiyuan yaodian jichang jiudian
    //        sceneBean = ProductProxy.newProxyInstance().getSceneRes();
    //        String isScenBean = new Gson().toJson(sceneBean);
    //        if (isScenBean != null && sceneBean != null) {
    //            String name = sceneBean.getResult().getIndustry();
    //            if (name != null) {
    //                switch (name) {
    //                    case "shangsha":
    //                        jumpActivity(BRouterPath.MAINPAGE_SHANGSHA);
    //                        tempURL = BRouterPath.MAINPAGE_SHANGSHA;
    //                        break;
    //                    case "jiadian":
    //                        jumpActivity(BRouterPath.MAINPAGE_JIADIAN);
    //                        tempURL = BRouterPath.MAINPAGE_JIADIAN;
    //                        break;
    //                    case "yaodian":
    //                        jumpActivity(BRouterPath.MAINPAGE_YAODIAN);
    //                        tempURL = BRouterPath.MAINPAGE_YAODIAN;
    //                        break;
    //                    case "fuzhuang":
    //                        jumpActivity(BRouterPath.MAINPAGE_FUZHUANG);
    //                        tempURL = BRouterPath.MAINPAGE_FUZHUANG;
    //                        break;
    //                    case "chaoshi":
    //                        jumpActivity(BRouterPath.MAINPAGE_CHAOSHI);
    //                        tempURL = BRouterPath.MAINPAGE_CHAOSHI;
    //                        break;
    //                    case "qiche":
    //                        jumpActivity(BRouterPath.MAINPAGE_QICHE);
    //                        tempURL = BRouterPath.MAINPAGE_QICHE;
    //                        break;
    //                    case "shipin":
    //                        jumpActivity(BRouterPath.MAINPAGE_SHIPIN);
    //                        tempURL = BRouterPath.MAINPAGE_SHIPIN;
    //                        break;
    //                    case "shuiwu":
    //                        jumpActivity(BRouterPath.MAINPAGE_SHUIWU);
    //                        tempURL = BRouterPath.MAINPAGE_SHUIWU;
    //                        break;
    //                    default:
    //                        jumpActivity(BRouterPath.MAINPAGE_CSJBOT);
    //                        tempURL = BRouterPath.MAINPAGE_CSJBOT;
    //                        break;
    //                }
    //                SharedPreUtil.putString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, tempURL);
    //            } else {
    //                jumpActivity(BRouterPath.MAINPAGE_CSJBOT);
    //            }
    //        } else {
    //            jumpActivity(BRouterPath.MAINPAGE_CSJBOT);
    //
    //
    //        }
    //    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this,UpdateAppService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        urlInit();
        selectBgChange(mMode);
        String urlUtil = (String) SharedPreferencesSDUtil.get(this, "urlUtil", SharedKey.DEFAULT_ADRESS, "");
        if (TextUtils.isEmpty(urlUtil)) {
            urlUtil = (String) SharedPreferencesSDUtil.get(this, "urlUtil", SharedKey.TEST_ADRESS, "");

        }
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
        //        ApiUrl.DEFAULT_ADRESS = urlUtil;
    }

    /**
     * 如果本地不存在接口文件就將阿裡云接口緩存在本地
     */
    private void urlInit() {
        if (!new File("/sdcard/blackgaga/data/urlUtil.xml").exists()) {
            if (StrUtil.isBlank((String) SharedPreferencesSDUtil.get(this, "urlUtil", SharedKey.DEFAULT_ADRESS, ""))) {
                SharedPreferencesSDUtil.put(this, "urlUtil", SharedKey.DEFAULT_ADRESS, ApiUrl.DEFAULT_ADRESS);
            }
        }

    }


    /**
     * @param language_mode
     * @param isFreash      刷新本地语言的方法
     */
    public void showLanguage(int language_mode, boolean isFreash) {
        Configuration config = context.getResources().getConfiguration();
        switch (language_mode) {
            case 0:
                config.locale = Locale.UK;
                break;
            case 1:
                config.locale = Locale.CHINA;
                break;
            case 2:
                config.locale = Locale.JAPAN;
                break;

        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        if (isFreash) {
            refresh();
        }
    }

    /**
     * 刷新界面
     */
    private void refresh() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

}
