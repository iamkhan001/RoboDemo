package com.csjbot.blackgaga.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseMainPageActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.home.adapter.HomeIconAdapter;
import com.csjbot.blackgaga.home.bean.HomeIconBean;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.http.workstream.Logo;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.body_action.IAction;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.network.CheckEthernetService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.service.BatteryService;
import com.csjbot.blackgaga.service.HomeService;
import com.csjbot.blackgaga.service.LocationService;
import com.csjbot.blackgaga.speechrule.SpeechRule;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * BlackGaGa
 * com.csjbot.blackgaga.home
 * Wql
 * 酒店首页
 */
@Route(path = BRouterPath.MAINPAGE_JIUDIAN)
public class JiuDianHomeActivity extends BaseMainPageActivity {
    @BindView(R.id.language)
    LinearLayout mLanguage;
    @BindView(R.id.rv_hotel)
    RecyclerView rvHotel;
    private OnMapListener listener;
    private boolean isWelcome;
    private volatile boolean isDetected;
    private volatile boolean isComplete;
    private IChassis chassis;
    private boolean isFinish = false;
    private boolean isLoadMapSuccess;
    private boolean onPause = false;
    private boolean OPENWAIT = true;

    private List<HomeIconBean> mLists;
    private HomeIconAdapter mAdapter;
    private CarouselLayoutManager mManager;
    private String robotType;//机器人类型

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.INVISIBLE);
        NaviAction.getInstance().setSpeaker(mSpeak);
        readLocalLanguage();
        startHomeService();
        startService(new Intent(this, CheckEthernetService.class));
        startService(new Intent(this, LocationService.class));
        loadData();

        new Handler().postDelayed(() -> startService(new Intent(JiuDianHomeActivity.this, BatteryService.class)), 1000);
        NaviTextHandler.listener = pointMatchListener;
    }

    private NaviTextHandler.PointMatchListener pointMatchListener = new NaviTextHandler.PointMatchListener() {
        @Override
        public void noMatch() {
            goNaviActAI(null, false);
        }

        @Override
        public void match(NaviBean naviBean, boolean flag) {
            goNaviActAI(naviBean, true);
        }

        @Override
        public void goNavi() {
            goNaviAct();
        }
    };

    private void readLocalLanguage() {
        Constants.Language.CURRENT_LANGUAGE =
                SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME
                        , SharedKey.LANGUAGEMODE_KEY
                        , Constants.Language.CHINESE);
    }

    private void startHomeService() {
        HomeService.IS_SWITCH_LANGUAGE = true;
        /* 启动控制底层连接的HomeService */
        startService(new Intent(this, HomeService.class));
    }

    private void loadData() {
        mLists.add(new HomeIconBean(getString(R.string.hotel_introduction), R.drawable.iv_service_introduction));
        mLists.add(new HomeIconBean(getString(R.string.local_service), R.drawable.iv_peripheral_services));
        mLists.add(new HomeIconBean(getString(R.string.nav_service), R.drawable.iv_navigation));
        mLists.add(new HomeIconBean(getString(R.string.member_registration), R.drawable.iv_member_center));
        mLists.add(new HomeIconBean(getString(R.string.room_introduction), R.drawable.iv_product));
        mAdapter.setNewData(mLists);
    }

    @Override
    public void init() {
        super.init();
        mLists = new ArrayList<>();
        if (isPlus()) {
            mAdapter = new HomeIconAdapter(R.layout.item_jiaju_list, mLists);
        } else {
            mAdapter = new HomeIconAdapter(R.layout.item_jiaju_list, mLists);
        }
        mManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        mManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mManager.scrollToPosition(2);
        rvHotel.setLayoutManager(mManager);
        rvHotel.addOnScrollListener(new CenterScrollListener());
        rvHotel.setHasFixedSize(true);
        rvHotel.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isDelayed = false;
            if (position != mManager.getCenterItemPosition()) {
                isDelayed = true;
                mManager.smoothScrollToPosition(rvHotel, null, position);
            }
            switch (position) {
                case 0://酒店介绍
                    mainHandler.postDelayed(() -> BRouter.jumpActivityByContent(getString(R.string.hotel_introduction)), isDelayed ? 500 : 0);
                    break;
                case 1://周边服务
                    mainHandler.postDelayed(this::goNearByAct, isDelayed ? 500 : 0);
                    break;
                case 2://服务导航
                    mainHandler.postDelayed(this::goNaviAct, isDelayed ? 500 : 0);
                    break;
                case 3://会员注册
                    mainHandler.postDelayed(this::goVIPCenterAct, isDelayed ? 500 : 0);
                    break;
                case 4://房型介绍
                    mainHandler.postDelayed(() -> BRouter.jumpActivityByContent(getString(R.string.room_introduction)), isDelayed ? 500 : 0);
                    break;
            }

        });

        switch (BuildConfig.robotType) {
            case "snow":
                robotType = "小雪";
                break;
            case "alice":
                robotType = "爱丽丝";
                break;
            case "alice_plus":
                robotType = "爱丽丝";
                break;
            case "amy_plus":
                robotType = "艾米";
                break;
            default:
                robotType = "爱丽丝";
                break;
        }

        Constants.Scene.CurrentScene = Constants.Scene.JiuDianScene;
        mLanguage.setVisibility(View.VISIBLE);
        addFaceListener();
        chassis = ServerFactory.getChassisInstance();
        RobotManager.getInstance().addListener(new OnMapListener() {
            @Override
            public void saveMap(boolean state) {

            }

            @Override
            public void loadMap(boolean state) {
                restoreMapResult(state);
            }
        });

        NaviAction.getInstance().registerActionListener(actionListener);

        listener = new OnMapListener() {
            @Override
            public void saveMap(boolean state) {

            }

            @Override
            public void loadMap(boolean state) {
                restoreMapResult(state);
            }
        };
        initIntentData();
    }

    private void initIntentData() {
        Constants.ProductKeyWord.initKeywords();
    }

    private NaviAction.NaviActionListener actionListener = new NaviAction.NaviActionListener() {
        @Override
        public void arrived(NaviBean current) {
            if (onPause) {
                return;
            }
            if (NaviAction.getInstance().getWorkType() == NaviAction.GUIDE_BACK) {
                speak(R.string.back_already);
                return;
            }

            if (current != null) {
                speak(current.getName() + getString(R.string.destination), new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        startActivity(new Intent(JiuDianHomeActivity.this, NaviGuideCommentActivity.class));
                    }
                });
            }
        }

        @Override
        public void error(int code) {
        }

        @Override
        public void sendSuccess() {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        onPause = false;
        isWelcome = true;
        isLoadMapSuccess = SharedPreUtil.getBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, false);
        RobotManager.getInstance().addListener(listener);
        IntentFilter wakeFilter = new IntentFilter(Constants.WAKE_UP);
        registerReceiver(wakeupReceiver, wakeFilter);

        IntentFilter filter1 = new IntentFilter("com.csjbot.blackgaga.naviinit");
        registerReceiver(naviInitReceiver, filter1);

        IntentFilter filter2 = new IntentFilter("com.csjbot.blackgaga.naviondestroy");
        registerReceiver(naviOndestroyReceiver, filter2);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isWelcome = false;
        onPause = true;
    }

    private Handler mFaceHandler = new Handler();

    private Runnable mFaceRb = () -> {
        if(!Constants.isOpenNuance){
            ServerFactory.getSpeechInstance().stopIsr();
        }
    };

    private BroadcastReceiver wakeupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!Constants.isOpenNuance){
                mFaceHandler.removeCallbacks(mFaceRb);
                ServerFactory.getSpeechInstance().startIsr();
                mFaceHandler.postDelayed(mFaceRb, 1000 * 60 * 5);
            }

        }
    };

    private BroadcastReceiver naviInitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!Constants.isOpenNuance){
                mFaceHandler.removeCallbacks(mFaceRb);
                ServerFactory.getSpeechInstance().startIsr();
            }

        }
    };

    private BroadcastReceiver naviOndestroyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!Constants.isOpenNuance){
                mFaceHandler.postDelayed(mFaceRb, 1000 * 60 * 5);
            }

        }
    };

    private volatile long faceTime = 0;

    private void addFaceListener() {
        /* 添加检测人脸监听 */
        RobotManager.getInstance().addListener(new OnFaceListener() {
            @Override
            public void personInfo(String json) {
                if(!Constants.isOpenNuance){
                    if (faceTime != 0) {
                        if ((System.currentTimeMillis() - faceTime) > 2000) {
                            mFaceHandler.removeCallbacks(mFaceRb);
                            ServerFactory.getSpeechInstance().startIsr();
                            mFaceHandler.postDelayed(mFaceRb, 1000 * 60 * 5);
                            faceTime = System.currentTimeMillis();
                        }
                    } else {
                        mFaceHandler.removeCallbacks(mFaceRb);
                        ServerFactory.getSpeechInstance().startIsr();
                        mFaceHandler.postDelayed(mFaceRb, 1000 * 60 * 5);
                        faceTime = System.currentTimeMillis();
                    }
                }


                if (isDetected && isWelcome && !isComplete) {
                    if (mSpeak.isSpeaking()) {
                        return;
                    }
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONObject(json).getJSONArray("face_list").get(0);
                        JSONObject faceRecg = jsonObject.getJSONObject("face_recg");
                        int confidence = faceRecg.getInt("confidence");
                        String name = faceRecg.getString("name");
                        if (confidence > 80) {
                            String speakText = getString(R.string.hello) + "," + name + "," + getString(R.string.welcome) + Logo.getLogoName() + "," + getString(R.string.happy_service)
                                    + getString(R.string.provide_services);
                            String text = speakText + "\n" +
                                    getString(R.string.tell_me) + "\n" +
                                    getString(R.string.take_me_there) + "\n" +
                                    getString(R.string.ask_problem) + "\n" +
                                    getString(R.string.interaction);
                            runOnUiThread(() -> setRobotChatMsg(text));
                            welcome(speakText);
                            getLog().debug("class:HomeActivity message:" + text);
                            welcomeAction();
                            //检测到人之后打开人脸识别
                            isComplete = true;
                        }
                    } catch (JSONException e) {
                        getLog().error("class:HomeActivity e:" + e.toString());
                    }
                }
            }

            @Override
            public void personNear(boolean person) {
                if (isWelcome) {
                    if (person) {
                        welcomeHandler.removeCallbacks(welcomeRb);
                    } else {
                        welcomeHandler.postDelayed(welcomeRb, 10000);
                    }
                }
                Constants.Face.person = person;
                isDetected = person;
                if (person) {
                    isComplete = false;
                    new Thread(() -> {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            getLog().debug("class:HomeActivity error:" + e.toString());
                        }
                        if (!isComplete && isWelcome) {
                            if (mSpeak.isSpeaking()) {
                                return;
                            }
                            isComplete = true;
                            String speakText = getString(R.string.hello) + "," + getString(R.string.welcome) + Logo.getLogoName() + "," + getString(R.string.happy_service)
                                    + getString(R.string.provide_services);
                            String text = speakText + "\n" +
                                    getString(R.string.tell_me) + "\n" +
                                    getString(R.string.take_me_there) + "\n" +
                                    getString(R.string.ask_problem) + "\n" +
                                    getString(R.string.interaction);
                            runOnUiThread(() -> setRobotChatMsg(text));
                            welcome(speakText);
                            welcomeAction();
                        }

                    }).start();
                }
            }

            @Override
            public void personList(String json) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 重新打开监听
     */
    public void reOpenThread() {
        OPENWAIT = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMoveForward();
    }

    /**
     * 打开监听
     */
    private void sendMoveForward() {
        OPENWAIT = true;
        new Thread(() -> {
            int i = 0;
            while (OPENWAIT) {
                try {
                    Thread.sleep(1000);
                    if (i++ >= 10) {
                        OPENWAIT = true;
                        //获取迎宾点
                        String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
                        List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
                        }.getType());
                        if (positionList == null || positionList.size() <= 0) {
                            ServerFactory.getChassisInstance().moveBack();
                            return;
                        }
                        RobotManager.getInstance().robot.reqProxy.navi(positionList.get(0).toJson());
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void welcome(String text) {
        if (SharedPreUtil.getBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, false)) {
            //判断是否是主动迎宾
            new Thread(() -> {
                ServerFactory.getChassisInstance().moveForward();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ServerFactory.getChassisInstance().moveForward();
            }).start();
        }
        speak(text);
    }

    private void welcomeAction() {
        new Thread(() -> {
            IAction action = ServerFactory.getActionInstance();
            switch (BuildConfig.robotType) {
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE:
                    action.nodAction();
                    action.righLargeArmUp();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    action.rightLargeArmDown();
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_SNOW:
                    int time = 5;
                    while (time != 0) {
                        try {
                            Thread.sleep(1000);
                            action.snowDoubleArm();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time--;
                    }
                    break;
            }

        }).start();

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_jiudian_home, R.layout.vertical_activity_jiudian_home);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (Constants.Language.isEnglish()) {
            return false;
        }
        // 拦截主页的语音控制返回动作
        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.BACK, null);
        // 拦截主页的语音控制返回主页动作
        getChatControl().addIntercept(this.getClass().getSimpleName(), SpeechRule.Action.GO, this.getClass().getSimpleName());

        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }

        if (text.contains(getString(R.string.hotel_introduction))) {//酒店介绍
            BRouter.jumpActivityByContent(getString(R.string.hotel_introduction));
            return true;
        }
        if (text.contains(getString(R.string.local_service))) {//周边服务
            goNearByAct();
            return true;
        }
        if (text.contains(getString(R.string.member_registration))) {//会员注册
            goVIPCenterAct();
            return true;
        }
        if (text.contains(getString(R.string.nav_service))) {//服务导航
            goNaviAct();
            return true;
        }
        if (text.contains(getString(R.string.room_introduction))) {//方形介绍
            BRouter.jumpActivityByContent(getString(R.string.room_introduction));
            return true;
        }
        if (!text.contains("在哪")) {
            if (NaviTextHandler.handle(text)) {
                return false;
            }
        }
        if (nearbyIntent(text)) {
            return true;
        }

        prattle(answerText);

        return true;
    }

    private boolean nearbyIntent(String text) {
        Intent intent = new Intent(this, PoiSearchActivity.class);
        for (String str : Constants.NearbyKeyWord.intents) {
            if (text.contains(str)) {
                for (String keyWord : Constants.NearbyKeyWord.keyWords) {
                    if (text.contains(keyWord)) {
                        intent.putExtra(PoiSearchActivity.KEYWORD, keyWord);
                        startActivity(intent);
                        return true;
                    }
                }
                jumpActivity(NearByActivity.class);
                return true;
            }
        }
        return false;
    }

    /*周边服务*/
    private void goNearByAct() {
        BRouter.jumpActivity(BRouterPath.NEAR_BY_MAIN);
    }

    /*会员注册*/
    private void goVIPCenterAct() {
        BRouter.jumpActivity(BRouterPath.VIP_CENTER);
    }

    /*服务导航*/
    private void goNaviAct() {
        if (!isLoadMapSuccess) {
            restoreMapDialog();
        } else {
            jumpActivity(NaviActivity.class);
        }
    }

    private void goNaviActAI(NaviBean current, boolean isPointKnow) {
        if (!isLoadMapSuccess) {
            restoreMapDialog();
        } else {
            NaviAction.getInstance().setCurrent(current);
            BRouter.getInstance()
                    .build(BRouterPath.NAVI_MAIN)
                    .withBoolean(BRouterKey.FROM_AI_GUIDE, true)
                    .withBoolean(BRouterKey.IS_POINT_KNOW, isPointKnow)
                    .navigation();
        }
    }


    /* 恢复地图对话框 */
    private void restoreMapDialog() {
        if (RobotManager.getInstance().getConnectState() && !isLoadMapSuccess) {
            showNewRetailDialog(getString(R.string.map_manager), getString(R.string.is_restore_map), new NewRetailDialog.OnDialogClickListener() {
                @Override
                public void yes() {
                    loadmap();
                    dismissNewRetailDialog();
                }

                @Override
                public void no() {
                    dismissNewRetailDialog();
                }
            });
        } else {
            Toast.makeText(context, R.string.not_connect_slam, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadmap() {
        chassis.loadMap();
        isFinish = false;
        checkConnect();
    }

    private void checkConnect() {
        new Handler().postDelayed(() -> {
            runOnUiThread(() -> {
                if (!isFinish) {
                    Toast.makeText(JiuDianHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
                }
            });
        }, Constants.internalCheckLinux);
    }

    private void restoreMapResult(boolean isState) {
        runOnUiThread(() -> {
            isFinish = true;
            if (isState) {
                isLoadMapSuccess = true;
                SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                Toast.makeText(JiuDianHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(JiuDianHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.Scene.CurrentScene = "";
        NaviTextHandler.activity = null;
        NaviAction.getInstance().unregisterActionListener(actionListener);
        unregisterReceiver(wakeupReceiver);
        unregisterReceiver(naviInitReceiver);
        unregisterReceiver(naviOndestroyReceiver);
        hideVideo();
    }
}
