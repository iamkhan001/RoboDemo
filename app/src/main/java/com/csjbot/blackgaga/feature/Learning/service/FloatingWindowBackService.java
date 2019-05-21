package com.csjbot.blackgaga.feature.Learning.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.Learning.event.LearnEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.OutputStream;


public class FloatingWindowBackService extends Service {

    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private Handler mHandler = new Handler();
    private AudioManager audio;
    private int mSlop;


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        createFloatView();
    }

    public void Restart() {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName(getApplication().getPackageName(), "com.csjbot.snowbot.activity.LauncherActivity");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        startActivity(mIntent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private AlphaAnimation mHideAnimation = null;

    private AlphaAnimation mShowAnimation = null;

    /**
     * View渐隐动画效果
     */
    private void setHideAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }

        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);

        view.startAnimation(mHideAnimation);
        mHandler.postDelayed(() -> view.setVisibility(View.INVISIBLE), duration);
    }

    /**
     * View渐现动画效果
     */
    private void setShowAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }


        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);

        view.startAnimation(mShowAnimation);
        mHandler.postDelayed(() -> view.setVisibility(View.VISIBLE), duration);
    }

    private void createFloatView() {
        wmParams = new LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = 552;

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.TOP | Gravity.START;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_window_back_layout, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        //浮动窗口按钮
        ImageView mFloatView = mFloatLayout.findViewById(R.id.floating_btn);
        View contentLayout = mFloatLayout.findViewById(R.id.contentLayout);
        ImageView float_back = mFloatLayout.findViewById(R.id.float_back);
        ImageView float_vol_up = mFloatLayout.findViewById(R.id.float_vol_up);
        ImageView float_vol_down = mFloatLayout.findViewById(R.id.float_vol_down);

        mFloatLayout.setOnTouchListener(new OnTouchListener() {
            boolean isMove;

            float rawX;//相对屏幕X坐标
            float rawY;//相对屏幕Y坐标
            float startX;//相对View X坐标
            float startY;//相对View Y坐标
            float downX;
            float downY;
            int newX;
            int newY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rawX = event.getRawX();
                rawY = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        downX = event.getRawX();
                        downY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        newX = (int) (rawX - startX);
                        newY = (int) (rawY - startY);
                        wmParams.x = newX;
                        wmParams.y = newY;
                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isMove = Math.abs(rawX - downX) > mSlop || Math.abs(rawY - downY) > mSlop;
                }
                return isMove;
            }
        });

//        mFloatView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (contentLayout.getVisibility() == View.VISIBLE) {
//                    setHideAnimation(contentLayout, 200);
//                } else {
//                    setShowAnimation(contentLayout, 200);
//                }
//            }
//        });

        float_back.setOnClickListener(v -> {
            RootShellCmd cmd = new RootShellCmd();
            cmd.simulateKey(KeyEvent.KEYCODE_BACK);
        });

        float_vol_up.setOnClickListener(v -> audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI));

        float_vol_down.setOnClickListener(v -> {
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//                Restart();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void closeApp(LearnEvent event) {
        if (!event.isCloseApp()) {
            return;
        }
        RootShellCmd cmd = new RootShellCmd();
        cmd.simulateKey(KeyEvent.KEYCODE_BACK);
    }

    /**
     * 用root权限执行Linux下的Shell指令
     *
     * @author jzj
     * @since 2014-09-09
     */
    public class RootShellCmd {
        private OutputStream os;

        /**
         * 执行shell指令
         *
         * @param cmd 指令
         */
        public final void exec(String cmd) {
            try {
                if (os == null) {
                    os = Runtime.getRuntime().exec("su").getOutputStream();
                }
                os.write(cmd.getBytes());
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 后台模拟全局按键
         *
         * @param keyCode 键值
         */
        public final void simulateKey(int keyCode) {
            exec("input keyevent " + keyCode + "\n");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        EventBus.getDefault().unregister(this);
    }


    public void addFloatView() {
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }


}
