package com.csjbot.blackgaga.feature.viphall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.viphall.adapter.XianYangVipHallAdapter;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.ContentTypeListListener;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.WebSettingUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Ben
 */
public class XianYangVipHallActivity extends BaseModuleActivity {


    @BindView(R.id.pb_xianyang_all)
    ProgressBar pbXianyangAll;
    @BindView(R.id.recy_xianyang_viphall)
    RecyclerView recyXianyangViphall;
    @BindView(R.id.pb_xianyang)
    ProgressBar pbXianyang;
    @BindView(R.id.web_xianyang_viphall)
    WebView webXianyangViphall;

    private XianYangVipHallAdapter mAdapter;

    private ContentTypeBean mTypeBean;
    private List<ContentTypeBean.ResultBean.ContentTypeMessageBean.MessageListBean> mMessageList;

    private static final String VIP_HALL = "vip_hall";
    private static final String OK = "ok";
    private static final String CONTENT_NAME = "贵宾厅服务";

    private String mName;
    private String resUrl;
    private String id;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getMessage();
                    break;
                case 2:
                    pbXianyangAll.setVisibility(View.GONE);
                    recyXianyangViphall.setVisibility(View.VISIBLE);
                    getMessageDetail();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_xian_yang_vip_hall;
    }

    @Override
    public void init() {
        super.init();
        //接收从其他界面传过来的需要选择子菜单的内容
        Intent intent = getIntent();
        if (intent != null) {
            mName = intent.getStringExtra(VIP_HALL);
        }
        mAdapter = new XianYangVipHallAdapter(this);
        mMessageList = new ArrayList<>();
        WebSettingUtil.initWebViewSetting(webXianyangViphall);

        recyXianyangViphall.setLayoutManager(new LinearLayoutManager(this));
        recyXianyangViphall.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllContentTypes();
    }

    /**
     * 获取主页面所有内容种类
     */
    private void getAllContentTypes() {
        recyXianyangViphall.setVisibility(View.GONE);
        pbXianyangAll.setVisibility(View.VISIBLE);
        ProductProxy product = ProductProxy.newProxyInstance();
        ContentBean c = product.getContent();
        if (c == null) {
            ProductProxy proxy = ServerFactory.createProduct();
            proxy.getContent(null);
            new Handler().postDelayed(() -> getAllContentTypes(), 3000);
            return;
        }
        if (TextUtils.equals(c.getMessage(), OK) &&
                c.getResult() != null &&
                c.getResult().getContentType().size() != 0) {
            ContentBean.ResultBean bean = c.getResult();
            List<ContentBean.ResultBean.ContentMessageBean> b = bean.getContentType();
            for (int i = 0; i < b.size(); i++) {
                if (TextUtils.equals(CONTENT_NAME, b.get(i).getName())) {
                    id = b.get(i).getId();
                    resUrl = URLDecoder.decode(b.get(i).getResUrl());
                    mHandler.sendEmptyMessage(1);
                }
            }
        }
    }

    /**
     * 获取二级界面消息体
     */
    private void getMessage() {
        if (TextUtils.isEmpty(resUrl)) {
            CSJToast.showToast(this, getString(R.string.no_data));
            return;
        }
        ProductProxy.newProxyInstance().getContentTypeListener(resUrl, new ContentTypeListListener() {
            @Override
            public void getContentTypeList(ContentTypeBean bean) {
                if (bean != null) {
                    mTypeBean = bean;
                }
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onContentTypeError(Throwable e) {
                if (TextUtils.isEmpty(id)) {
                    return;
                }
                mTypeBean = ProductProxy.newProxyInstance().getContentType(id);
                mHandler.sendEmptyMessage(2);
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

            }

            @Override
            public void loadFailed(String name) {

            }

            @Override
            public void loadAllSuccess() {

            }

            @Override
            public void loadError500() {
                if (TextUtils.isEmpty(id)) {
                    return;
                }
                mTypeBean = ProductProxy.newProxyInstance().getContentType(id);
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    /**
     * 获取二级界面消息体详细内容
     */
    private void getMessageDetail() {
        if (mTypeBean != null &&
                TextUtils.equals(mTypeBean.getMessage(), OK) &&
                mTypeBean.getResult() != null &&
                mTypeBean.getResult().getContentMessage() != null &&
                !mTypeBean.getResult().getContentMessage().getMessageList().isEmpty()) {
            mMessageList.clear();
            mMessageList.addAll(mTypeBean.getResult().getContentMessage().getMessageList());

            mAdapter.setData(mMessageList);
            onSpeakClick(mName);

            mAdapter.setOnItemClickListener(position -> {
                if (!mMessageList.isEmpty() && mMessageList.size() > position) {
                    onItemClick(position);
                }
            });

        } else {
            CSJToast.showToast(this, getString(R.string.no_data), 1000);
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        Log.e("咸阳机场贵宾厅", "onSpeechMessage: "+text);
        if (mMessageList.isEmpty()) {
            return false;
        }

        onSpeakClick(text);
        return true;
    }

    /**
     * 语音选择
     */
    private void onSpeakClick(String name) {
        if (TextUtils.isEmpty(name)) {
            onItemClick(0);
            return;
        }

        for (int i = 0; i < mMessageList.size(); i++) {
            if (name.contains(mMessageList.get(i).getName())) {
                onItemClick(i);
            }
        }
    }

    /**
     * item 点击事件
     *
     * @param position
     */
    private void onItemClick(int position) {
        mAdapter.setItemClick(position);
        setWebView(URLDecoder.decode(mMessageList.get(position).getCcontentUrl()));
    }

    /**
     * 显示指定url的webview内容
     *
     * @param loadUrl
     */
    private void setWebView(String loadUrl) {
        pbXianyang.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(loadUrl)) {
            pbXianyang.setVisibility(View.GONE);
            webXianyangViphall.setVisibility(View.GONE);
            return;
        }
        webXianyangViphall.setVisibility(View.VISIBLE);
        webXianyangViphall.loadUrl(loadUrl);
        webXianyangViphall.setWebChromeClient(new WebChromeClient());
        webXianyangViphall.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                pbXianyang.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }
        });
    }
}
