package com.csjbot.blackgaga.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.SplashActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.nuance.NuanceLanguages;
import com.csjbot.blackgaga.service.NuanceService;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.roobo.vui.api.VUIApi;

import java.util.Locale;

/**
 * Created by jingwc on 2018/4/4.
 */

public class LanguageWindow extends PopupWindow {

    Context mContext;

    LanguageView mLanguageView;

    int mCurrentLanguage;

    public LanguageWindow(Context context) {
        super(context);
        this.mContext = context;

        setContentView(mLanguageView = new LanguageView(context));

        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        setAnimationStyle(R.style.language_list_anim_style);

        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);

        setTouchInterceptor((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                return true;
            }
            return false;
        });

        initView();

        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        mCurrentLanguage = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
        mLanguageView.setLanguageClickListener(new LanguageView.LanguageClickListener() {
            @Override
            public void onChineseClick() {
                if (mCurrentLanguage != Constants.Language.CHINESE) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
                    refreshLanguage(Constants.Language.CHINESE);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:中文");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Mandarin_China);
                }
            }

            @Override
            public void onEnglishUSClick() {
                if (mCurrentLanguage != Constants.Language.ENGELISH_US) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.ENGELISH_US);
                    refreshLanguage(Constants.Language.ENGELISH_US);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:美国英语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.English_US);
                }
            }

            @Override
            public void onEnglishUKClick() {
                if (mCurrentLanguage != Constants.Language.ENGELISH_UK) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.ENGELISH_UK);
                    refreshLanguage(Constants.Language.ENGELISH_UK);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:英国英语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.English_UK);
                }
            }

            @Override
            public void onEnglishCanadaClick() {

            }

            @Override
            public void onEnglishIndiaClick() {

            }

            @Override
            public void onJapaneseClick() {
                if (mCurrentLanguage != Constants.Language.JAPANESE) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.JAPANESE);
                    refreshLanguage(Constants.Language.JAPANESE);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:日语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Japanese);
                }

            }

            @Override
            public void onKoreanClick() {
                if (mCurrentLanguage != Constants.Language.KOREAN) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.KOREAN);
                    refreshLanguage(Constants.Language.KOREAN);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:韩语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Korean);
                }
            }

            @Override
            public void onFranchFranceClick() {
                if (mCurrentLanguage != Constants.Language.FRANCH_FRANCE) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.FRANCH_FRANCE);
                    refreshLanguage(Constants.Language.FRANCH_FRANCE);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:法语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.French_France);
                }
            }

            @Override
            public void onFranchCanadaClick() {

            }

            @Override
            public void onSpanishSpainClick() {
                if (mCurrentLanguage != Constants.Language.SPANISH_SPAIN) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.SPANISH_SPAIN);
                    refreshLanguage(Constants.Language.SPANISH_SPAIN);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:西班牙语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Spanish_Spain);
                }
            }

            @Override
            public void onSpanishLatAmClick() {

            }


            @Override
            public void onPortuguesePortugalClick() {
                if (mCurrentLanguage != Constants.Language.PORTUGUESE_PORTUGAL) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.PORTUGUESE_PORTUGAL);
                    refreshLanguage(Constants.Language.PORTUGUESE_PORTUGAL);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:葡萄牙语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Portuguese_Portugal);
                }
            }

            @Override
            public void onPortugueseBrazilClick() {

            }


            @Override
            public void onIndonesiaClick() {
                if (mCurrentLanguage != Constants.Language.INDONESIA) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.INDONESIA);
                    refreshLanguage(Constants.Language.INDONESIA);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:印尼语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Bahasa_Indonesia);
                }
            }

            @Override
            public void onRussianClick() {
                if (mCurrentLanguage != Constants.Language.RUSSIAN) {
                    SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.RUSSIAN);
                    refreshLanguage(Constants.Language.RUSSIAN);
                    jumpSplashAct();
                }
                LanguageWindow.this.dismiss();
                if(Constants.isOpenNuance){
                    CsjlogProxy.getInstance().info("切换语言:俄语");
                    VUIApi.getInstance().setCloudRecognizeLang(NuanceLanguages.Russian);
                }
            }
        });
    }

    private void jumpSplashAct() {
        if (mContext != null) {
            Constants.ChangeLan = true;
            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.putExtra(SplashActivity.START, SplashActivity.HEAT_START);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }

    /**
     * 刷新本地语言
     *
     * @param mode
     */
    private void refreshLanguage(int mode) {
        Configuration config = mContext.getResources().getConfiguration();
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
            case Constants.Language.JAPANESE:
                config.locale = Locale.JAPAN;
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
                config.locale = Locale.CHINA;
                break;
        }
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }
}
