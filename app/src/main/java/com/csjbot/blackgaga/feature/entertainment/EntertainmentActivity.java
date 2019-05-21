package com.csjbot.blackgaga.feature.entertainment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.ai.EntertainmentAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.dance.DanceActivity;
import com.csjbot.blackgaga.feature.music.MusicActivity;
import com.csjbot.blackgaga.feature.story.StoryActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouterPath;

import java.util.Random;

import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/16.
 */

@Route(path = BRouterPath.ENTERTAINMENT)
public class EntertainmentActivity extends BaseModuleActivity implements EntertainmentContract.View {

    EntertainmentContract.Presenter mPresenter;

    EntertainmentAI mAI;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_entertainment,R.layout.activity_vertical_entertainment);
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

        mPresenter = new EntertainmentPresenter();
        mPresenter.initView(this);

        getTitleView().setBackVisibility(View.VISIBLE);


        mAI = EntertainmentAI.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected CharSequence initChineseSpeakText() {
//        int color = Color.parseColor("#0099ff");
//        SpannableStringBuilder ssb = new RichTextUtil().append("您好,我可以提供一下娱乐模式,请说出您要的娱乐模式\t")
//                .append("\t1.讲故事", color, v -> storytelling())
//                .append("\t2.唱歌", color, v -> music())
//                .append("\t3.跳舞", color, v -> dance())
//                .finish();
        if(Constants.Scene.CurrentScene.equals(Constants.Scene.JiuDianScene)){
            String[] texts = new String[]{"人家一个人好寂寞呢，您要不要跟我一起来玩呢？"
                    ,"我想到好多好玩的东西，您要不要来试试呢？"
                    ,"下面是嗨爆时间,让我们一起摇起来！"};
            int index = new Random().nextInt(3);
            if(index >= texts.length){
                index = 0;
            }
            return texts[index];
        }
        return getString(R.string.entertainment_init_speak);
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return getString(R.string.entertainment_init_speak);
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return getString(R.string.entertainment_init_speak);
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

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        EntertainmentAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }
}
