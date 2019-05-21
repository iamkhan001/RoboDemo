package com.csjbot.blackgaga.feature.read;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.tts.IflySpeechImpl;
import com.csjbot.coshandler.tts.SpeechFactory;
import com.iflytek.cloud.SpeechError;

import butterknife.BindView;

/**
 * Created by jingwc on 2018/3/5.
 */

public class ReadActivity extends BaseModuleActivity {

    IflySpeechImpl iflySpeech;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_desc)
    TextView tv_desc;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
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
        getTitleView().setBackVisibility(View.VISIBLE);

        iflySpeech = new IflySpeechImpl(this, i -> {

        },"vixying");

        tv_title.setText("读得懂 读的溜 陕西话没麻达");
        tv_content.setText("清明时节雨唰唰，\n跑皮一天累日塌，\n借问馆子在啊答，\n碎松一指在雾答。");
        tv_desc.setText("（其中的“日塌”=“坏”，“啊答”=“哪里”，“岁松”=“小娃”，“雾答”=“哪里”）");

        iflySpeech.startSpeaking("读得懂，读的溜，陕西话模吗哒。", new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                iflySpeech.startSpeaking(tv_content.getText().toString(),null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(iflySpeech != null) {
            iflySpeech.pauseSpeaking();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(iflySpeech != null) {
            iflySpeech.stopSpeaking();
        }
        // 切回正常语音
        RobotManager.getInstance().robot.initSpeak(this, SpeechFactory.SpeechType.IFLY);
    }
}
