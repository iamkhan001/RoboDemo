package com.csjbot.blackgaga.feature.take_number;

import android.content.Intent;
import android.os.Bundle;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;

import butterknife.OnClick;

/**
 * Created by jingwc on 2017/9/26.
 */

public class TakeNumberActivity extends BaseModuleActivity implements TakeNumberContract.View {

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_take_number;
    }

    TakeNumberContract.Presenter mPresenter;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new TakeNumberPresenter();
        mPresenter.initView(this);
    }

    @OnClick(R.id.bt_take_number)
    public void takeNumber(){
        mPresenter.takeNumber();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
