package com.csjbot.blackgaga.feature.content2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.Content2AI;
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
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.util.WebSettingUtil;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 孙秀艳 on 2018/1/24.
 * 今日活动
 */
@Route(path = BRouterPath.TWOLEVEL)
public class Content2Activity extends BaseModuleActivity implements Content2Contract.View {
    @BindView(R.id.layout_recyclerview)
    LinearLayout layoutRecyclerView;
    @BindView(R.id.activity_recyclerview)
    RecyclerView recyclerView;//今日活动列表
    @BindView(R.id.activity_webview)
    WebView webView;//今日活动内容 webview形式展示
    @BindView(R.id.pb)
    ProgressBar progressBar;//webview 加载完成前loading显示
    @BindView(R.id.pb_all)
    ProgressBar progressBar2;//webview 加载完成前loading显示

    Content2Contract.Presenter mPresenter;
    Content2Adapter content2Adapter;//今日活动列表adapter
    //    View show_no_data;//今日活动没有数据时view
    List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mLists = new ArrayList<>();//今日活动列表有效数据
    List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> totalLists = new ArrayList<>();//今日活动列表所有数据
    Content2AI mAI;//今日活动AI
    private static ContentTypeBean contentTypeBean = null;
    private BRouter.ContentUrlBody url = null;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo)) {
            if ((BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                    || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))) {
                return R.layout.vertical_activity_content2;
            } else {
                return R.layout.activity_content2_xingzheng;
            }
        } else {
            return getCorrectLayoutId(R.layout.activity_content2, R.layout.vertical_activity_content2);
        }

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        webView = WebSettingUtil.initWebViewSetting(webView);
        mPresenter = new Content2Presenter();
        mPresenter.initView(this);
        initTitleview();
        getChatView().clearChatMsg();
        mAI = new Content2AI();
        mAI.initAI(this);
        initData();
    }

    private void initTitleview() {
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    /**
     * 进入今日活动页面话术
     */
    public void initSpeak() {
//        if(Constants.Language.CURRENT_LANGUAGE == Constants.Language.CHINESE) {
        new Handler().postDelayed(() -> {
            if (url == null) {
                return;
            }
            String ssb = url.getWords();
            if (ssb != null) {
                speak(ssb);
                setRobotChatMsg(ssb);
            }
        }, 500);
//        }
    }

    /**
     * 上进入界面获取今日活动数据
     */
    private void initData() {
        url = new Gson().fromJson(getIntent().getStringExtra(BRouterKey.CONTENT_KEY), BRouter.ContentUrlBody.class);
        progressBar2.setVisibility(View.VISIBLE);
        BlackgagaLogger.debug("chenqi url to activity" + url.getResId());
        if (url == null) {
            progressBar2.setVisibility(View.GONE);
            return;
        }
        ProductProxy.newProxyInstance().getContentTypeListener(url.getResId(), new ContentTypeListListener() {
            @Override
            public void getContentTypeList(ContentTypeBean bean) {
                contentTypeBean = bean;
                progressBar2.setVisibility(View.GONE);
                initDataView();
            }

            @Override
            public void onContentTypeError(Throwable e) {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLocaleContentTypeList(ContentTypeBean bean) {

            }

            @Override
            public void ImageSize(int num) {

            }

            @Override
            public void loadSuccess(String name) {

            }

            @Override
            public void cacheToMore() {
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void loadFailed(String name) {
            }

            @Override
            public void loadAllSuccess() {
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void loadError500() {
                contentTypeBean = ProductProxy.newProxyInstance().getContentType(url.getParentId());
                initDataView();
                progressBar2.setVisibility(View.GONE);
            }
        });

    }

    private void initDataView() {
        totalLists.clear();
        mLists.clear();
        if (contentTypeBean != null && contentTypeBean.getResult() != null && contentTypeBean.getResult().getContentMessage() != null) {
            totalLists = contentTypeBean.getResult().getContentMessage().getMessageList();
        }
        BlackgagaLogger.debug("chenqi initData" + totalLists.size());
        if (totalLists != null && totalLists.size() > 0) {
            for (int i = 0; i < totalLists.size(); i++) {
                if (totalLists.get(i).isEffective()) {
                    mLists.add(totalLists.get(i));
                }
            }
        }
        sort();
        initView();
        initRecycleView();
        initSpeak();
    }

    private void sort() {
        Comparator<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> comparator = (s1, s2) -> {
            // 先排年龄
            if (s1.getOrder() != s2.getOrder()) {
                return s1.getOrder() - s2.getOrder();
            }
            return 0;
        };
        Collections.sort(mLists, comparator);

    }


    private void initView() {
        if (webView == null) {
            webView = (WebView) findViewById(R.id.activity_webview);
        }
        if (mLists == null || mLists.size() <= 0) {
            layoutRecyclerView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
        } else {
            layoutRecyclerView.setVisibility(View.VISIBLE);
            if (webView != null) {
                webView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 今日活动数据recycleview
     */
    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        content2Adapter = new Content2Adapter(this, mLists);
//        showNoDataView();
        recyclerView.setAdapter(content2Adapter);
//        content2Adapter.registerAdapterDataObserver(observer);
//        observer.onChanged();
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        content2Adapter.setOnItemClickListener((view, position, speak) -> {
            if (mLists != null && mLists.size() > position) {
                onClickItem(position);
            }
            runOnUiThread(() -> speak(speak, true));
        });
        if (mLists != null && mLists.size() > 0) {
            onClickItem(0);
        }
    }

    /**
     * 显示指定url的webview内容
     *
     * @param loadUrl
     */
    private void setWebView(String loadUrl) {
        if (loadUrl == null || loadUrl.trim().equals("")) {
            webView.setVisibility(View.GONE);
            return;
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                // 当WebView进度改变时更新窗口进
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("chenqi 网页加载完毕！");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('video');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                    view.loadUrl("javascript:(function() { " +
                            "var videos = document.getElementsByTagName('audio');" +
                            " for(var i=0;i<videos.length;i++){videos[i].play();}})()");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.loadUrl(URLDecoder.decode(loadUrl));
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        });
    }

    /**
     * 话术和点击事件
     *
     * @param position
     */
    public void onClickItem(int position) {
        setWebView(mLists.get(position).getCcontentUrl());
        content2Adapter.setItemClickBg(position);
    }

    /**
     * 语音交互事件
     */
    public void onAIClickItem(int position) {
        setWebView(mLists.get(position).getCcontentUrl());
        content2Adapter.setItemClickBg(position);
        speak(mLists.get(position).getSpeechScript(), true);
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
        if (!mAI.dynamicHandle(text, mLists)) {
            prattle(answerText);
        }
        return true;
    }

    public SpannableStringBuilder getSpeakStr() {
        RichTextUtil richTextUtil = new RichTextUtil();
        if (mLists != null && mLists.size() > 0) {
            richTextUtil.append(getString(R.string.today_act_have));
            String name = "";
            for (int i = 0; i < mLists.size(); i++) {
                ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean messageListBean = mLists.get(i);
                name = (i == mLists.size() - 1) ? messageListBean.getName() : messageListBean.getName() + "，";
                richTextUtil.append(name);
            }
            richTextUtil.append(getString(R.string.query_which_activity));
        }

        return richTextUtil.finish();
    }

    /**
     * 今日活动没有数据
     */
//    public void showNoDataView() {
//        //构建子view想要显示的参数
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                , ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        //加载没有数据的view
//        show_no_data = getLayoutInflater().inflate(R.layout.no_data_show, null);
//        Button button = show_no_data.findViewById(R.id.in_data_setting);
//        TextView textView = show_no_data.findViewById(R.id.body_no_data);
//        textView.setText(R.string.no_data);
//        button.setVisibility(View.GONE);
//
//        ((ViewGroup) recyclerView.getRootView()).addView(show_no_data);
//    }
    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    public void onStop() {
        super.onStop();
//        try {
//            content2Adapter.unregisterAdapterDataObserver(observer);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        contentTypeBean = null;
        super.onDestroy();
    }

//    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
//        @Override
//        public void onChanged() {//设置空view原理都一样，没有数据显示空view，有数据隐藏空view
//            if (content2Adapter.getItemCount() == 0) {
//                show_no_data.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.GONE);
//            } else {
//                show_no_data.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount) {
//            onChanged();
//        }
//
//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
//            onChanged();
//        }
//
//        @Override
//        public void onItemRangeInserted(int positionStart, int itemCount) {
//            onChanged();
//        }
//
//        @Override
//        public void onItemRangeRemoved(int positionStart, int itemCount) {
//            onChanged();
//        }
//
//        @Override
//        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//            onChanged();
//        }
//    };

}
