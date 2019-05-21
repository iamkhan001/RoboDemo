package com.csjbot.blackgaga.feature.navigation.config;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.navigation.map.NaviConfigMapActivity;
import com.csjbot.blackgaga.util.BlackgagaLogger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/9/21.
 */

public class NaviConfigActivity extends BaseModuleActivity
        implements NaviConfigContract.View{

    NaviConfigContract.Presenter mPresenter;

    @BindView(R.id.et_name)
    EditText et_name;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_navi_config;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new NaviConfigPresenter();
        mPresenter.initView(this);
    }

    /**
     * 保存导航点配置
     */
    @OnClick(R.id.bt_save)
    public void saveConfig(){
        BlackgagaLogger.debug("saveConfig");
        if(TextUtils.isEmpty(et_name.getText())){
            return;
        }
        mPresenter.getPosition(et_name.getText().toString());
    }

    /**
     * 进入地图绘制页
     */
    @OnClick(R.id.bt_go_config_map)
    public void goMapConfig(){
        startActivity(new Intent(this, NaviConfigMapActivity.class));
        this.finish();
    }
}
