package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
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
 * 评价设置
 */
public class SettingsEvaluateActivity extends BaseModuleActivity {


    @BindView(R.id.switch_evaluate)
    CheckBox switch_evaluate;

    @BindView(R.id.tv_time)
    TextView tv_time;

    private int time;

    private boolean isChecked;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings_evaluate, R.layout.vertical_activity_settings_evaluate);

    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    public void init() {
        super.init();

        isChecked = SharedPreUtil.getBoolean(SharedKey.EVALUATE, SharedKey.EVALUATE_SWITCH, true);
        time = SharedPreUtil.getInt(SharedKey.EVALUATE, SharedKey.EVALUATE_TIME, 4);
        switch_evaluate.setChecked(isChecked);
        tv_time.setText(String.valueOf(time));

        switch_evaluate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsEvaluateActivity.this.isChecked = isChecked;
        });
    }

    @OnClick(R.id.iv_top)
    public void iv_top() {
        if (isChecked) {
            int temp = time;
            if (++temp <= 10) {
                time = temp;
            }
            tv_time.setText(String.valueOf(time));
        }

    }

    @OnClick(R.id.iv_bottom)
    public void iv_bottom() {
        if (isChecked) {
            int temp = time;
            if (--temp >= 2) {
                time = temp;
            }
            tv_time.setText(String.valueOf(time));
        }
    }

    @OnClick(R.id.bt_save)
    public void save() {
        SharedPreUtil.putBoolean(SharedKey.EVALUATE, SharedKey.EVALUATE_SWITCH, isChecked);
        SharedPreUtil.putInt(SharedKey.EVALUATE, SharedKey.EVALUATE_TIME, time);
        speak(getString(R.string.save_success));
        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        return true;
    }


    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
