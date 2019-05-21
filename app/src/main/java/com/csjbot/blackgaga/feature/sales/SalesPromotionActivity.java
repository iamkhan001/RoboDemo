package com.csjbot.blackgaga.feature.sales;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.SalesBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.widget.LoopImageHolderView;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * 促销活动
 * Created by jingwc on 2018/3/29.
 */

public class SalesPromotionActivity extends BaseModuleActivity {

    @BindView(R.id.convenient_banner)
    ConvenientBanner convenient_banner;

    private List<SalesBean> mSalesBeans;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sales_promotion;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        convenient_banner.setOnItemClickListener(position -> {
            if (mSalesBeans != null && mSalesBeans.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("sales", mSalesBeans.get(position));
                getLog().info("img1" + mSalesBeans.get(position).getDetailImg());
                getLog().info("img2" + mSalesBeans.get(position).getLocationImg());
                getLog().info("position" + position);
                jumpActivity(SalesPromotionDetailActivity.class, bundle);
            }
        });

        ServerFactory.createApi().getSales(ProductProxy.SN, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody body) {
                String bodyJson = "";
                try {
                    bodyJson = body.string();
                } catch (IOException e) {
                }
                getLog().info("getSales:onNext");
                getLog().info("getSales:bodyJson:" + bodyJson);
                if (!TextUtils.isEmpty(bodyJson)) {
                    try {
                        String json = new JSONObject(bodyJson).getJSONObject("result").getJSONArray("activityList").toString();
                        List<SalesBean> salesBeans = new Gson().fromJson(json, new TypeToken<List<SalesBean>>() {
                        }.getType());
                        setBannerSource(mSalesBeans = salesBeans);
                    } catch (JSONException e) {
                        getLog().error("e:" + e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                getLog().info("getSales:e:" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    private void setBannerSource(List<SalesBean> source) {
        if (source == null || source.size() == 0) {
            speak(R.string.no_promotion, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    finish();
                }
            });
            return;
        }
        ArrayList<String> images = new ArrayList<>();
        for (SalesBean salesBean : source) {
            images.add(salesBean.getActivityImg());
        }
        convenient_banner.setPages(LoopImageHolderView::new, images)
                .setPointViewVisible(true)//设置指示器是否可见
                .startTurning(2000)//设置自动切换（同时设置了切换时间间隔）
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);//设置指示器的方向（左、中、右）

        prattle(getString(R.string.hello) + "," + getString(R.string.look_more_discount));
    }
}
