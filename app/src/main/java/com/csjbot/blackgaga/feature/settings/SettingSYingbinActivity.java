package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.util.CSJToast;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import butterknife.BindView;

public class SettingSYingbinActivity extends BaseModuleActivity {
    @BindView(R.id.charging_pile_switch)
    CheckBox charging_pile_switch;

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_yingbin_setting : R.layout.activity_yingbin_setting;

    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        charging_pile_switch.setChecked(SharedPreUtil.getBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, false));
        charging_pile_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activeYingbin(isChecked);
        });
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void activeYingbin(boolean ye) {
        if (SharedPreUtil.putBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, ye)) {
            CSJToast.showToast(this, getString(R.string.save_success), 1000);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
