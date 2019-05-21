package com.csjbot.blackgaga.feature.content3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFragment;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.WebSettingUtil;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 孙秀艳 on 2018/2/1.
 */

public class Content3Fragment extends BaseFragment {
//    @BindView(R.id.title)
//    TextView titleView;

    @BindView(R.id.webview)
    WebView webView;

    private String mUrl;

    public static Content3Fragment newInstance(String contentUrl) {
        Content3Fragment fragment = new Content3Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("contentUrl", contentUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content3, container, false);
        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            mUrl = getArguments().getString("contentUrl");
            Log.e("网址", "Content3Fragment: " + URLDecoder.decode(mUrl));
        }
        showWebView(mUrl);
        return rootView;
    }

    private void showWebView(String contentUrl) {
        BlackgagaLogger.debug("contentUrl" + contentUrl);
        webView = WebSettingUtil.initWebViewSetting(webView);
        webView.loadUrl(URLDecoder.decode(contentUrl));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 当WebView进度改变时更新窗口进
                if (newProgress == 100) {
                    System.out.println("chenqi 网页加载完毕！");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('video');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('audio');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
}
