package com.csjbot.blackgaga.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import java.io.File;

/**
 * Created by jingwc on 2017/10/16.
 */

public class TitleView extends FrameLayout {


    Context mContext;

    /* 设置点击监听事件 */
    OnSettingsListener settingsListener;

    /* 客服点击监听事件 */
    OnCustomerServiceListener customerServiceListener;

    /* 返回首页点击监听事件 */
    OnHomePageListener homePageListener;

    /* 返回上一页点击监听事件 */
    OnBackListener backListener;

    /* 购物车点击监听事件 */
    OnShoppingCartListener shoppingCartListener;

    /* 设置页面返回按钮点击事件 */
    OnSettingsPageBackListener settingsPageBackListener;
    /* 设置语言选择按钮点击事件 */
    OnSetLanguageListener setLanguageListener;

    /* logo图标 */
    ImageView ivLogo;

    /* 语言图标 */
    public ImageView ivLanguage;

    /* 语言文字 */
    public TextView tvLanguage;

    /* 客服 */
    RelativeLayout customerService;

    /* 客服图标 */
    ImageView ivCustomerService;

    /* 客服文字 */
    TextView tvCustomerService;

    /* 设置 */
    public RelativeLayout settings;

    /* 设置图标 */
    ImageView ivSettings;

    /* 设置文字 */
    TextView tvSettings;

    /* 首页与返回的父布局 */
    LinearLayout back;

    /* 首页 */
    TextView tvHomePage;

    /* 返回 */
    TextView tvBack;

    /* 购物车 */
    RelativeLayout shoppingCart;

    /* 购物数量 */
    TextView tvShoppingCount;

    /* 设置页面的返回功能 */
    LinearLayout setBack;

    TextView tv_set_back;

    private String induStry;

    public TitleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化布局文件
     */
    void init(Context context) {
        induStry = SharedPreUtil.getString(SharedKey.MAINPAGE, SharedKey.MAINPAGE_KEY, "");

        mContext = context;

        // 加载布局到当前View中
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            LayoutInflater.from(mContext).inflate(R.layout.layout_plus_title, this, true);
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.layout_title, this, true);
        }

        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        tvLanguage = (TextView) findViewById(R.id.tv_language);
        ivLanguage = (ImageView) findViewById(R.id.iv_language);
        ivCustomerService = (ImageView) findViewById(R.id.iv_customer_service);
        tvCustomerService = (TextView) findViewById(R.id.tv_customer_service);
        ivSettings = (ImageView) findViewById(R.id.iv_settings);
        tvSettings = (TextView) findViewById(R.id.tv_settings);

        settings = (RelativeLayout) findViewById(R.id.settings);
        customerService = (RelativeLayout) findViewById(R.id.customer_service);

        back = (LinearLayout) findViewById(R.id.back);
        tvHomePage = (TextView) findViewById(R.id.tv_home_page);
        tvBack = (TextView) findViewById(R.id.tv_back);

        shoppingCart = (RelativeLayout) findViewById(R.id.shopping_cart);
        tvShoppingCount = (TextView) findViewById(R.id.tv_shopping_count);

        setBack = (LinearLayout) findViewById(R.id.set_back);
        tv_set_back = (TextView) findViewById(R.id.tv_set_back);

