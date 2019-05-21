package com.csjbot.blackgaga.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseMainPageActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.Learning.service.FloatingWindowBackService;
import com.csjbot.blackgaga.feature.clothing.ClothingListActivity;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.entertainment.fuzhuang.FuzhuangEntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.blackgaga.feature.shop_location.ShopLocationActivity;
import com.csjbot.blackgaga.feature.vipcenter.FuZhuangVipCenterActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.home.adapter.HomeIconAdapter;
import com.csjbot.blackgaga.home.bean.HomeIconBean;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
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
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.blackgaga.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.blackgaga.widget.pagergrid.PagerGridSnapHelper;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * BlackGaGa
 * com.csjbot.blackgaga.home
 * Wql
 * 服装首页
 */
@Route(path = BRouterPath.MAINPAGE_FUZHUANG)
public class FuZhuangHomeActivity extends BaseMainPageActivity {

    @BindView(R.id.rv_cloth)
    RecyclerView rvCloth;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;

    @BindView(R.id.language)
    LinearLayout mLanguage;

    private List<HomeIconBean> mList;
    private HomeIconAdapter mAdapter;
    private int rows = 1;
    private int columns = 5;
    private int pageSize;//页数

    private OnMapListener listener;
    private boolean isWelcome;
    private volatile boolean isDetected;
    private volatile boolean isComplete;
    private IChassis chassis;
    private boolean isFinish = false;
    private boolean isLoadMapSuccess;
    private boolean onPause = false;

    private Intent mIntent;

    @Override
    public void init() {
        Constants.Scene.CurrentScene = Constants.Scene.Fuzhuang;
        super.init();
        initHomeIcon();
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

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.GONE);
        mLanguage.setVisibility(View.VISIBLE);
        NaviAction.getInstance().setSpeaker(mSpeak);
        readLocalLanguage();
        startHomeService();
        startService(new Intent(this, CheckEthernetService.class));
        startService(new Intent(this, LocationService.class));
        loadData();

