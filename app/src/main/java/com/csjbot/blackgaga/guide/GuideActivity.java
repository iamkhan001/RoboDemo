package com.csjbot.blackgaga.guide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.csjbot.blackgaga.MainActivity;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.cart.widget.HorizontalPageLayoutManager;
import com.csjbot.blackgaga.cart.widget.PagingScrollHelper;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.network.CheckEthernetService;
import com.csjbot.blackgaga.network.NetworkConstants;
import com.csjbot.blackgaga.network.NetworkListenerService;
import com.csjbot.blackgaga.service.HomeService;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.CSJToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/12/11.
 * 引导页
 */

public class GuideActivity extends BaseModuleActivity implements GuideContract.view {
    @BindView(R.id.guide_button)
    Button guideButton;
    @BindView(R.id.guide_text)
    TextView guideText;
    @BindView(R.id.view_video)
    VideoView viewVideo;
    @BindView(R.id.view_i)
    ImageView viewImage;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.view_image)
    RelativeLayout viewImageLayout;

    private GuidePresenter guidePresenter;

    private int seekTo = 0;

    private static int id = 1;//默认播放视频

    private static boolean isFirstOpen = true;

    /*动画*/
    private AnimationDrawable animationDrawable;

    private PagingScrollHelper pagingScrollHelper = null;

    /*轮播图*/
    private boolean flags = true;


    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        guidePresenter = new GuidePresenter(this);
        guidePresenter.initView(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(NetworkConstants.SEND_ACTION);
        filter.addAction(Constants.CONNECT_LINUX_BROADCAST);
        registerReceiver(receiverAction, filter);

        if (!isFirstOpen) {
            //没有跳过
            showButton();
        } else {
            //跳过的
            isFirstOpen = false;
            startHomeService();
            checkWifi();
            guideButton.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        guidePresenter.startTh();
                        break;
                    case MotionEvent.ACTION_UP:
                        guidePresenter.stopDown();
                        break;
                }
                return true;
            });
            initMedia();
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return guidePresenter;
    }

    private long preTime;

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            // 如果时间间隔大于2秒，不处理
            if ((currentTime - preTime) > 2000) {
                // 显示消息
                CSJToast.showToast(this, getString(R.string.one_more_exit_program));
                //更新时间
                preTime = currentTime;
                //截获事件，不再处理
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*重新开启*/
        startMedia(id);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }


    BroadcastReceiver receiverAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            guidePresenter.stopDelay();
            switch (intent.getAction()) {
                case NetworkConstants.SEND_ACTION:
                    BlackgagaLogger.debug("chenqi action" + NetworkConstants.SEND_ACTION);
                    int state = intent.getIntExtra(NetworkConstants.NET_WORK_STATE, 0);
                    if (state == NetworkConstants.NETWORK_AVAILABLE) {
                        guideText.setText(getString(R.string.network_available));
                        guidePresenter.actionSpLoad();
                    } else {
                        guideText.setText(getString(R.string.net_work_nouse));
                    }
                    break;
                case Constants.CONNECT_LINUX_BROADCAST:
                    int td = intent.getIntExtra(Constants.LINUX_CONNECT_STATE, 0);
                    if (td == HomeService.LINUX_CONNECTED) {
                        guideText.setText(R.string.linux_connected_check_netword);
                        //检查网络是否可用
                        startService(new Intent(GuideActivity.this, NetworkListenerService.class));
                        guidePresenter.delayTh(GuidePresenter.Type.NET);
                    }
                    break;
            }
            //通知连接成功
        }
    };

    private void startHomeService() {
        startService(new Intent(this, CheckEthernetService.class));
        HomeService.IS_SWITCH_LANGUAGE = true;
        startService(new Intent(this, HomeService.class));
        guideText.setText(R.string.connect_linux);
        guidePresenter.delayTh(GuidePresenter.Type.LINUX);
    }

    /**
     * 只能是第一调用
     */
    private void initMedia() {
        Random random = new Random();
        int i = random.nextInt(3);
        startMedia(i);
    }

    private void initView() {
        if (viewVideo.getVisibility() == View.VISIBLE) {
            viewVideo.setVisibility(View.GONE);
        }
        if (viewImageLayout.getVisibility() == View.VISIBLE) {
            viewImageLayout.setVisibility(View.GONE);
        }
        if (viewImage.getVisibility() == View.VISIBLE) {
            viewImage.setVisibility(View.GONE);
        }
    }

    /**
     * 开始播放媒体
     * 在开始的时候记录id是多少
     *
     * @param id
     */
    private void startMedia(int id) {
        initView();
        this.id = id;
        switch (id) {
            case 0:
                if (viewVideo.getVisibility() == View.GONE) {
                    viewVideo.setVisibility(View.VISIBLE);
                }
                Uri mUri = Uri.parse("android.resource://" + getPackageName() );
                playVideo(mUri);
                //播放视频
                break;
            case 1:
                if (viewImageLayout.getVisibility() == View.GONE) {
                    viewImageLayout.setVisibility(View.VISIBLE);
                }
                playCarouselFigure();
                //播放轮播图
                break;
            case 2:
                if (viewImage.getVisibility() == View.GONE) {
                    viewImage.setVisibility(View.VISIBLE);
                }
                playAnimation();
                //播放帧动画
                break;
        }
    }

    /**
     * 暂停所有资源
     *
     * @param id
     */
    private void stopMedia(int id) {
        switch (id) {
            case 1:
                viewVideo.pause();
                seekTo = viewVideo.getCurrentPosition();
                //播放视频
                break;
            case 2:
                //关闭线程
                flags = false;
                //播放轮播图
                break;
            case 3:
                animationDrawable.stop();
                //播放帧动画
                break;
        }
    }


    /**
     * 播放视频广告
     * 如果播放失败了就放其他媒体
     */
    private void playVideo(Uri mUri) {
        viewVideo.setVideoURI(mUri);
        if (seekTo != 0) {
            viewVideo.seekTo(seekTo);
        }
        viewVideo.start();
        //或者
        viewVideo.setOnCompletionListener(mp -> {
            viewVideo.setVideoURI(mUri);
            viewVideo.start();
        });
        viewVideo.setOnErrorListener((mp, what, extra) -> {
            int ll = new Random().nextInt(3);
            if (ll != 1) {
                startMedia(ll);
            }
            return false;
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopMedia(id);
    }

    /**
     * 播放轮播图
     */
    private void playCarouselFigure() {
        //获取本地图片
        List<Integer> list = new ArrayList();
        list.add(R.drawable.gas_station);
        list.add(R.drawable.atm);
        list.add(R.drawable.hotel);
        if (list.size() == 0) {
            chooseOkPlayer(id);
            return;
        }
        GuideAdapter myAdapter = new GuideAdapter(list);
        recyclerview.setAdapter(myAdapter);
        HorizontalPageLayoutManager horizontalPageLayoutManager = new HorizontalPageLayoutManager(1, 1);
        pagingScrollHelper = new PagingScrollHelper();
        pagingScrollHelper.setUpRecycleView(recyclerview);
        RecyclerView.LayoutManager layoutManager = horizontalPageLayoutManager;
        if (layoutManager != null) {
            recyclerview.setLayoutManager(layoutManager);
        }
        pagingScrollHelper.init(list.size(), 1);
        flags = true;
        Thread thread = new Thread(() -> {
            while (flags) {
                try {
                    Thread.sleep(3000);
                    handler.sendMessage(Message.obtain());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * 通过type来选择另外两种不同的播放方式
     *
     * @param type
     */
    private void chooseOkPlayer(int type) {
        int[] si = new int[]{3};
        int tpy = 0;
        for (int i = 0; i < 3; i++) {
            if (type == i) {
                continue;
            } else {
                tpy++;
                si[tpy] = i;
            }
        }
        int il = new Random().nextInt(si.length);
        startMedia(il);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            runOnUiThread(() -> pagingScrollHelper.setIndexPage(1));
        }
    };

    /**
     * 播放帧动画
     */
    private void playAnimation() {
        viewImage.setImageResource(R.drawable.zhendonghua);
        animationDrawable = (AnimationDrawable) viewImage.getDrawable();
        animationDrawable.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverAction);
        guidePresenter.releaseHandler();
        viewVideo.stopPlayback();
        isFirstOpen = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }


    /**
     * 检查Wifi如果是关闭状态则开启Wifi
     */
    public void checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
    }

    @Override
    public void timeOut(GuidePresenter.Type type) {
        switch (type) {
            case LINUX:
                showTextMsg(getString(R.string.linux_timeout));
                break;
            case NET:
                showTextMsg(getString(R.string.network_timeout));
                break;
            case DATA:
                showTextMsg(getString(R.string.data_timeout));
                break;
        }
    }

    private void showTextMsg(String msg) {
        runOnUiThread(() -> guideText.setText(msg));
    }

    @Override
    public void showButton() {
        if (viewVideo != null)
            viewVideo.stopPlayback();
        Intent in = new Intent(this, MainActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }

    @Override
    public void shutDown() {
        showButton();
    }
}
