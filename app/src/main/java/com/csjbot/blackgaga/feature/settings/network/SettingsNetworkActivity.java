package com.csjbot.blackgaga.feature.settings.network;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.coshandler.global.RobotContants;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/20.
 */

public class SettingsNetworkActivity extends BaseModuleActivity implements SettingsNetworkContract.View {

    SettingsNetworkContract.Presenter mPresenter;

    @BindView(R.id.network_webview)
    WebView network_webview;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_network, R.layout.activity_settings_network_plus);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();

        mPresenter = new SettingsNetworkPresenter();
        mPresenter.initView(this);
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);

        initWebView();
    }

    void initWebView() {
        network_webview.getSettings().setJavaScriptEnabled(true);
        String wirelessURL;
        // 如果是1.0 版本的硬件，就用老的
        if (RobotManager.getInstance().getHardWareVersion() == RobotContants.ROBOT_VERSION.ROBOT_VERSION_1_0) {
            wirelessURL = "http://192.168.99.1/cgi-bin/luci?username=root&&password=admin&getcontent=0";
        } else {

            wirelessURL = "http://192.168.99.1/cgi-bin/luci/admin/network/wireless?luci_username=admin&luci_password=admin";
        }

        network_webview.loadUrl(wirelessURL);
        network_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                hiddenDiv();
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPb.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 使用js代码隐藏Div布局
     */
    public void hiddenDiv() {
        // 使用js隐藏div方法
        if (RobotManager.getInstance().getHardWareVersion() == RobotContants.ROBOT_VERSION.ROBOT_VERSION_1_0) {
            network_webview.loadUrl("javascript:function hidden(){document.getElementsByClassName('nav')[0].style.display='none';};");
        } else {
//            network_webview.loadUrl("javascript:function hidden(){document.getElementsByClassName('container')[0].style.display='none';};");
        }
//         调用隐藏div的function
        network_webview.loadUrl("javascript:hidden();");
        // 显示wifi中继页面
        network_webview.loadUrl("javascript:changePanel('wdsopt');");


        if (!network_webview.isShown()) {
            network_webview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    protected void onDestroy() {
        if (network_webview != null) {
            network_webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            network_webview.clearHistory();

            ((ViewGroup) network_webview.getParent()).removeView(network_webview);
            network_webview.destroy();
            network_webview = null;
        }
        super.onDestroy();
    }
}
