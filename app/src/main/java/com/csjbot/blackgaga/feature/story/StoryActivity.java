package com.csjbot.blackgaga.feature.story;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/16.
 */

public class StoryActivity extends BaseModuleActivity implements StoryContract.View {

    StoryContract.Presenter mPresenter;

    ISpeak storySpeak;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_story,R.layout.activity_vertical_story);
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
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }



    @Override
    public void init() {
        super.init();

        RobotManager.getInstance().addListener(new OnSpeechGetResultListener() {
            @Override
            public void response(String json) {
                try {
                    String dataJson = new JSONObject(json).getJSONObject("result").getJSONObject("data").toString();
                    runOnUiThread(()->{onShowMessage(dataJson);});
                } catch (JSONException e) {
                    CsjlogProxy.getInstance().error(e.toString());
                }
            }
        });

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        storySpeak = ServerFactory.getSpeakInstance();


        mPresenter = new StoryPresenter();
        mPresenter.initView(this);

        getTitleView().setBackVisibility(View.VISIBLE);

        // 判断是否携带音乐数据而来
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String data = bundle.getString("data");
            if(!TextUtils.isEmpty(data)){
                showData(jsonToStory(data));
            }
        }else{
            ServerFactory.getSpeechInstance().getResult("讲个故事");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    protected boolean isInterruptSpeech() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wakeupReceiver);
    }

    public void showData(String[] contents){
        if(contents == null || contents.length == 0){
            return;
        }
        String answer = contents[0];
        String title = contents[1];
        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
        if(!TextUtils.isEmpty(answer)){
            tv_content.setText(answer);
            storySpeak.stopSpeaking();
            storySpeak.startSpeaking(answer, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    StoryActivity.this.finish();
                }
            });
        }
    }

    private void pauseStory(){
        storySpeak.pauseSpeaking();
    }

    private void restartStroy(){
        storySpeak.resumeSpeaking();
    }


    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }

        // 故事页面控制播放逻辑
        if(text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))){
            pauseStory();
        }else if(text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))){
            restartStroy();
        }
        if(text.contains("换一个")
                || text.contains("换个故事")){
            ServerFactory.getSpeechInstance().getResult("讲个故事");
        }
        return true;
    }

    @Override
    protected void onShowMessage(String data) {
        super.onShowMessage(data);
        showData(jsonToStory(data));
    }

    private String[] jsonToStory(String data){
        String story = "";
        String title = "";
        try {
            story = new JSONObject(data).getJSONObject("data").getString("answer");
            title = new JSONObject(data).getJSONObject("semantic").getJSONObject("slots").getJSONObject("NAME").getString("originalValue");
            if(!TextUtils.isEmpty(title)){
                if(title.equals("RANDOM_STORY")){
                    title = null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[]{story,title};
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseStory();
    }

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseStory();
        }
    };
}
