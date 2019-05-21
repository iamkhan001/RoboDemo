package com.csjbot.blackgaga.robot_test;


import android.content.Intent;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.widget.SlideProgressView;
import com.csjbot.coshandler.listener.OnMicroVolumeListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/11/21.
 */

public class TestMicroVolumeActivity extends BaseModuleActivity {

    @BindView(R.id.slide_progress)
    SlideProgressView slide_progress;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_micro_test;
    }

    @Override
    public void init() {
        super.init();
        RobotManager.getInstance().addListener(new OnMicroVolumeListener() {
            @Override
            public void response(int volume) {
                getLog().info("volume:"+volume);
                runOnUiThread(()->slide_progress.setValue(volume));

            }
        });
        ServerFactory.getConfigInstance().getMicroVolume();
    }

    @OnClick(R.id.bt_save)
    public void save(){
        int value = slide_progress.getValue();
        ServerFactory.getConfigInstance().setMicroVolume(value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