//        tvLanguage.setOnClickListener(v -> setLanguageListener.onClick());
        ivLanguage.setOnClickListener(v -> setLanguageListener.onClick());

        setBack.setOnClickListener(v -> settingsPageBackListener.onClick());

        settings.setOnClickListener(v -> settingsListener.onClick());
        customerService.setOnClickListener(v -> customerServiceListener.onClick());

        tvHomePage.setOnClickListener(v -> homePageListener.onClick());
        tvBack.setOnClickListener(v -> backListener.onClick());

        shoppingCart.setOnClickListener(v -> shoppingCartListener.onClick());
    }

    public RelativeLayout getShoppingCart() {
        return shoppingCart;
    }

    /**
     * 设置Logo图标
     */
    public void setLogo(int resId) {
        if (ivLogo != null) ivLogo.setImageResource(resId);
    }

    public void setLogo(String url) {
        if (ivLogo != null) Glide.with(mContext).load(url).into(ivLogo);
    }

    public void setSDLogo(String filePath) {
        if (ivLogo != null) {
            if(new File(filePath).exists()){
                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                ivLogo.setImageBitmap(bmp);
            }
//            Glide.with(this).load(filePath).into(ivLogo);
        }
    }

    /**
     * 设置logo显示与隐藏
     */
    public void setLogoVisibility(int visibility) {
        if (ivLogo != null) ivLogo.setVisibility(visibility);
    }

    /**
     * 设置当前语言图标
     */
    public void setLanguageImage(int resId) {
//        if (tvLanguage != null) tvLanguage.setBackgroundResource(resId);
        if (ivLanguage != null) ivLanguage.setImageResource(resId);
    }

    /**
     * 设置当前语言文字
     */
    public void setLanguageText(int resId) {
//        if (tvLanguage != null) tvLanguage.setText(resId);
    }

    /**
     * 设置当前语言文字
     */
    public void setLanguageText(String text) {
        if (tvLanguage != null) tvLanguage.setText(text);
    }

    /**
     * 设置当前语言文字显示与隐藏
     */
    public void setLanguageTextVisibility(int visibility) {
        if (tvLanguage != null) tvLanguage.setVisibility(visibility);
    }


    /**
     * 设置首页文字显示与隐藏
     */
    public void setHomePageTextVisibility(int visibility) {
        //根据场景影藏首页图标
        if (induStry.equals("jichang")) {
            if (tvHomePage != null)
                tvHomePage.setVisibility(GONE);
        } else {
            if (tvHomePage != null) tvHomePage.setVisibility(visibility);
        }

    }

    public void setTvHomePageText(String text){
        if(tvHomePage != null){
            tvHomePage.setText(text);
        }
    }

    public void setTvBackText(String text){
        if(tvBack != null){
            tvBack.setText(text);
        }
    }

    public TextView getTvHomePage(){
        return tvHomePage;
    }

    public TextView getTvBack(){
        return tvBack;
    }

    /**
     * 设置返回文字显示与隐藏
     */
    public void setBackTextVisibility(int visibility) {
        if (tvBack != null) tvBack.setVisibility(visibility);
    }

    /**
     * 设置语言的显示与隐藏
     */
    public void setLanguageVisibility(int visibility) {
//        if (ivLanguage != null) ivLanguage.setVisibility(visibility);
        if (tvLanguage != null) tvLanguage.setVisibility(visibility);
    }

    /**
     * 设置客服图标
     */
    public void setCustomerServiceImage(int resId) {
        if (ivCustomerService != null) ivCustomerService.setImageResource(resId);
    }

    /**
     * 设置客服文字
     */
    public void setCustomerServiceText(int resId) {
        if (tvCustomerService != null) tvCustomerService.setText(resId);
    }

    /**
     * 设置客服文字显示与隐藏
     */
    public void setCustomerServiceTextVisibility(int visibility) {
        if (tvCustomerService != null) tvCustomerService.setVisibility(visibility);
    }

    /**
     * 设置客服的显示与隐藏
     */
    public void setCustomerServiceVisibility(int visibility) {
        if (ivCustomerService != null) ivCustomerService.setVisibility(visibility);
        if (tvCustomerService != null) tvCustomerService.setVisibility(visibility);
    }

    /**
     * 设置-设置图片
     */
    public void setSettingsImage(int resId) {
        if (ivSettings != null) ivSettings.setImageResource(resId);
    }

    /**
     * 设置-设置文字
     */
    public void setSettingsText(int resId) {
        if (tvSettings != null) tvSettings.setText(resId);
    }

    /**
     * 设置-设置文字
     */
    public void setSettingsText(String text) {
        if (tvSettings != null) tvSettings.setText(text);
    }

    /**
     * 设置-设置文字显示与隐藏
     */
    public void setSettingsTextVisibility(int visibility) {
        if (tvSettings != null) tvSettings.setVisibility(visibility);
    }

    /**
     * 设置购物数量
     */
    public void setShoppingCount(String text) {
        if (tvShoppingCount != null) tvShoppingCount.setText(text);
    }


    /**
     * 设置-设置的显示与隐藏
     */
    public void setSettingsVisibility(int visibility) {
        Log.i("TAG", "setSettingsVisibility:" + visibility);
        if (ivSettings != null) ivSettings.setVisibility(visibility);
        if (tvSettings != null) tvSettings.setVisibility(visibility);
    }

    /**
     * 设置返回首页和返回上一页的导航的显示与隐藏
     */
    public void setBackVisibility(int visibility) {
        if (back != null) back.setVisibility(visibility);
    }

    /**
     * 设置购物车的显示与隐藏
     */
    public void setShoppingCartVisibility(int visibility) {
        if (induStry.equals("jichang")) {
            if (shoppingCart != null)
                shoppingCart.setVisibility(GONE);
        } else {
//            if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
//                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
//                if (visibility == View.VISIBLE) {
//                    if (settings != null) settings.setVisibility(View.GONE);
//                } else {
//                    if (settings != null) settings.setVisibility(View.VISIBLE);
//                }
//            }

            if (shoppingCart != null) {
                shoppingCart.setVisibility(visibility);
            }
        }
    }

    /**
     * 设置购物数量的显示与隐藏
     *
     * @param visibility
     */
    public void setShoppingCountVisibility(int visibility) {
        if (induStry.equals("jichang")) {
            if (tvShoppingCount != null) tvShoppingCount.setVisibility(GONE);
        } else {
            if (tvShoppingCount != null) tvShoppingCount.setVisibility(visibility);
        }

    }

    public void setSettingsPageBackVisibility(int visibility) {
        if (setBack != null) setBack.setVisibility(visibility);
    }


    /**
     * 设置-设置监听事件
     */
    public void setSettingsListener(OnSettingsListener listener) {
        this.settingsListener = listener;
    }

    /**
     * 设置客服监听事件
     */
    public void setCustomerServiceListener(OnCustomerServiceListener listener) {
        this.customerServiceListener = listener;
    }

    /**
     * 设置语言监听事件
     */
    public void setSetLanguageListener(OnSetLanguageListener listener) {
        this.setLanguageListener = listener;
    }


    public void setHomePageListener(OnHomePageListener listener) {
        this.homePageListener = listener;
    }

    public void setBackListener(OnBackListener listener) {
        this.backListener = listener;
    }

    public void setShoppingCartListener(OnShoppingCartListener listener) {
        this.shoppingCartListener = listener;
    }

    public void setSettingsPageBackListener(OnSettingsPageBackListener listener) {
        this.settingsPageBackListener = listener;
    }

    public ImageView getIvSettings(){
        return ivSettings;
    }

    public TextView getTvLanguage(){
        return tvLanguage;
    }

    public TextView getTvSetBack(){
        return tv_set_back;
    }

    public RelativeLayout getSettins(){
        return settings;
    }

    /**
     * 设置点击事件
     */
    public interface OnSettingsListener {
        void onClick();
    }

    /**
     * 客服点击事件
     */
    public interface OnCustomerServiceListener {
        void onClick();
    }

    /**
     * 返回首页点击事件
     */
    public interface OnHomePageListener {
        void onClick();
    }

    /**
     * 返回上一页点击事件
     */
    public interface OnBackListener {
        void onClick();
    }

    /**
     * 购物车点击事件
     */
    public interface OnShoppingCartListener {
        void onClick();
    }

    /**
     * 设置页面的返回按键点击事件
     */
    public interface OnSettingsPageBackListener {
        void onClick();
    }


    /**
     * 设置更改语言点击实际
     */
    public interface OnSetLanguageListener {
        void onClick();
    }
}
