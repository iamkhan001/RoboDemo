package com.csjbot.blackgaga.feature.content2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.pactivity.introduce_list.ProductListActivity;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.util.CSJToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by  Wql , 2018/2/9 16:43
 */
public class ServiceIntroduceActivity extends BaseModuleActivity {
    @BindView(R.id.iv_introduction)
    ImageView mIvIntroduction;
    @BindView(R.id.iv_exhibition)
    ImageView mIvExhibition;
    @BindView(R.id.iv_destine)
    ImageView mIvDestine;
    @BindView(R.id.iv_exhibition_news)
    ImageView mIvExhibitionNews;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_service_introduce;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @OnClick({R.id.iv_introduction, R.id.iv_exhibition, R.id.iv_destine, R.id.iv_exhibition_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_introduction:
                BRouter.jumpActivityByContent("展馆介绍");
                break;
            case R.id.iv_exhibition:
                jumpActivity(ProductListActivity.class);
                break;
            case R.id.iv_destine:
                   CSJToast.showToast(this,"场馆预定");
                break;
            case R.id.iv_exhibition_news:
                BRouter.jumpActivityByContent("展会新闻");
                break;
        }
    }
}
