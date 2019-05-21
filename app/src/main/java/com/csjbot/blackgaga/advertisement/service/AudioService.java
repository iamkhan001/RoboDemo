package com.csjbot.blackgaga.advertisement.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.event.AudioEvent;
import com.csjbot.blackgaga.advertisement.factory.ProxyFactory;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.home.AdminHomeActivity;
import com.csjbot.blackgaga.home.CSJBotHomeActivity;
import com.csjbot.blackgaga.home.ChaoShiHomeActivity;
import com.csjbot.blackgaga.home.CheGuanSuoHomeActivity;
import com.csjbot.blackgaga.home.CheZhanHomeActivity;
import com.csjbot.blackgaga.home.FuZhuangHomeActivity;
import com.csjbot.blackgaga.home.HomeActivity;
import com.csjbot.blackgaga.home.JiChangHomeActivity;
import com.csjbot.blackgaga.home.JiaDianHomeActivity;
import com.csjbot.blackgaga.home.JiaJuHomeActivity;
import com.csjbot.blackgaga.home.JiaoYu2HomeActivity;
import com.csjbot.blackgaga.home.JiuDianHomeActivity;
import com.csjbot.blackgaga.home.KeJiGuanHomeActivity;
import com.csjbot.blackgaga.home.LvYouHomeActivity;
import com.csjbot.blackgaga.home.MuseumHomeActivity;
import com.csjbot.blackgaga.home.QiCheHomeActivity;
import com.csjbot.blackgaga.home.QiCheZhanHomeActivity;
import com.csjbot.blackgaga.home.QianTaiHomeActivity;
import com.csjbot.blackgaga.home.ShangShaHomeActivity;
import com.csjbot.blackgaga.home.ShiPinHomeActivity;
import com.csjbot.blackgaga.home.ShouJiHomeActivity;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.blackgaga.home.StateGridHomeActivity;
import com.csjbot.blackgaga.home.XianYangAirportHomeActivity;
import com.csjbot.blackgaga.home.XieDianHomeActivity;
import com.csjbot.blackgaga.home.YanJingHomeActivity;
import com.csjbot.blackgaga.home.YaoDianHomeActivity;
import com.csjbot.blackgaga.home.YiYuanHomeActivity;
import com.csjbot.blackgaga.home.YinHangHomeActivity;
import com.csjbot.blackgaga.home.ZhanGuanHomeActivity;
import com.csjbot.blackgaga.model.http.bean.AdvBean;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * @author Ben
 * @date 2018/3/23
 */

public class AudioService extends Service implements MediaPlayer.OnCompletionListener, OnFaceListener {
    private static final String TAG = "AudioService";

    private HttpProxyCacheServer mServer;
    private WindowManager mWindowManager = null;
    private RelativeLayout mFloatLayout = null;
    private VideoView mVideo = null;
    private MediaPlayer mPlayer = null;
    private ConvenientBanner<String> mBanner = null;
    /**
     * VideoView 是否已经初始化
     */
    private boolean isVideoInit = false;
    /**
     * MediaPlayer 是否已经初始化
     */
    private boolean isMusicInit = false;
    /**
     * 是否是最新数据
     */
    private boolean isLatestData = false;
    /**
     * 视频资源列表
     */
    private List<String> mListVideo;
    /**
     * 音乐资源列表
     */
    private List<String> mListMusic;
    /**
     * 轮播图片资源
     */
    private List<String> mListImage;

    private CountDownTimer mTimer;

    private AdvBean mAdvBean;
    /**
     * 广告是否可以显示
     */
    private boolean isCanShow = true;

    private int mPositionVideo = 0;
    private int mPositionMusic = 0;

