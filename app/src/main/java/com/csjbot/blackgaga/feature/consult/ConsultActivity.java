package com.csjbot.blackgaga.feature.consult;

import android.content.Intent;
import android.os.Bundle;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultActivity extends BaseModuleActivity implements ConsultContract.View{


    ConsultContract.Presenter mPresenter;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_consult;
    }

    @Override
    public void init() {
        mPresenter = new ConsultPresenter();
        mPresenter.initView(this);
    }

    @Override
    public void showConsult() {
        startActivity(new Intent(getApplicationContext(),ConsultActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
