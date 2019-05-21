package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2018/7/11.
 */
public class SettingsNaviActivity extends BaseModuleActivity {


    @BindView(R.id.cb_navi_auto_exit)
    CheckBox cb_navi_auto_exit;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_navi : R.layout.activity_settings_navi;
    }

    @Override
    public void init() {
        super.init();
        boolean isChecked = SharedPreUtil.getBoolean(SharedKey.NAVI_AUTO_EXIT,SharedKey.NAVI_AUTO_EXIT,cb_navi_auto_exit.isChecked());
        cb_navi_auto_exit.setChecked(isChecked);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @OnClick(R.id.bt_ok)
    public void ok(){
        SharedPreUtil.putBoolean(SharedKey.NAVI_AUTO_EXIT,SharedKey.NAVI_AUTO_EXIT,cb_navi_auto_exit.isChecked());
        Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
        speak(R.string.save_success);
    }

}