        new Handler().postDelayed(() -> startService(new Intent(FuZhuangHomeActivity.this, BatteryService.class)), 1000);
        NaviTextHandler.listener = pointMatchListener;
    }

    /**
     * 初始化首页图标
     */
    private void initHomeIcon() {
        mList = new ArrayList<>();
        //可以根据大小屏动态设置不同的布局
        if (TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            mAdapter = new HomeIconAdapter(R.layout.item_home_icon_vertical, mList);
        } else {
            mAdapter = new HomeIconAdapter(R.layout.item_home_icon, mList);
        }
        PagerGridLayoutManager manager = new PagerGridLayoutManager(rows, columns, PagerGridLayoutManager.HORIZONTAL);
        rvCloth.setLayoutManager(manager);

        manager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                ImageView imageView;
                for (int i = 0; i < llDots.getChildCount(); i++) {
                    imageView = (ImageView) llDots.getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.iv_point_selected);
                    } else {
                        imageView.setImageResource(R.drawable.iv_point_unselected);
                    }
                }
            }
        });
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(rvCloth);
        rvCloth.setHasFixedSize(true);
        rvCloth.setAdapter(mAdapter);

        mList.add(new HomeIconBean(getString(R.string.lead_way), R.drawable.iv_navigation));
        mList.add(new HomeIconBean(getString(R.string.vip_center), R.drawable.iv_member_center));
        mList.add(new HomeIconBean(getString(R.string.cloth_list), R.drawable.iv_product));
        mList.add(new HomeIconBean(getString(R.string.online_mall), R.drawable.flower_shop));
        mList.add(new HomeIconBean(getString(R.string.shop_introduction), R.drawable.iv_about));
        mList.add(new HomeIconBean(getString(R.string.interactive_entertainment), R.drawable.iv_entertainment));
        mList.add(new HomeIconBean(getString(R.string.local_service), R.drawable.iv_peripheral_services));
        mList.add(new HomeIconBean("店铺分布", R.drawable.iv_shop_location));
        mAdapter.setNewData(mList);

        double a = (double) mList.size() / (double) (rows * columns);
        pageSize = (int) Math.ceil(a);
        if (pageSize > 1) {
            llDots.removeAllViews();
            ImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new ImageView(this);
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.iv_point_selected);
                } else {
                    dot.setImageResource(R.drawable.iv_point_unselected);
                }
                llDots.addView(dot);
            }
        }
        //首页获取衣服种类
        ProductProxy.newProxyInstance().getAllClothType(Robot.SN, new Observer<ClothTypeBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ClothTypeBean clothTypeBean) {
                if (clothTypeBean == null) {
                    return;
                }
                if (TextUtils.equals(clothTypeBean.getMessage(), "ok")
                        && TextUtils.equals(clothTypeBean.getStatus(), "200")) {
                    //衣服种类
                    Constants.ClothProduct.clothTypeBean = clothTypeBean;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().error("FuZhuangHomeActivity --->获取服装种类列表失败: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    goNaviAct();
                    break;
                case 1:
                    jumpActivity(FuZhuangVipCenterActivity.class);
                    break;
                case 2:
                    BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_TYPE);
                    break;
                case 3:
                    sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
                    PackageManager packageManager = getPackageManager();
                    Intent intent = packageManager.getLaunchIntentForPackage("com.longlive.search");
                    if (intent != null) {
                        if (mIntent == null) {
                            mIntent = new Intent(this, FloatingWindowBackService.class);
                        }
                        startService(mIntent);
                        mainHandler.postDelayed(() -> speak("这是我们的线上商城，可以扫描二维码在线购买商品"), 800);
                        startActivity(intent);
                    }
                    break;
                case 4:
                    BRouter.jumpActivityByContent(getString(R.string.shop_introduction));
                    break;
                case 5:
                    jumpActivity(FuzhuangEntertainmentActivity.class);
                    break;
                case 6:
                    jumpActivity(NearByActivity.class);
                    break;
                case 7:
                    jumpActivity(ShopLocationActivity.class);
                    break;
            }
        });
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
                SharedPreUtil.
                        getInt(SharedKey.LANGUAGEMODE_NAME
                                , SharedKey.LANGUAGEMODE_KEY
                                , Constants.Language.CHINESE);
    }

    private void startHomeService() {
        HomeService.IS_SWITCH_LANGUAGE = true;
        /* 启动控制底层连接的HomeService */
        startService(new Intent(this, HomeService.class));
    }

    private void loadData() {
        ProductProxy proxy = com.csjbot.blackgaga.model.http.factory.ServerFactory.createProduct();
        proxy.removeMenuList();
        proxy.getRobotMenuList(true);
    }

    private void initIntentData() {
        Constants.ProductKeyWord.initKeywords();
    }

    NaviAction.NaviActionListener actionListener = new NaviAction.NaviActionListener() {
        @Override
        public void arrived(NaviBean current) {
            if (onPause)
                return;
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
                        startActivity(new Intent(FuZhuangHomeActivity.this, NaviGuideCommentActivity.class));
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
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
        if (mIntent != null) {
            stopService(mIntent);
            mIntent = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isWelcome = false;
        onPause = true;
    }

    private void addFaceListener() {
        /* 添加检测人脸监听 */
        RobotManager.getInstance().addListener(new OnFaceListener() {
            @Override
            public void personInfo(String json) {
//                reOpenThread();
                if (isDetected && isWelcome && !isComplete) {
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONObject(json).getJSONArray("face_list").get(0);
                        JSONObject faceRecg = jsonObject.getJSONObject("face_recg");
                        int confidence = faceRecg.getInt("confidence");
                        String name = faceRecg.getString("name");
                        if (confidence > 80) {
                            if (mSpeak.isSpeaking()) {
                                return;
                            }
                            String text = getString(R.string.hello) + "," + name + "," + getString(R.string.welcome) + Logo.getLogoName() + "，";
                            welcome(text);
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
                            String text = getString(R.string.hello_welcome) + Logo.getLogoName() + "，";
                            welcome(text);
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
        ClothTypeBean clothTypeBean = Constants.ClothProduct.clothTypeBean;
        runOnUiThread(() -> {
            if (clothTypeBean != null
                    && clothTypeBean.getResult() != null
                    && clothTypeBean.getResult().size() > 2) {
                setRobotChatMsg(text + "爱丽丝正在为您服务，我可以为您提供引领带路、商品讲解等服务。\n" +
                        "您可以通过语音对我说：\n" +
                        "1、" + clothTypeBean.getResult().get(0).getSecondLevel() + "\n" +
                        "2、" + clothTypeBean.getResult().get(1).getSecondLevel() + "\n" +
                        "3、" + clothTypeBean.getResult().get(2).getSecondLevel() + "\n" +
                        "如果需要问我其他问题，请对我说“灵犀灵犀”");
            } else {
                setRobotChatMsg(text + getString(R.string.happy_service));
            }
        });
        if (clothTypeBean != null
                && clothTypeBean.getResult() != null
                && clothTypeBean.getResult().size() > 1) {
            speak(text + "您是想看看" + clothTypeBean.getResult().get(0).getSecondLevel() + "还是" + clothTypeBean.getResult().get(1).getSecondLevel() + "？");
        } else {
            speak(text + getString(R.string.happy_service));
        }

    }

    private boolean openWait = true;

    /**
     * 重新打开监听
     */
    public void reOpenThread() {
        openWait = false;
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
        if (getClass().getName().equals(NaviActivity.class.getName()) || getClass().getName().equals(NaviSettingActivity.class.getName()) ||
                getClass().getName().equals(EntertainmentActivity.class.getName())) {
            return;
        }
        openWait = true;
        new Thread(() -> {
            int i = 0;
            while (openWait) {
                try {
                    Thread.sleep(1000);
                    if (i++ >= 10) {
                        openWait = true;
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
//        return getCorrectLayoutId(R.layout.activity_fuzhuang_home, R.layout.vertical_activity_fuzhuang_home);
        return R.layout.vertical_activity_fuzhuang_home;
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
        if (onPause) {
            return false;
        }
        if (Constants.Language.isEnglish()) {
            return false;
        }

        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }

        if (NaviTextHandler.handle(text)) {
            return false;
        }

        if (nearbyIntent(text)) {
            return true;
        }

        if (Constants.ClothProduct.clothTypeBean != null && Constants.ClothProduct.clothTypeBean.getResult() != null
                && !Constants.ClothProduct.clothTypeBean.getResult().isEmpty()) {
            for (ClothTypeBean.ResultBean resultBean : Constants.ClothProduct.clothTypeBean.getResult()) {
                if (text.contains(resultBean.getSecondLevel())) {
                    BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_LIST,
                            ClothingListActivity.SELECT_TYPE, resultBean.getSecondLevel());
                    return true;
                }
            }
        }

        if (text.contains(getString(R.string.lead_way))) {
            goNaviAct();
        } else if (text.contains(getString(R.string.vip_center))) {
            jumpActivity(FuZhuangVipCenterActivity.class);
        } else if (text.contains(getString(R.string.cloth_list))) {
            BRouter.jumpActivity(BRouterPath.PRODUCT_CLOTHING_TYPE);
        } else if (text.contains(getString(R.string.online_mall))) {
            sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage("com.longlive.search");
            if (intent != null) {
                if (mIntent == null) {
                    mIntent = new Intent(this, FloatingWindowBackService.class);
                }
                startService(mIntent);
                mainHandler.postDelayed(() -> speak("这是我们的线上商城，可以扫描二维码在线购买商品"), 800);
                startActivity(intent);
            }
        } else if (text.contains(getString(R.string.shop_introduction))) {
            BRouter.jumpActivityByContent(getString(R.string.shop_introduction));
        } else if (text.contains(getString(R.string.interactive_entertainment))) {
            jumpActivity(FuzhuangEntertainmentActivity.class);
        } else if (text.contains(getString(R.string.local_service))) {
            jumpActivity(NearByActivity.class);
        }

        if(isChatting){
            speak(answerText, new OnSpeakListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    ClothTypeBean clothTypeBean = Constants.ClothProduct.clothTypeBean;
                    if (clothTypeBean != null
                            && clothTypeBean.getResult() != null
                            && clothTypeBean.getResult().size() > 1) {
                        String text = "亲，请问您今天需要买什么衣服，" + clothTypeBean.getResult().get(0).getSecondLevel() + "还是" + clothTypeBean.getResult().get(1).getSecondLevel() + "？";
                        prattle(text);
                    } else {
                        prattle("亲，请问您今天需要买什么衣服？");
                    }
                }
            });
            setRobotChatMsg(answerText);
        }else{
            prattle(answerText);
        }
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
                    loadMap();
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

    private void loadMap() {
        chassis.loadMap();
        isFinish = false;
        checkConnect();
    }

    private void checkConnect() {
        new Handler().postDelayed(() -> runOnUiThread(() -> {
            if (!isFinish) {
                Toast.makeText(FuZhuangHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
            }
        }), Constants.internalCheckLinux);
    }

    private void restoreMapResult(boolean isState) {
        runOnUiThread(() -> {
            isFinish = true;
            if (isState) {
                isLoadMapSuccess = true;
                SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                Toast.makeText(FuZhuangHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(FuZhuangHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void product(String intentjson, String source) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.Scene.CurrentScene = "";
        NaviTextHandler.activity = null;
        NaviAction.getInstance().unregisterActionListener(actionListener);
        hideVideo();
    }
}
