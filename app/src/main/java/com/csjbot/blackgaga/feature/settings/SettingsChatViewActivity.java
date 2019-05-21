package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
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
 * Created by 荆为成 on 2018/8/7.
 */

public class SettingsChatViewActivity extends BaseModuleActivity {


    @BindView(R.id.chatview_switch)
    CheckBox chatview_switch;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);

        chatview_switch.setChecked(SharedPreUtil.getBoolean(SharedKey.CHAT_VIEW,SharedKey.CHAT_VIEW_IS_OPEN,true));
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_chat_view : R.layout.activity_settings_chat_view;

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


    @OnClick(R.id.bt_save)
    public void bt_save(){
        boolean isChecked = chatview_switch.isChecked();
        SharedPreUtil.putBoolean(SharedKey.CHAT_VIEW,SharedKey.CHAT_VIEW_IS_OPEN,isChecked);
        Constants.isOpenChatView = isChecked;
        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

    }

}
