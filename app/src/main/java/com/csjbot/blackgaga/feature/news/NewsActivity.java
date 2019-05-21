package com.csjbot.blackgaga.feature.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.NewsBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechGetResultListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsActivity extends BaseModuleActivity {


    List<NewsBean> mNewsList = new ArrayList<>();


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.iv_picture)
    ImageView iv_picture;

    int newsIndex;

    int totalCount;

    ISpeak newsSpeak;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news;
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

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        newsSpeak = ServerFactory.getSpeakInstance();

        getTitleView().setBackVisibility(View.VISIBLE);

        new Thread(()->{
            // 判断是否携带音乐数据而来
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                String data = bundle.getString("data");
                if(!TextUtils.isEmpty(data)){
                    mNewsList = jsonToNews(data);
                    if(mNewsList != null){
                        totalCount = mNewsList.size();
                        runOnUiThread(()->showData(newsIndex));
                    }

                }
            }else{
                RobotManager.getInstance().addListener(new OnSpeechGetResultListener() {
                    @Override
                    public void response(String json) {
                        try {
                            String dataJson = new JSONObject(json).getJSONObject("result").getJSONObject("data").getJSONObject("data").toString();
                            onShowMessage(dataJson);
                        } catch (JSONException e) {
                            CsjlogProxy.getInstance().error(e.toString());
                        }
                    }
                });
                ServerFactory.getSpeechInstance().getResult("新闻");
            }
        }).start();


    }

    public void showData(int index){
        if(mNewsList != null && mNewsList.size() > 0){
            NewsBean newsBean = mNewsList.get(index);
            if(newsBean == null){
                return;
            }
            if(!TextUtils.isEmpty(newsBean.getTitle())) {
                tv_title.setText(newsBean.getTitle());
            }
            if(!TextUtils.isEmpty(newsBean.getContent())) {
                tv_content.setText(newsBean.getContent());
                newsSpeak.stopSpeaking();
                newsSpeak.startSpeaking(getString(R.string.current_news) + newsBean.getTitle(), new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        newsSpeak.startSpeaking(getString(R.string.news_content) + newsBean.getContent(), new OnSpeakListener() {
                            @Override
                            public void onSpeakBegin() {

                            }

                            @Override
                            public void onCompleted(SpeechError speechError) {
                                NewsActivity.this.finish();
                            }
                        });
                    }
                });
            }
            if(!TextUtils.isEmpty(newsBean.getPicture())){
                iv_picture.setVisibility(View.VISIBLE);
                Glide.with(this).load(newsBean.getPicture()).into(iv_picture);
            }else{
                iv_picture.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        // 新闻页面控制播放逻辑
        if(text.contains("换一篇")
                || text.contains("换一个")
                || text.contains("下一个")
                || text.contains("下一篇")){
            if(newsIndex < (totalCount - 1)){
                showData(++newsIndex);
            }
        }else if(text.contains("上一个")
                || text.contains("上一篇")){
            if((newsIndex - 1) >= 0){
                showData(--newsIndex);
            }
        }else if(text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))){
            pauseNews();
        }else if(text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))){
            restartNews();
        }
        return true;
    }

    private void pauseNews(){
        newsSpeak.pauseSpeaking();
    }

    private void restartNews(){
        newsSpeak.resumeSpeaking();
    }

    @Override
    protected void onShowMessage(String data) {
        super.onShowMessage(data);
        new Thread(()->{
            newsIndex = 0;
            mNewsList = jsonToNews(data);
            if(mNewsList != null){
                totalCount = mNewsList.size();
                runOnUiThread(()->showData(newsIndex));
            }
        }).start();

    }

    private List<NewsBean> jsonToNews(String data){
        String songs = "";
        try {
            songs = new JSONObject(data).getJSONArray("info").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(songs,new TypeToken<List<NewsBean>>(){}.getType());
    }

    @Override
    protected void onPause() {
        super.onPause();

        pauseNews();
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

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseNews();
        }
    };
}
