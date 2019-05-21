package com.csjbot.blackgaga.feature.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.SearchAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.RichTextUtil;

import butterknife.BindView;

/**
 * Created by xiasuhuei321 on 2017/10/18.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.INFO_SEARCH)
public class SearchActivity extends BaseModuleActivity {

    @BindView(R.id.theQuery)
    WebView mTheQuery;
    private Button bt_sellService;
    private Button bt_aboutVip;
    private Button bt_priceLow;
    private Button bt_joinService;
    private Button bt_helpCenter;

    private Button lastClick;

    SearchAI mAI;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        bt_sellService = (Button) findViewById(R.id.bt_sellService);
        bt_aboutVip = (Button) findViewById(R.id.bt_aboutVip);
        bt_priceLow = (Button) findViewById(R.id.bt_priceLow);
        bt_joinService = (Button) findViewById(R.id.bt_joinService);
        bt_helpCenter = (Button) findViewById(R.id.bt_helpCenter);
        lastClick = bt_sellService;


        getTitleView().setBackVisibility(View.VISIBLE);
        bt_sellService.setOnClickListener(v -> {
            initWebView("https://www.zhouheiya.cn/index.php/index-show-tid-19.html");
//            Toast.makeText(SearchActivity.this, R.string.selled_service, Toast.LENGTH_SHORT).show();
            selectBgChange(bt_sellService);
        });

        bt_aboutVip.setOnClickListener(v -> {
            initWebView("https://www.zhouheiya.cn/index.php/index-show-tid-2.html");
//            Toast.makeText(SearchActivity.this, R.string.about_vip, Toast.LENGTH_SHORT).show();
            selectBgChange(bt_aboutVip);
        });

        bt_priceLow.setOnClickListener(v -> {
            initWebView("https://www.zhouheiya.cn/index.php/index-show-tid-31.html");
//            Toast.makeText(SearchActivity.this, R.string.price_low, Toast.LENGTH_SHORT).show();
            selectBgChange(bt_priceLow);
        });

        bt_helpCenter.setOnClickListener(v -> {
            initWebView("https://www.zhouheiya.cn/index.php/index-show-tid-19.html");
//            Toast.makeText(SearchActivity.this, R.string.helper_center, Toast.LENGTH_SHORT).show();
            selectBgChange(bt_helpCenter);
        });

        bt_joinService.setOnClickListener(v -> {
            initWebView("https://www.zhouheiya.cn/index.php/index-show-tid-46.html");
//            Toast.makeText(SearchActivity.this, R.string.join_servcie, Toast.LENGTH_SHORT).show();
            //  selectBgChange(bt_aboutVip);
            selectBgChange(bt_joinService);
        });
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        int color = Color.parseColor("#0099ff");
        SpannableStringBuilder ssb = new RichTextUtil().append("您好,请说出您要选择的查询类型\n")
                .append("\t1.售后服务", color, v -> customerService())
                .append("\t2.关于会员", color, v -> aboutVip())
                .append("\t3.优惠服务", color, v -> discountService())
                .append("\t4.帮助中心", color, v -> helpCenter())
                .append("\t5.加盟服务", color, v -> joinService())
                .finish();
        return ssb;
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return null;
    }

    private void selectBgChange(Button bt) {
        lastClick.setBackgroundResource(R.drawable.text_normal);
        lastClick.setTextColor(getResources().getColor(R.color.darkgray));
        bt.setBackgroundResource(R.drawable.text_select);
        bt.setTextColor(getResources().getColor(android.R.color.white));
        lastClick = bt;
    }

    public void customerService() {
        selectBgChange(bt_sellService);
    }

    public void aboutVip() {
        selectBgChange(bt_aboutVip);
    }

    public void discountService() {
        selectBgChange(bt_priceLow);
    }

    public void helpCenter() {
        selectBgChange(bt_helpCenter);
    }

    public void joinService() {
        selectBgChange(bt_joinService);
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
    public void init() {
        super.init();
        mAI = SearchAI.newInstance();
        mAI.initAI(this);
        initWebView("https://www.zhouheiya.cn/");
    }

    void initWebView(String loadUrl) {
        mTheQuery.loadUrl(loadUrl);
        mTheQuery.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
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
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        SearchAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mTheQuery != null) {
            mTheQuery.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mTheQuery.clearHistory();

            ((ViewGroup) mTheQuery.getParent()).removeView(mTheQuery);
            mTheQuery.destroy();
            mTheQuery = null;
        }
        super.onDestroy();
    }
}
