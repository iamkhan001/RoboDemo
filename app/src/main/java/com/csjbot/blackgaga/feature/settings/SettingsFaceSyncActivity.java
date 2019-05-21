package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.coshandler.listener.OnSyncFaceListener;

import butterknife.OnClick;

/**
 * Created by 荆为成 on 2018/8/30.
 */

public class SettingsFaceSyncActivity extends BaseModuleActivity {


    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);

        RobotManager.getInstance().addListener(new OnSyncFaceListener() {
            @Override
            public void response(int errorCode) {
                if(errorCode == 0){
                    runOnUiThread(() -> {
                        speak(R.string.start_sync_face_data);
                        Toast.makeText(context, R.string.start_sync_face_data, Toast.LENGTH_SHORT).show();
                    });
                }else if(errorCode == 1){
                    runOnUiThread(() -> {
                        speak(R.string.in_sync);
                        Toast.makeText(context, R.string.in_sync, Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void complete(int errorCode) {
                runOnUiThread(() -> {
                    speak(R.string.sync_complete);
                    Toast.makeText(context, R.string.sync_complete, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_face_sync : R.layout.activity_settings_face_sync;

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

    @OnClick(R.id.bt_face_data_sync)
    public void bt_face_data_sync(){
        ServerFactory.getFaceInstance().syncFaceData();
    }


}
