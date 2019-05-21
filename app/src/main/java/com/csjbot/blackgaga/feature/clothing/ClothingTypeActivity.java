package com.csjbot.blackgaga.feature.clothing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.feature.clothing.mvp.ClothingTypeContract;
import com.csjbot.blackgaga.feature.clothing.mvp.ClothingTypePresenter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterPath;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 服装种类界面
 *
 * @author ShenBen
 * @date 2018/11/13 9:48
 * @email 714081644@qq.com
 */
@Route(path = BRouterPath.PRODUCT_CLOTHING_TYPE)
public class ClothingTypeActivity extends BaseModuleActivity implements ClothingTypeContract.View {

    @BindView(R.id.rv_clothing)
    RecyclerView rvClothing;

    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                mPresenter.previousPage();
                break;
            case R.id.iv_right:
                mPresenter.nextPage();
                break;
        }
    }

    private ClothingTypeContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.vertical_activity_clothing_type;
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
        new ClothingTypePresenter(this);
        mPresenter.initData();
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }


    @Override
    public void setPresenter(ClothingTypeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRvCloth() {
        return rvClothing;
    }

    @Override
    public void speakMessage(String message) {
        prattle(message);
    }

    @Override
    public void isNoData(boolean isNodata) {
        if (isNodata) {
            llData.setVisibility(View.GONE);
            flNoData.setVisibility(View.VISIBLE);
        } else {
            flNoData.setVisibility(View.GONE);
            llData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }

        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_LIST,
                            ClothingListActivity.SELECT_TYPE, resultBean.getSecondLevel());
                    return true;
                }
            }
        }
        prattle(answerText);
        return true;
    }
}
