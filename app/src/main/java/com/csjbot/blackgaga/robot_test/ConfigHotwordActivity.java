package com.csjbot.blackgaga.robot_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.coshandler.listener.OnHotWordsListener;
import com.csjbot.coshandler.log.CsjlogProxy;

import java.util.Arrays;

import butterknife.BindView;

public class ConfigHotwordActivity extends BaseModuleActivity {

    @BindView(R.id.edittextHotWords)
    EditText edittextHotWords;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        RobotManager.getInstance().addListener((OnHotWordsListener) hotWords -> runOnUiThread(() -> {
            StringBuilder showWords = new StringBuilder();
            for (String word : hotWords) {
                showWords.append(word).append(",");
            }

            edittextHotWords.setText(showWords.toString());
            CsjlogProxy.getInstance().debug(showWords.toString());
        }));
        RobotManager.getInstance().robot.getHotWords();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_config_hotwords;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    public void readAllLocation(View view) {

    }

    public void clearAllHotWords(View view) {
    }

    public void saveAllHotWords(View view) {
        String hotwordsFromUI = edittextHotWords.getText().toString();
        String hotHords[] = hotwordsFromUI.split(",");
        RobotManager.getInstance().robot.setHotWords(Arrays.asList(hotHords));
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
