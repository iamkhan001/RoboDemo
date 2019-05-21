package com.csjbot.blackgaga.feature.entertainment.xianyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.dance.DanceActivity;
import com.csjbot.blackgaga.feature.music.MusicActivity;
import com.csjbot.blackgaga.feature.read.ReadActivity;
import com.csjbot.blackgaga.feature.story.StoryActivity;

import butterknife.OnClick;

/**
 * Created by jingwc on 2018/3/5.
 */

public class XianYangEntertainmentActivity extends BaseModuleActivity {

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_entertainment_xianyang;
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
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.VISIBLE);

    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return getString(R.string.airport_entertainment_speak_text);
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "";
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return "";
    }

    @OnClick(R.id.rv_storytelling)
    public void storytelling() {
        jumpActivity(StoryActivity.class);
    }

    @OnClick(R.id.rv_dance)
    public void dance() {
        jumpActivity(DanceActivity.class);
    }

    @OnClick(R.id.rv_music)
    public void music() {
        jumpActivity(MusicActivity.class);
    }

    @OnClick(R.id.rv_read)
    public void read(){
        jumpActivity(ReadActivity.class);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if(text.contains(getString(R.string.dance))){
            dance();
        }else if(text.contains(getString(R.string.sing))){
            music();
        }else if(text.contains(getString(R.string.tellstory))){
            storytelling();
        }else if(text.contains(getString(R.string.read_a_read))){
            read();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }
}
