package com.csjbot.blackgaga.feature.dance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.body_action.IAction;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.widget.MusicView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jingwc on 2017/10/12.
 */

@Route(path = BRouterPath.DANCE)
public class DanceActivity extends BaseModuleActivity implements DanceContract.View {

    @BindView(R.id.music_view)
    MusicView music_view;

    List<String> songs = new ArrayList<>();

    IAction action;

    boolean isPause;

    boolean isUnavailable;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_dance,R.layout.activity_vertical_dance);
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

        songs.add("http://cdn.stormorai.com/qq/download/003uVtt403QZ0X.m4a");
        songs.add("http://cdn.stormorai.com/resource/music_bk/412/138785412.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/mID/003txpo62fRmID.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/DXl/000Q3vqG3GaDXl.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/37094168.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/519/135163519.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/352/135905352.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/others/bTl/0031MMo90FDbTl.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/993/31313993.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/740/132350740.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/31067128.mp3");
        songs.add("http://cdn.stormorai.com/qq/download/102950619.m4a");
        songs.add("http://cdn.stormorai.com/resource/music_bk/858/31530858.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/643/139191643.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/378/30100378.mp3");
        songs.add("http://cdn.stormorai.com/resource/music_bk/35525334.mp3");

        getTitleView().setBackVisibility(View.VISIBLE);

        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        IntentFilter aFilter = new IntentFilter("com.example.BROADCAST");
        registerReceiver(batteryReceiver, aFilter);

        action = ServerFactory.getActionInstance();

        startDance();

    }

    private void startDance(){
        int random = (int) (Math.random()*songs.size())+1;
        if(random < 0 || random > (songs.size()-1)){
            random = 0;
        }
        music_view.playMusic(songs.get(random), () -> {
            DanceActivity.this.finish();
        });
        action.startDance();
    }

    public void pauseDance(){
        music_view.pauseMusic();
        action.stopDance();
    }
    public void restartDance(){
        music_view.restartMusic();
        action.startDance();
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if(super.onSpeechMessage(text,answerText)){
            return false;
        }
        if(text.contains(getString(R.string.pause))
                || text.contains(getString(R.string.stop))
                || text.contains(getString(R.string.do_not_dance))
                || text.contains("不要跳了")){
            pauseDance();
        }else if(text.contains(getString(R.string.resume))
                || text.contains(getString(R.string.start))){
            restartDance();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        pauseDance();
        isPause = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_MUTE));
        if(isPause) {
            restartDance();
        }
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release(){
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(wakeupReceiver);
        music_view.release();
        action.stopDance();
    }


    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseDance();
            DanceActivity.this.finish();
        }
    };

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseDance();
        }
    };

    @Override
    public void networkAvailability() {
        super.networkAvailability();
        if(isUnavailable){
            restartDance();
        }
        isUnavailable = false;
    }

    @Override
    public void networkUnavailable() {
        super.networkUnavailable();
        isUnavailable = true;
        pauseDance();
    }
}
