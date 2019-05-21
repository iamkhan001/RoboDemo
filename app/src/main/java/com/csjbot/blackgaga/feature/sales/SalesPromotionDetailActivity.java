package com.csjbot.blackgaga.feature.sales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.SalesBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 促销活动详情
 * Created by jingwc on 2018/3/29.
 */

public class SalesPromotionDetailActivity extends BaseModuleActivity {

    @BindView(R.id.iv_detail)
    ImageView iv_detail;

    @BindView(R.id.iv_location)
    ImageView iv_location;

    SalesBean mSales;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sales_promotion_detail;
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
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);
        readData();
        showImage();
    }

    private void readData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mSales = (SalesBean) bundle.getSerializable("sales");
        }
    }

    private void showImage(){
        if(mSales != null){
            Glide.with(this).load(mSales.getDetailImg()).into(iv_detail);
            Glide.with(this).load(mSales.getLocationImg()).into(iv_location);
        }
    }

    @OnClick(R.id.iv_detail)
    public void iv_detail(View view){
        this.finish();
    }

    @OnClick(R.id.iv_location)
    public void iv_location(View view){
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