    private List<String> mActivityNames;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mListVideo.clear();
                    mListMusic.clear();
                    mListImage.clear();
                    for (int i = 0; i < mAdvBean.getResult().size(); i++) {
                        if (TextUtils.equals("video", mAdvBean.getResult().get(i).getAdvtype())) {
                            mListVideo.add(mAdvBean.getResult().get(i).getAdvurl());
                        } else if (TextUtils.equals("audio", mAdvBean.getResult().get(i).getAdvtype())) {
                            mListMusic.add(mAdvBean.getResult().get(i).getAdvurl());
                        } else if (TextUtils.equals("picture", mAdvBean.getResult().get(i).getAdvtype())) {
                            mListImage.add(mAdvBean.getResult().get(i).getAdvurl());
                        }
                    }
                    break;
                case 2:
                    start();
                    break;
                case 3:
                    initActivityName();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mServer = ProxyFactory.getProxy(getApplicationContext());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        RobotManager.getInstance().addListener(this);
        mListVideo = new ArrayList<>();
        mListMusic = new ArrayList<>();
        mListImage = new ArrayList<>();
        mHandler.sendEmptyMessage(3);
        mTimer = new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mHandler.sendEmptyMessage(2);
            }
        };
        mTimer.start();

        getAdvertisement();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 添加主页activity name
     */
    private void initActivityName() {
        mActivityNames = new ArrayList<>();
        mActivityNames.add(ChaoShiHomeActivity.class.getName());
        mActivityNames.add(CheZhanHomeActivity.class.getName());
        mActivityNames.add(CSJBotHomeActivity.class.getName());
        mActivityNames.add(FuZhuangHomeActivity.class.getName());
        mActivityNames.add(HomeActivity.class.getName());
        mActivityNames.add(JiaDianHomeActivity.class.getName());
        mActivityNames.add(JiaJuHomeActivity.class.getName());
        mActivityNames.add(JiaoYu2HomeActivity.class.getName());
        mActivityNames.add(JiChangHomeActivity.class.getName());
        mActivityNames.add(JiuDianHomeActivity.class.getName());
        mActivityNames.add(KeJiGuanHomeActivity.class.getName());
        mActivityNames.add(LvYouHomeActivity.class.getName());
        mActivityNames.add(QianTaiHomeActivity.class.getName());
        mActivityNames.add(QiCheHomeActivity.class.getName());
        mActivityNames.add(QiCheZhanHomeActivity.class.getName());
        mActivityNames.add(ShangShaHomeActivity.class.getName());
        mActivityNames.add(ShiPinHomeActivity.class.getName());
        mActivityNames.add(ShouJiHomeActivity.class.getName());
        mActivityNames.add(ShuiWuHomeActivity3.class.getName());
        mActivityNames.add(XianYangAirportHomeActivity.class.getName());
        mActivityNames.add(XieDianHomeActivity.class.getName());
        mActivityNames.add(YanJingHomeActivity.class.getName());
        mActivityNames.add(YaoDianHomeActivity.class.getName());
        mActivityNames.add(YinHangHomeActivity.class.getName());
        mActivityNames.add(YiYuanHomeActivity.class.getName());
        mActivityNames.add(ZhanGuanHomeActivity.class.getName());
        mActivityNames.add(StateGridHomeActivity.class.getName());
        mActivityNames.add(AdminHomeActivity.class.getName());
        mActivityNames.add(CheGuanSuoHomeActivity.class.getName());
        mActivityNames.add(MuseumHomeActivity.class.getName());
    }

    /**
     * 获取广告资源
     */
    private void getAdvertisement() {
        String sn = TextUtils.isEmpty(Robot.SN) ? "12345678" : Robot.SN;
        ServerFactory.createAdvertisement().getAdvertisement(sn, Constants.Language.getLanguageStr(), new Observer<AdvBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull AdvBean advBean) {
                if (advBean != null) {
                    mAdvBean = advBean;
                    isLatestData = false;
                    mHandler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void getAudioAction(AudioEvent event) {
        switch (event.getAction()) {
            case START:
                mTimer.cancel();
                start();
                break;
            case STOP:
                destroyAudio();
                break;
            case UPDATE_DATA:
                destroyAudio();
                getAdvertisement();
                break;
            case CAN_NOT_SHOW:
                isCanShow = false;
                destroyAudio();
                mTimer.cancel();
                break;
            case CAN_SHOW:
                isCanShow = true;
                break;
            default:
                break;
        }
    }

    /**
     * 广告开始
     * 优先级：如果有视频资源，则只播放视频资源；如果音乐、轮播图数据均有，则同时播放，否侧单一播放。
     */
    private void start() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        boolean isOnHomeActivity = false;
        for (String activity : mActivityNames) {
            if (TextUtils.equals(runningActivity, activity)) {
                isOnHomeActivity = true;
                break;
            }
        }

        if (!isOnHomeActivity) {
            Log.e(TAG, "当前Activity不是主页");
            return;
        }

        if (!isLatestData) {
            destroyAudio();
        }
        isLatestData = true;

        if (!mListVideo.isEmpty()) {
            videoStart();
            return;
        }

//        if (!mListMusic.isEmpty()) {
//            Log.e(TAG, "背景音乐start: ");
//            musicStart();
//        }

        if (!mListImage.isEmpty()) {
            bannerShow();
        }
    }

    /**
     * 视频播放
     */
    public void videoStart() {
        //如果音乐正在播放或者正在显示图片，先执行destroyAudio()
        if (isMusicInit || mBanner != null) {
            destroyAudio();
        }
        //如果没有显示，先创建
        if (!isVideoInit) {
            createFloatWindow(1);
            return;
        }
        if (mVideo != null && !mVideo.isPlaying()) {
            mVideo.start();
        }
    }

    /**
     * 视频暂停
     */
    public void videoPause() {
        if (mVideo != null && mVideo.isPlaying()) {
            mVideo.pause();
        }
    }

    /**
     * 音乐播放
     */
    public void musicStart() {
        if (isVideoInit) {
            destroyAudio();
        }
        if (!isMusicInit) {
            mPlayer = new MediaPlayer();
            initMusic(mListMusic.get(0));
            return;
        }
        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    /**
     * 音乐暂停
     */
    public void musicPause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    /**
     * 轮播图显示
     */
    public void bannerShow() {
        if (isVideoInit) {
            destroyAudio();
        }
        createFloatWindow(2);
    }

    /**
     * 创建悬浮窗
     */
    private void createFloatWindow(int type) {
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mParams.gravity = Gravity.START;

        mParams.x = 0;
        mParams.y = 0;

        mFloatLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_audio, null);

        mFloatLayout.setOnClickListener(view -> {
            //点击悬浮窗消失
            destroyAudio();
            mTimer.cancel();
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, mParams);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        switch (type) {
            case 1:
                mVideo = mFloatLayout.findViewById(R.id.video_float);
                if (mBanner != null) {
                    mBanner.setVisibility(View.GONE);
                }
                mVideo.setVisibility(View.VISIBLE);
                mVideo.setLayoutParams(layoutParams);
                initVideo(mListVideo.get(0));
                isVideoInit = true;
                break;
            case 2:
                mBanner = mFloatLayout.findViewById(R.id.cb_audio_image);
                if (mVideo != null) {
                    mVideo.setVisibility(View.GONE);
                }
                mBanner.setVisibility(View.VISIBLE);
                mBanner.setLayoutParams(layoutParams);
                initBanner();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化VideoView
     * <p>
     * 开始播放视频
     */
    private void initVideo(String videoUrl) {
        if (mVideo != null) {
            mVideo.setVideoPath(getProxyUrl(videoUrl));
            mVideo.setOnPreparedListener(mp -> mVideo.start());
            mVideo.setOnCompletionListener(this);
        }
    }

    /**
     * 初始化音乐播放器
     */
    private void initMusic(String musicUrl) {
        if (mPlayer != null) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(getProxyUrl(musicUrl));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(mp -> mPlayer.start());
                mPlayer.setOnCompletionListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isMusicInit = true;
    }

    /**
     * 初始化Iamge
     * <p>
     * 显示图片
     */
    private void initBanner() {
        mBanner.setPages(NetworkImageHolderView::new, mListImage).startTurning(2000).setManualPageable(false);
    }

    /**
     * 释放资源
     */
    private void destroyAudio() {
        if (mVideo != null) {
            mVideo.stopPlayback();
            mVideo.suspend();
            mVideo = null;
        }
        if (mBanner != null) {
            mBanner.stopTurning();
            mBanner = null;
        }
        if (mWindowManager != null && mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout = null;
            mWindowManager = null;
        }
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        isVideoInit = false;
        isMusicInit = false;
    }

    /**
     * 获取代理Url地址
     *
     * @param url 原url
     * @return 代理url
     */
    private String getProxyUrl(String url) {
        return (mServer != null && !TextUtils.isEmpty(url)) ? mServer.getProxyUrl(URLDecoder.decode(url)) : "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyAudio();
        RobotManager.getInstance().removeListener(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mTimer.cancel();
        mTimer = null;
    }

    /**
     * 音乐播放完成
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVideo != null) {
            if (mPositionVideo == mListVideo.size() - 1) {
                mPositionVideo = 0;
            } else {
                mPositionVideo++;
            }
            initVideo(mListVideo.get(mPositionVideo));
        }
        if (mPlayer != null) {
            if (mPositionMusic == mListMusic.size() - 1) {
                mPositionMusic = 0;
            } else {
                mPositionMusic++;
            }
            initMusic(mListMusic.get(mPositionMusic));
        }
    }

    @Override
    public void personInfo(String json) {

    }

    @Override
    public void personNear(boolean person) {
        if (!isCanShow) {
            return;
        }
        if (person) {
            destroyAudio();
            mTimer.cancel();
        } else {
            mTimer.start();
        }
    }

    @Override
    public void personList(String json) {

    }

    /**
     * 图片轮播
     */
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(imageView);
        }
    }
}
