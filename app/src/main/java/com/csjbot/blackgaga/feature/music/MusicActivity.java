package com.csjbot.blackgaga.feature.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.bean.MusicBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.widget.MusicView;
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

/**
 * Created by jingwc on 2017/10/16.
 */
@Route(path = BRouterPath.MUSIC)
public class MusicActivity extends BaseModuleActivity implements MusicContract.View {

    MusicContract.Presenter mPresenter;

    List<MusicBean> mMusics = new ArrayList<>();

    @BindView(R.id.music_view)
    MusicView music_view;

    int musicIndex;

    int totalCount;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_music,R.layout.activity_vertical_music);
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

        mPresenter = new MusicPresenter();
        mPresenter.initView(this);

        getTitleView().setBackVisibility(View.VISIBLE);

        new Thread(()->{
            // 判断是否携带音乐数据而来
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                String data = bundle.getString("data");
                if(!TextUtils.isEmpty(data)){
                    mMusics = jsonToMusic(data);
                    if(mMusics != null) {
                        totalCount = mMusics.size();
                        runOnUiThread(()->{
                            music_view.pauseMusic();
                            startPlay(musicIndex);
                        });

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
                ServerFactory.getSpeechInstance().getResult("唱首歌");
            }
        }).start();


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

        // 音乐页面控制播放逻辑
        if(text.contains(getString(R.string.next_music))
                || text.contains("下一首")
                || text.contains("换一首")
                || text.contains("换一个")){
            if(musicIndex < (totalCount - 1)){
                music_view.pauseMusic();
                startPlay(++musicIndex);
            }
        }else if(text.contains(getString(R.string.previous_music))
                || text.contains("上一首")
                || text.contains("上一个")){
            if((musicIndex - 1) >= 0){
                music_view.pauseMusic();
                startPlay(--musicIndex);
            }
        }else if(text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))
                || text.contains("别唱了")){
            music_view.pauseMusic();
        }else if(text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))){
            music_view.restartMusic();
        }
        return true;
    }

    @Override
    protected void onShowMessage(String data) {
        super.onShowMessage(data);
        CsjlogProxy.getInstance().info("触发音乐ShowMessge方法");
        new Thread(()->{
            musicIndex = 0;
            mMusics = jsonToMusic(data);
            if(mMusics != null) {
                totalCount = mMusics.size();
                runOnUiThread(()->{
                    music_view.pauseMusic();
                    startPlay(musicIndex);
                });

            }
        }).start();


    }

    private List<MusicBean> jsonToMusic(String data){
        String songs = "";
        try {
            songs = new JSONObject(data).getJSONArray("song_list").toString();
        } catch (JSONException e) {
            speak(R.string.no_music);
        }
        return new Gson().fromJson(songs,new TypeToken<List<MusicBean>>(){}.getType());
    }

    public void startPlay(int index) {
        if(mMusics != null && mMusics.size() > 0){
            MusicBean musicBean = mMusics.get(index);
            if(musicBean == null){
                return;
            }
            if(!TextUtils.isEmpty(musicBean.getAlbumIcon())){
                music_view.setBg(musicBean.getAlbumIcon());
            }
            speak(getString(R.string.playing_music_name) + musicBean.getSongName() + "", new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if(!TextUtils.isEmpty(musicBean.getMusicUrl())) {
                        music_view.playMusic(musicBean.getMusicUrl(), () -> MusicActivity.this.finish());
                    }
                }
            });
            // 日语tts目前没有播放完成检测,所以播报歌曲名称之后,延时去播放音乐
            if(Constants.Language.isJapanese()){
                new Handler().postDelayed(()->{
                    if(!TextUtils.isEmpty(musicBean.getMusicUrl())) {
                        music_view.playMusic(musicBean.getMusicUrl(), () -> MusicActivity.this.finish());
                    }
                },3000);
            }


        }else{
            speak(getString(R.string.no_music));
        }
    }

    public void release() {
        unregisterReceiver(wakeupReceiver);
        music_view.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        music_view.pauseMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }
    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            music_view.pauseMusic();
        }
    };

}
