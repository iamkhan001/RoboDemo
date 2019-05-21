package com.csjbot.blackgaga.feature.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.NaviGuideCommentAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.navigation.contract.NaviCommentContract;
import com.csjbot.blackgaga.feature.navigation.contract.NaviCommentPresenter;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.iflytek.cloud.SpeechError;

import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/12/12.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.NAVI_GUIDE_COMMENT)
public class NaviGuideCommentActivity extends BaseModuleActivity implements NaviCommentContract.View, NaviAction.NaviActionListener {
    NaviCommentContract.Presenter presenter = new NaviCommentPresenter();
    private NaviGuideCommentAI mAI;
    private boolean isFromNaviAI;

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_service_evaluation,R.layout.vertical_activity_service_evaluation);
    }

    int time = 0;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        presenter.initView(this);
        mAI = NaviGuideCommentAI.newInstance();
        mAI.initAI(this);
        isFromNaviAI = getIntent().getBooleanExtra("isFromNaviAI", false);

        boolean isChecked = SharedPreUtil.getBoolean(SharedKey.EVALUATE,SharedKey.EVALUATE_SWITCH,true);
        time = SharedPreUtil.getInt(SharedKey.EVALUATE,SharedKey.EVALUATE_TIME,4);
        if(!isChecked){
            onBackPressed();
            return;
        }

        NaviAction.getInstance().registerActionListener(this);

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        NaviGuideCommentAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
        } else {
            prattle(answerText);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
        getTitleView().setBackVisibility(View.VISIBLE);
        getChatView().clearChatMsg();
//        getChatView().addChatMsg(0, getString(R.string.service_evaluation));
        speak(R.string.commnet_hint, new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                h.sendEmptyMessageDelayed(1, (time * 1000));
            }
        },true);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @OnClick(R.id.buttonPanel1)
    public void wellClick() {
        h.removeMessages(1);
        well();
    }

    @OnClick(R.id.buttonPanel2)
    public void veryWellClick() {
        h.removeMessages(1);
        veryWell();
    }

    @OnClick(R.id.buttonPanel3)
    public void badClick() {
        h.removeMessages(1);
        bad();
    }

    @Override
    public void well() {
//        Toast.makeText(context, "满意", Toast.LENGTH_SHORT).show();
        presenter.submitResult(GOOD);
    }

    @Override
    public void veryWell() {
//        Toast.makeText(context, "非常满意", Toast.LENGTH_SHORT).show();
        presenter.submitResult(VERY_GOOD);
    }

    @Override
    public void bad() {
//        Toast.makeText(context, "不满意", Toast.LENGTH_SHORT).show();
        presenter.submitResult(BAD);
    }

    @Override
    public void success() {
        Toast.makeText(context, R.string.comment_success, Toast.LENGTH_SHORT).show();
        speak(R.string.thankyou, new OnSpeakListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (!isFromNaviAI) {
                    NaviAction.getInstance().back();
                } else {
                    NaviGuideCommentActivity.this.finish();
                }
            }
        }, true);
    }

    @Override
    public void failed() {
        Toast.makeText(context, R.string.comment_failed, Toast.LENGTH_SHORT).show();
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onBackPressed();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacksAndMessages(null);
        NaviAction.getInstance().unregisterActionListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!isFromNaviAI) {
            NaviAction.getInstance().back();
            super.onBackPressed();
        } else {
            setResult(RESULT_OK);
            finish();
        }
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
    public void arrived(NaviBean naviBean) {

    }

    @Override
    public void error(int code) {
        if (code == NaviAction.NO_YINBIN_POINT) {
            Toast.makeText(context, R.string.no_yinbin, Toast.LENGTH_SHORT).show();
            speak(R.string.no_yinbin, true);
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public void sendSuccess() {
        setResult(RESULT_OK);
        this.finish();
    }
}
