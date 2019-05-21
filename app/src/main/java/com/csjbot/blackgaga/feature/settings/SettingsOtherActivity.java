package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 荆为成 on 2018/8/30.
 */

public class SettingsOtherActivity extends BaseModuleActivity {

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_other : R.layout.activity_settings_other;

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
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @OnClick(R.id.bt_chatview)
    public void bt_chatview(){
        jumpActivity(SettingsChatViewActivity.class);
    }

    @OnClick(R.id.bt_face_sync)
    public void bt_face_sync(){
        jumpActivity(SettingsFaceSyncActivity.class);
    }

}
