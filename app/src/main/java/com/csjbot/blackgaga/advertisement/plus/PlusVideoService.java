package com.csjbot.blackgaga.advertisement.plus;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.event.AudioEvent;
import com.csjbot.blackgaga.advertisement.factory.ProxyFactory;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.AdvBean;
import com.csjbot.blackgaga.widget.LoopImageHolderView;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by jingwc on 2018/6/12.
 */

public class PlusVideoService extends Service implements OnFaceListener {

    private HttpProxyCacheServer mServer;
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mParams;
    private View mLayout;
    private VideoView mVideoView;
    public ConvenientBanner<String> mBanner;
    private List<String> mVideos;
    private List<String> mPictures;
    private int mVideoIndex;
    private boolean isCreateFloatWindow;
    private PlusBroadCast mPlusBroadCast;

    private MediaPlayer mPlayer;
    private boolean isFirstLaunch = true;//是否第一次启动
    private boolean isMute = false;//是否静音
    private boolean isPerson = false;//是否有人在机器人旁

    private boolean isShowing = false;
    private boolean isShowPicture = false;
    /**
     * 常见视频格式
     */
    private String[] videoUrls = new String[]{".mp4", ".MP4", ".avi", ".AVI", ".wma", ".WMA", ".rmvb", ".RMVB", ".rm", ".RM", ".flash", ".FLASH", ".mid", ".MID", ".3gp", ".3GB", ".mov", ".MOV", ".mkv", ".MKV", ".mpg", ".MPG", ".flv", ".FLV"};

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Robot.initSN();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> getAdvertisement(), 10000);
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVideos = new ArrayList<>();
        mPictures = new ArrayList<>();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPlusBroadCast = new PlusBroadCast();
        RobotManager.getInstance().addListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PLUS_VIDEO_SHOW);
        filter.addAction(PLUS_VIDEO_MIN_VOICE);
        filter.addAction(PLUS_VIDEO_MIN_MUTE);
        filter.addAction(PLUS_VIDEO_BACK);
        filter.addAction(PLUS_VIDEO_HIDE);
        registerReceiver(mPlusBroadCast, filter);
        mServer = ProxyFactory.getProxy(getApplicationContext());
        createFloatWindow();
        getAdvertisement();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void getAudioAction(AudioEvent event) {
        if (event != null) {
            switch (event.getAction()) {
                case UPDATE_DATA:
                    getAdvertisement();
                    break;
            }
        }
    }

    /**
     * 获取广告资源
     */
    private void getAdvertisement() {
        if (TextUtils.isEmpty(Robot.SN)) {
            mHandler.sendEmptyMessage(1);
            return;
        }
        com.csjbot.blackgaga.model.http.factory.ServerFactory.createAdvertisement().getAdvertisement(Robot.SN,
                Constants.Language.getLanguageStr(), new Observer<AdvBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull AdvBean advBean) {
                if (advBean != null && advBean.getResult() != null) {
                    getLog().info("getAdvertisement onNext:advBean" + new Gson().toJson(advBean));
                    mVideos.clear();
                    mPictures.clear();
                    String url;
                    for (AdvBean.ResultBean bean : advBean.getResult()) {
                        switch (bean.getAdvtype()) {
                            case "video":
                                url = bean.getAdvurl().trim();
                                //判断视频格式是否有误
                                for (String videoUrl : videoUrls) {
                                    if (url.endsWith(videoUrl)) {
                                        mVideos.add(bean.getAdvurl());
                                        break;
                                    }
                                }
                                break;
                            case "picture":
                                mPictures.add(bean.getAdvurl());
                                break;
                        }
                    }

                    if (isShowing) {
                        start();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                show();
                getLog().info("getAdvertisement onError" + e.getMessage());
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private CsjlogProxy getLog() {
        return CsjlogProxy.getInstance();
    }

    /**
     * 创建悬浮窗
     */
    private void createFloatWindow() {
        mParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //设置window type
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams.gravity = Gravity.START;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mParams.x = 0;
        mParams.y = 1313;

        // 设置悬浮窗口长宽数据
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = 607;

        mLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_plus_video, null);

        mVideoView = mLayout.findViewById(R.id.myVideoView);
        mBanner = mLayout.findViewById(R.id.banner);

        mVideoView.setOnPreparedListener(mp -> {
            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            mPlayer = mp;
            setVolume();
            mVideoView.start();
        });

        mVideoView.setOnCompletionListener(mp -> {
            if (mVideos.isEmpty()) {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
                mVideoView.setVideoURI(uri);
                return;
            }
            mVideoIndex++;
            if (mVideoIndex >= mVideos.size()) {
                mVideoIndex = 0;
            }
            mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
        });

        mBanner.setPageIndicator(new int[]{R.drawable.iv_point_unselected, R.drawable.iv_point_selected})
                .setPointViewVisible(true)
                .setScrollDuration(800);
    }

    /**
     * 开始播放广告
     * <p>
     * 优先级
     * 1、图片
     * 2、视频（没有就播放默认的）
     */
    private void start() {
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
        if (mBanner.isTurning()) {
            mBanner.stopTurning();
        }
        if (!mPictures.isEmpty()) {
            isShowPicture = true;
            mLayout.setX(0);
            mLayout.setY(0);
            mVideoView.setVisibility(View.GONE);
            mBanner.setVisibility(View.VISIBLE);
            mBanner.setPages(LoopImageHolderView::new, mPictures).startTurning(2000);
            return;
        }

        mBanner.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.stopPlayback();
        isShowPicture = false;
        //设置布局位置
        mLayout.setX(0);
        mLayout.setY(1313);
        if (!mVideos.isEmpty()) {
            mVideoView.setVideoPath(getProxyUrl(mVideos.get(mVideoIndex)));
            mVideoView.start();
        } else {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.vertical_default);
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }
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

    private void show() {
        isMute = false;
        setVolume();
        if (!isCreateFloatWindow) {
            isCreateFloatWindow = true;
            if (isFirstLaunch) {
                start();
                mWindowManager.addView(mLayout, mParams);
                isFirstLaunch = false;
                isShowing = true;
            } else {
                if (isShowPicture) {
                    mLayout.setX(0);
                    mLayout.setY(0);
                } else {
                    mLayout.setX(0);
                    mLayout.setY(1313);
                }
                mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                mParams.height = 607;

                mWindowManager.updateViewLayout(mLayout, mParams);
            }
        }


    }

    private void hide() {
        setVolume();
        if (isCreateFloatWindow) {
            if (mVideoView != null) {
                mVideoView.stopPlayback();
                mVideoView.suspend();
            }
            if (mBanner != null) {
                mBanner.stopTurning();
            }
            if (mWindowManager != null && mLayout != null) {
                mWindowManager.removeView(mLayout);
            }
            isFirstLaunch = true;
            isCreateFloatWindow = false;
            isShowing = false;
        }
    }

    /**
     * 布局缩小有声音
     */
    private void minAndVoice() {
        isMute = false;
        setVolume();
        if (isCreateFloatWindow && isShowing) {
            if (isShowPicture) {
                mLayout.setX(0);
                mLayout.setY(0);
            } else {
                mLayout.setX(0);
                mLayout.setY(1313);
            }
            mParams.width = 100;
            mParams.height = 60;
            mWindowManager.updateViewLayout(mLayout, mParams);
            isCreateFloatWindow = false;
        }
    }

    /**
     * 布局缩小静音
     */
    private void minAndMute() {
        if (isCreateFloatWindow && isShowing) {
            if (isShowPicture) {
                mLayout.setX(0);
                mLayout.setY(0);
            } else {
                mLayout.setX(0);
                mLayout.setY(1313);
            }
            mParams.width = 100;
            mParams.height = 60;
            mWindowManager.updateViewLayout(mLayout, mParams);
            isCreateFloatWindow = false;
        }
        mute();
    }

    /**
     * 设置音量
     */
    private void setVolume() {
        try {
            if (mPlayer != null) {
                if (isPerson || isMute) {
                    mPlayer.setVolume(0.0f, 0.0f);
                } else {
                    mPlayer.setVolume(1.0f, 1.0f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mute() {
        if (mPlayer != null) {
            isMute = true;
            try {
                mPlayer.setVolume(0.0f, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void destroy() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.suspend();
            mVideoView = null;
        }
        if (mBanner != null) {
            mBanner.stopTurning();
        }
        if (mWindowManager != null && mLayout != null) {
            mWindowManager.removeView(mLayout);
            mLayout = null;
            mWindowManager = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
        unregisterReceiver(mPlusBroadCast);
        RobotManager.getInstance().removeListener(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public static final String PLUS_VIDEO_SHOW = "PLUS_VIDEO_SHOW";

    public static final String PLUS_VIDEO_MIN_VOICE = "PLUS_VIDEO_MIN_VOICE";//缩小有声音

    public static final String PLUS_VIDEO_BACK = "PLUS_VIDEO_BACK";

    public static final String PLUS_VIDEO_MIN_MUTE = "PLUS_VIDEO_MIN_MUTE";//缩小没声音

    public static final String PLUS_VIDEO_HIDE = "PLUS_VIDEO_HIDE";//隐藏

    @Override
    public void personInfo(String json) {

    }

    @Override
    public void personNear(boolean person) {
        isPerson = person;
        if (mPlayer == null) {
            return;
        }
        try {
            if (person || isMute) {
                mPlayer.setVolume(0.0f, 0.0f);
            } else {
                mPlayer.setVolume(1.0f, 1.0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void personList(String json) {

    }

    class PlusBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, PLUS_VIDEO_SHOW)) {
                show();
            } else if (TextUtils.equals(action, PLUS_VIDEO_MIN_VOICE)) {
                minAndVoice();
            } else if (TextUtils.equals(action, PLUS_VIDEO_BACK)) {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);
                //获取当前activity栈中activity数量 及 栈顶activity类名，判断是否返回到主页
                if (TextUtils.equals(".Launcher", info.topActivity.getShortClassName()) && info.numActivities == 1) {
                    hide();
                }
            } else if (TextUtils.equals(action, PLUS_VIDEO_MIN_MUTE)) {
                minAndMute();
            } else if (TextUtils.equals(action, PLUS_VIDEO_HIDE)) {
                hide();
            }
        }
    }

}
