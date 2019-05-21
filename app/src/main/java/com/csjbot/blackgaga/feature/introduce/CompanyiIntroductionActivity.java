package com.csjbot.blackgaga.feature.introduce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.VideoView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;

import butterknife.BindView;

/**
 * Created by  Wql , 2018/2/28 13:23
 * 公司介绍
 */
public class CompanyiIntroductionActivity extends BaseModuleActivity {


    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.qr_code_small)
    ImageView mQrCodeSmall;
    @BindView(R.id.qr_code_max)
    ImageView mQrCodeMax;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        playVideo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
        webview.loadUrl("https://www.zhouheiya.cn/index.php/index-show-tid-2.html");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

    public void playVideo() {
//        String uri = "android.resource://" + getPackageName() + "/" + R.raw.jiudian;
//        videoview.setVideoPath(uri);
//        videoview.start();
//        videoview.setOnPreparedListener(mp -> {
//            mp.start();
//            mp.setLooping(true);
//        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_companyi_introduction;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoview != null) {
            videoview.stopPlayback();
            videoview.suspend();
            videoview = null;
        }

        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();

            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }

    }
}
