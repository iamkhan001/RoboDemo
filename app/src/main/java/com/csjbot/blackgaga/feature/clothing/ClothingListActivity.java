package com.csjbot.blackgaga.feature.clothing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.feature.clothing.mvp.ClothingListContract;
import com.csjbot.blackgaga.feature.clothing.mvp.ClothingListPresenter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterPath;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服装列表界面
 *
 * @author ShenBen
 * @date 2018/11/12 10:59
 * @email 714081644@qq.com
 */
@Route(path = BRouterPath.PRODUCT_CLOTHING_LIST)
public class ClothingListActivity extends BaseModuleActivity implements ClothingListContract.View {
    public static final String SELECT_TYPE = "SELECT_TYPE";

    @BindView(R.id.tv_select_clothing)
    TextView tvSelectClothing;
    @BindView(R.id.rv_clothing)
    RecyclerView rvClothing;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;

    @OnClick({R.id.tv_select_clothing, R.id.iv_left, R.id.iv_right})
    public void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_clothing:
                mPresenter.showSelectPopup();
                break;
            case R.id.iv_left:
                mPresenter.previousPage();
                break;
            case R.id.iv_right:
                mPresenter.nextPage();
                break;
        }
    }

    private ClothingListContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_clothing_list, R.layout.vertical_activity_clothing_list);
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
        new ClothingListPresenter(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            String selectType = intent.getStringExtra(SELECT_TYPE);
            mPresenter.init();
            mPresenter.setGoodStyyle(selectType);
            mPresenter.loadData("", 0, Integer.MAX_VALUE);
        } else {
            isNoData(true);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if (resultCode == RESULT_OK && data != null) {
                mPresenter.setGoodStyyle(data.getStringExtra("GoodsType"));
                mPresenter.loadData("", 0, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public void setPresenter(ClothingListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRvCloth() {
        return rvClothing;
    }

    @Override
    public TextView getSelectCloth() {
        return tvSelectClothing;
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
        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null
                && !Constants.ClothProduct.clothTypeBean.getResult().isEmpty()) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    mPresenter.setGoodStyyle(resultBean.getSecondLevel());
                    mPresenter.loadData("", 0, Integer.MAX_VALUE);
                    return true;
                }
            }
        }
        prattle(answerText);
        return true;
    }

}
