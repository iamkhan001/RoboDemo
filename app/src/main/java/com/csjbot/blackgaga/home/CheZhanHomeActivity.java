package com.csjbot.blackgaga.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.HomeAI;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseMainPageActivity;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.introduce_list.ProductListActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.aboutus.AboutUsActivity;
import com.csjbot.blackgaga.feature.coupon.CouponActivity;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity;
import com.csjbot.blackgaga.feature.search.SearchActivity;
import com.csjbot.blackgaga.feature.vipcenter.VipCenterActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.jpush.service.GetMessageService;
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
import com.csjbot.blackgaga.speechrule.SpeechRule;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.Csjlogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * BlackGaGa
 * com.csjbot.blackgaga.home
 * Wql
 * 车站首页
 */
@Route(path = BRouterPath.MAINPAGE_CHEZHAN)
public class CheZhanHomeActivity extends BaseMainPageActivity {
    @BindView(R.id.language)
    LinearLayout mLanguage;


    Class<? extends Activity> actClass;


    private OnMapListener listener;

    HomeAI mHomeAI;

    boolean isWelcome;

    int centerIndex = 0;

    volatile boolean isDetected;

    volatile boolean isComplete;

    boolean isNetworkEmptyData;

    private IChassis chassis;

    private boolean isFinish = false;
    private boolean isLoadMapSuccess;
    private boolean onPause = false;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setBackVisibility(View.INVISIBLE);

        NaviAction.getInstance().setSpeaker(mSpeak);
        readLocalLanguage();
        startHomeService();
        startService(new Intent(this, CheckEthernetService.class));
        startService(new Intent(this, LocationService.class));
//        startService(new Intent(this, GetMessageService.class));
        loadData();

        new Handler().postDelayed(() -> startService(new Intent(CheZhanHomeActivity.this, BatteryService.class)), 1000);
        NaviTextHandler.listener = pointMatchListener;
    }

    NaviTextHandler.PointMatchListener pointMatchListener = new NaviTextHandler.PointMatchListener() {
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

    List<RobotMenuListBean.ResultBean.MenulistBean> getDefaultMenu() {
        isNetworkEmptyData = true;
        List<RobotMenuListBean.ResultBean.MenulistBean> menus = new ArrayList<>();
        RobotMenuListBean.ResultBean.MenulistBean menu;
        centerIndex = 1;
        //        getLog().debug("class:HomeActivity message:homepage data is null, use default data");
        return menus;
    }


    @Override
    public void init() {
        super.init();
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
                speak(current.getName() + getString(R.string.destination) , new OnSpeakListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        startActivity(new Intent(CheZhanHomeActivity.this, NaviGuideCommentActivity.class));
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
        //        mTitleView.setLogo(R.drawable.index_logo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isWelcome = false;
        onPause = true;
    }

    void addFaceListener() {
        /* 添加检测人脸监听 */
        RobotManager.getInstance().addListener(new OnFaceListener() {
            @Override
            public void personInfo(String json) {
                reOpenThread();
                if (isDetected && isWelcome && !isComplete) {
                    try {
                        JSONObject jsonObject = (JSONObject) new JSONObject(json).getJSONArray("face_list").get(0);
                        JSONObject faceRecg = jsonObject.getJSONObject("face_recg");
                        int confidence = faceRecg.getInt("confidence");
                        String name = faceRecg.getString("name");
                        if (confidence > 80) {
                            if(mSpeak.isSpeaking()) {
                                return;
                            }
                            String text = getString(R.string.hello) + "," + name + "," + getString(R.string.welcome) + Logo.getLogoName() + "，" + getString(R.string.happy_service);
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
                reOpenThread();
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
                            if(mSpeak.isSpeaking()) {
                                return;
                            }
                            isComplete = true;
                            String text = getString(R.string.hello_welcome) + Logo.getLogoName() + "，" + getString(R.string.happy_service);
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
            ServerFactory.getChassisInstance().moveForward();
            sendMoveForward();
        }
        runOnUiThread(() -> setRobotChatMsg(text));
        speak(text);
    }

    private boolean OPENWAIT = true;

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
    public void sendMoveForward() {
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

    public void welcomeAction() {
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
        return R.layout.activity_chezhan_home;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


    @OnClick({R.id.rl_consultation_chezhan, R.id.rl_nav_chezhan, R.id.rl_product_chezhan, R.id.rl_fun_chezhan, R.id.rl_local_service_chezhan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_consultation_chezhan:
                BRouter.jumpActivityByContent("咨询服务");
                break;
            case R.id.rl_nav_chezhan:
                goNaviAct();
                break;
            case R.id.rl_product_chezhan:
                goProductAct();
                break;
            case R.id.rl_fun_chezhan:
                goEntertainmentAct();
                break;
            case R.id.rl_local_service_chezhan:
                goNearByAct();
                break;
        }
    }

    interface OnItemClickListener {
        void itemClick(int position);
    }


    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        //        NaviTextHandler.init(ChemistHomeActivity.this);
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

        if (NaviTextHandler.handle(text)) {
            return false;
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

    private boolean productIntent(String text) {
        if (Constants.ProductKeyWord.products.size() == 0) {
            Constants.ProductKeyWord.initKeywords();
        }
        for (String str : Constants.ProductKeyWord.intents) {
            if (text.contains(str)) {
                for (RobotSpListBean.ResultBean.ProductBean product : Constants.ProductKeyWord.products) {
                    if (text.contains(product.getName())) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        jumpActivity(ProductDetailActivity.class, bundle);
                        return true;
                    }
                }
                //                jumpActivity(ProductListActivity.class);
            }
        }
        return false;
    }


    public void goEntertainmentAct() {
        jumpActivity(EntertainmentActivity.class);
    }

    public void goCouponAct() {
        Csjlogger.debug("coupon_chemi--->");
        jumpActivity(CouponActivity.class);
    }


    public void goNearByAct() {
        jumpActivity(NearByActivity.class);
    }

    public void goAboutAct() {
        jumpActivity(AboutUsActivity.class);
    }

    public void goSearchAct() {
        jumpActivity(SearchActivity.class);
    }

    public void goVIPCenterAct() {
        jumpActivity(VipCenterActivity.class);
    }

    public void goProductAct() {
        jumpActivity(ProductListActivity.class);
    }

    public void goNaviAct() {
        if (!isLoadMapSuccess) {
            restoreMapDialog();
        } else {
            jumpActivity(NaviActivity.class);
        }
    }

    public void goNaviActAI(NaviBean current, boolean isPointKnow) {
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
                    Toast.makeText(CheZhanHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CheZhanHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(CheZhanHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NaviTextHandler.activity = null;
        NaviAction.getInstance().unregisterActionListener(actionListener);
        hideVideo();
    }
}
