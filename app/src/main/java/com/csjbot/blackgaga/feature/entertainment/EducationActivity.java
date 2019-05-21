package com.csjbot.blackgaga.feature.entertainment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.EntertainmentAI2;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.dance.DanceActivity;
import com.csjbot.blackgaga.feature.music.MusicActivity;
import com.csjbot.blackgaga.feature.story.StoryActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.SharedKey;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/16.
 */

//@Route(path = BRouterPath.ENTERTAINMENT)
public class EducationActivity extends BaseModuleActivity implements EntertainmentContract.View {

    EntertainmentContract.Presenter mPresenter;

    EntertainmentAI2 mAI;
    @BindView(R.id.iv_music)
    ImageView mIvMusic;
    @BindView(R.id.iv_storytelling)
    ImageView mIvStorytelling;
    @BindView(R.id.iv_dance)
    ImageView mIvDance;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_education_entertainment;
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
//        // TODO: 2018/2/8 更改首页图标
//        /**
//         *  "product_menu_cn";
//         "product_menu_en";
//         "product_menu_jp";
//         */
//        String lan = SharedKey.MENU_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();

        mPresenter = new EntertainmentPresenter();
        mPresenter.initView(this);

        getTitleView().setBackVisibility(View.VISIBLE);


        mAI = EntertainmentAI2.newInstance();
        mAI.initAI(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*根据语音跟换图片*/
        String lan = SharedKey.MENU_LAN.getLanguageStr(Constants.Language.getLanguageStr());
        if (lan.equals("product_menu_jp")) {
            mIvMusic.setImageDrawable(getDrawable(R.drawable.singing_jp));
            mIvStorytelling.setImageDrawable(getDrawable(R.drawable.tellstory_jp));
            mIvDance.setImageDrawable(getDrawable(R.drawable.dancing_jp));


        } else if (lan.equals("product_menu_en")) {
            mIvMusic.setImageDrawable(getDrawable(R.drawable.singing_en));
            mIvStorytelling.setImageDrawable(getDrawable(R.drawable.tellstory_en));
            mIvDance.setImageDrawable(getDrawable(R.drawable.dancing_en));
        } else {
            mIvMusic.setImageDrawable(getDrawable(R.drawable.singing_cn));
            mIvStorytelling.setImageDrawable(getDrawable(R.drawable.tellstory_cn));
            mIvDance.setImageDrawable(getDrawable(R.drawable.dancing_cn));

        }

}

    @Override
    protected CharSequence initChineseSpeakText() {
        //        int color = Color.parseColor("#0099ff");
        //        SpannableStringBuilder ssb = new RichTextUtil().append("您好,我可以提供一下娱乐模式,请说出您要的娱乐模式\t")
        //                .append("\t1.讲故事", color, v -> storytelling())
        //                .append("\t2.唱歌", color, v -> music())
        //                .append("\t3.跳舞", color, v -> dance())
        //                .finish();
        return "您好,我可以提供一下娱乐模式,请说出您要的娱乐模式:\t\t1.讲故事\t2.跳舞\t3.唱歌";
    }

    public void music() {
        jumpActivity(MusicActivity.class);
    }

    public void dance() {
        jumpActivity(DanceActivity.class);
    }

    public void storytelling() {
        jumpActivity(StoryActivity.class);
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "I can sing, dance and tell stories.";
    }


    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        EntertainmentAI2.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    @OnClick({R.id.iv_storytelling, R.id.iv_dance, R.id.iv_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_storytelling:
                storytelling();
                break;
            case R.id.iv_dance:
                dance();
                break;
            case R.id.iv_music:
                music();
                break;
        }
    }
}
