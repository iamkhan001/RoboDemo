package com.csjbot.blackgaga.feature.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.router.BRouterPath;

import butterknife.BindView;

/**
 * Created by xiasuhuei321 on 2017/10/19.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */
@Route(path= BRouterPath.ABOUT_US_PATH)
public class AboutUsActivity extends BaseModuleActivity {
    @BindView(R.id.wv_aboutus)
    WebView aboutUs;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_aboutus : R.layout.activity_aboutus;

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
        aboutUs.loadUrl("https://www.zhouheiya.cn/index.php/index-show-tid-2.html");
        aboutUs.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }
    @Override
    protected void onDestroy() {
        if (aboutUs != null) {
            aboutUs.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            aboutUs.clearHistory();

            ((ViewGroup) aboutUs.getParent()).removeView(aboutUs);
            aboutUs.destroy();
            aboutUs = null;
        }
        super.onDestroy();
    }
}
