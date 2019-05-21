package com.csjbot.blackgaga.feature.content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.ContentAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.clothing.ClothingListActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.ContentTypeListListener;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.WebSettingUtil;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/24.
 * @Package_name: BlackGaGa
 */
@Route(path = BRouterPath.ONELEVEL)
public class ContentActivity extends BaseModuleActivity {
    @BindView(R.id.network_webview_sales)
    WebView network_webview;
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.sales)
    RelativeLayout sales;

    ContentAI mAI;

    private static ContentTypeBean contentTypeBean = null;
    private BRouter.ContentUrlBody url = null;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)) {
            if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
                return R.layout.vertical_activity_content;
            } else {
                return R.layout.activity_content_xingzheng;
            }
        } else {
            return getCorrectLayoutId(R.layout.activity_content, R.layout.vertical_activity_content);
        }
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        network_webview = WebSettingUtil.initWebViewSetting(network_webview);
        network_webview.setWebChromeClient(new WebChromeClient() {
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
        mAI = new ContentAI();
        mAI.initAI(this);
        initdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    private void initdata() {
        mPb.setVisibility(View.VISIBLE);
        BRouter.ContentUrlBody url = new Gson().fromJson(getIntent().getStringExtra(BRouterKey.CONTENT_KEY), BRouter.ContentUrlBody.class);
        if (url == null) {
            mPb.setVisibility(View.GONE);
            return;
        }
        ProductProxy.newProxyInstance().getContentTypeListener(url.getResId(), new ContentTypeListListener() {
            @Override
            public void getContentTypeList(ContentTypeBean bean) {
                contentTypeBean = bean;
                mPb.setVisibility(View.GONE);
                initDataView();
            }

            @Override
            public void onContentTypeError(Throwable e) {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void onLocaleContentTypeList(ContentTypeBean bean) {

            }

            @Override
            public void ImageSize(int num) {

            }

            @Override
            public void loadSuccess(String name) {
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void cacheToMore() {

            }

            @Override
            public void loadFailed(String name) {
            }

            @Override
            public void loadAllSuccess() {
                mPb.setVisibility(View.GONE);
            }

            @Override
            public void loadError500() {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                mPb.setVisibility(View.GONE);
            }
        });
    }

    private void initDataView() {
        url = new Gson().fromJson(getIntent().getStringExtra(BRouterKey.CONTENT_KEY), BRouter.ContentUrlBody.class);
        List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> listBeen = null;
        try {
            listBeen = contentTypeBean.getResult().getContentMessage().getMessageList();
        } catch (Exception e) {
        }

        if (contentTypeBean == null || listBeen == null || listBeen.size() == 0) {
            network_webview.setVisibility(View.GONE);
        } else {
            network_webview.loadUrl(URLDecoder.decode(listBeen.get(0).getCcontentUrl()));
            network_webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    //                    network_webview.loadUrl("javascript:(function() { " +
                    //                            "var videos = document.getElementsByTagName('audio');" +
                    //                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                    mPb.setVisibility(View.GONE);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    mPb.setVisibility(View.VISIBLE);
                }
            });
        }
        initSpeak();
    }

    public void initSpeak() {
//        if (Constants.Language.CURRENT_LANGUAGE == Constants.Language.CHINESE) {
        new Handler().postDelayed(() -> {
            if (url == null) {
                return;
            }
            String speakStr = url.getWords();
            speak(speakStr);
            setRobotChatMsg(speakStr);
        }, 500);
//        }
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "";
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "";
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && network_webview.canGoBack()) {
            network_webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null
                && !Constants.ClothProduct.clothTypeBean.getResult().isEmpty()) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_LIST,
                            ClothingListActivity.SELECT_TYPE, resultBean.getSecondLevel());
                    return true;
                }
            }
        }
        if (!mAI.dynamicHandle(text)) {
            prattle(answerText);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentTypeBean = null;
        sales.removeView(network_webview);
        network_webview.clearCache(true);
        network_webview.clearHistory();
        network_webview.destroy();
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


}
