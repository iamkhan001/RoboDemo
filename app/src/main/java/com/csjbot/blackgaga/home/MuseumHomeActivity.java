package com.csjbot.blackgaga.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.HomeAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseMainPageActivity;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.pactivity.introduce_list.ProductListActivity;
import com.csjbot.blackgaga.cart.widget.HorizontalPageLayoutManager;
import com.csjbot.blackgaga.cart.widget.PagingScrollHelper;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.blackgaga.feature.vipcenter.VipCenterActivity;
import com.csjbot.blackgaga.global.Constants;
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
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.BaseSpaceItemDecoration;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * BlackGaGa
 * com.csjbot.blackgaga.home
 * 博物馆首页
 */
@Route(path = BRouterPath.MAINPAGE_MUSEUM)
public class MuseumHomeActivity extends BaseMainPageActivity {

    @BindView(R.id.home_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.language)
    LinearLayout mLanguage;

    @BindView(R.id.ll_dot)
    LinearLayout llDot;

    private HomeAdapter mAdapter;

    private Class<? extends Activity> actClass;

    private OnMapListener listener;

    private HomeAI mHomeAI;

    private boolean isWelcome;

    private int centerIndex = 0;

    private volatile boolean isDetected;

    private volatile boolean isComplete;

    private boolean isNetworkEmptyData;

    private IChassis chassis;

    private boolean isFinish = false;
    private boolean isLoadMapSuccess;
    private boolean onPause = false;
    private PagingScrollHelper pagingScrollHelper = null;

    private ImageView[] dots;

    private int mDotsIndex;

    private CarouselLayoutManager mManager;

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

        new Handler().postDelayed(() -> startService(new Intent(MuseumHomeActivity.this, BatteryService.class)), 1000);
        NaviTextHandler.listener = pointMatchListener;
    }

    @Override
    protected void beforeSetContentView() {

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
                SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
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

        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.consultation_activity));
        menu.setMImg(String.valueOf(R.drawable.iv_consultation));
        menus.add(menu);

        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.museum_service));
        menu.setMImg(String.valueOf(R.drawable.iv_self_service));
        menus.add(menu);


        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.intelligent_navigation));
        menu.setMImg(String.valueOf(R.drawable.iv_coupon));
        menus.add(menu);


        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.tour_guide));
        menu.setMImg(String.valueOf(R.drawable.iv_navigation));
        menus.add(menu);

        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.treasure_selection));
        menu.setMImg(String.valueOf(R.drawable.iv_product));
        menus.add(menu);

        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.member_registration));
        menu.setMImg(String.valueOf(R.drawable.iv_vip_sw));
        menus.add(menu);

        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.about_museum));
        menu.setMImg(String.valueOf(R.drawable.iv_about));
        menus.add(menu);

        centerIndex = 1;

        return menus;
    }

    List<RobotMenuListBean.ResultBean.MenulistBean> loadMenu() {
        RobotMenuListBean robotMenuListBean = com.csjbot.blackgaga.model.http.factory.ServerFactory.createProduct().getMenuList();
        if (robotMenuListBean != null) {
            RobotMenuListBean.ResultBean resultBean = robotMenuListBean.getResult();
            List<RobotMenuListBean.ResultBean.MenulistBean> menus = resultBean.getMenulist();
            if (menus == null || menus.size() <= 0) {
                return getDefaultMenu();
            }
            int i = -1;
            List<RobotMenuListBean.ResultBean.MenulistBean> datas = new ArrayList<>();
            for (RobotMenuListBean.ResultBean.MenulistBean menu : menus) {
                if (menu.getLevel() == 1) {
                    i++;
                    datas.add(menu);
                    if (menu.getIsdefaulthome().equals("1")) {
                        centerIndex = i;
                    }
                }
            }
            return datas;
        } else {
            return getDefaultMenu();
        }
    }

    @Override
    public void init() {
        super.init();
        mLanguage.setVisibility(View.VISIBLE);
        List<RobotMenuListBean.ResultBean.MenulistBean> list = new ArrayList<>(getDefaultMenu());
        if (isPlus()) {
            HorizontalPageLayoutManager mLayoutManager = new HorizontalPageLayoutManager(1, 4);
            mRecyclerView.setLayoutManager(mLayoutManager);
            pagingScrollHelper = new PagingScrollHelper();
            pagingScrollHelper.init(list.size(), 4);
            pagingScrollHelper.setUpRecycleView(mRecyclerView);
            pagingScrollHelper.setOnScrollPageListener(index -> {
                if (mDotsIndex != index) {
                    dots[mDotsIndex].setSelected(false);
                    dots[index].setSelected(true);
                    mDotsIndex = index;
                }
            });
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dm_20dp);
            mRecyclerView.addItemDecoration(new BaseSpaceItemDecoration(spacingInPixels, 0, spacingInPixels, spacingInPixels, 2));

            int size = pagingScrollHelper.getAllPage() + 1;
            dots = new ImageView[size];
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mParams.setMargins(10, 0, 10, 0);//设置小圆点左右之间的间隔
            ImageView img;
            for (int i = 0; i < size; i++) {
                img = new ImageView(this);
                img.setLayoutParams(mParams);
                img.setBackgroundResource(R.drawable.point_selector);
                img.setSelected(i == 0);
                dots[i] = img;
                llDot.addView(img);
            }
            mDotsIndex = 0;
        } else {
            llDot.setVisibility(View.GONE);
            mManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
            mManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            mManager.setMaxVisibleItems(2);
            mRecyclerView.setLayoutManager(mManager);
            mRecyclerView.setHasFixedSize(true);
            mManager.scrollToPosition(3);
        }
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter(itemClickListener));
        mAdapter.addAll(list);

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
            if (onPause) return;
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
                        startActivity(new Intent(MuseumHomeActivity.this, NaviGuideCommentActivity.class));
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
        if (getClass().getName().equals(NaviActivity.class.getName()) || getClass().getName().equals(NaviSettingActivity.class.getName()) ||
                getClass().getName().equals(EntertainmentActivity.class.getName())) {
            return;
        }
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

    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void itemClick(int position) {
            boolean isDelayed = false;
            if (mManager != null && position != mManager.getCenterItemPosition()) {
                isDelayed = true;
                mManager.smoothScrollToPosition(mRecyclerView, null, position);
            }

            actClass = null;
            switch (position) {
                case 0:
                    if (isDelayed) {
                        new Handler().postDelayed(() -> {
                            ProductProxy.newProxyInstance().getContent(null);
                            BRouter.jumpActivityByContent("咨询活动");
                        }, 500);
                    } else {
                        ProductProxy.newProxyInstance().getContent(null);
                        BRouter.jumpActivityByContent("咨询活动");
                    }
                    break;
                case 1:
                    if (isDelayed) {
                        new Handler().postDelayed(() -> {
                            ProductProxy.newProxyInstance().getContent(null);
                            BRouter.jumpActivityByContent("场馆服务");
                        }, 500);
                    } else {
                        ProductProxy.newProxyInstance().getContent(null);
                        BRouter.jumpActivityByContent("场馆服务");
                    }
                    break;
                case 2:
                    if (!isLoadMapSuccess) {
                        restoreMapDialog();
                    } else {
                        actClass = NaviActivity.class;
                    }
                    break;
                case 3:
                    if (isDelayed) {
                        new Handler().postDelayed(() -> {
                            ProductProxy.newProxyInstance().getContent(null);
                            BRouter.jumpActivityByContent("参观指南");
                        }, 500);
                    } else {
                        ProductProxy.newProxyInstance().getContent(null);
                        BRouter.jumpActivityByContent("参观指南");
                    }
                    break;
                case 4:
                    actClass = ProductListActivity.class;
                    break;
                case 5:
                    actClass = VipCenterActivity.class;
                    break;
                case 6:
                    if (isDelayed) {
                        new Handler().postDelayed(() -> {
                            ProductProxy.newProxyInstance().getContent(null);
                            BRouter.jumpActivityByContent("关于博物馆");
                        }, 500);
                    } else {
                        ProductProxy.newProxyInstance().getContent(null);
                        BRouter.jumpActivityByContent("关于博物馆");
                    }
                    break;
                default:
                    actClass = null;
                    break;
            }
            if (actClass != null) {
                if (isDelayed) {
                    new Handler().postDelayed(() -> jumpActivity(actClass), 500);
                } else {
                    jumpActivity(actClass);
                }
            }
        }
    };

    @OnClick(R.id.iv_right)
    public void next() {
        if (pagingScrollHelper != null) {
            pagingScrollHelper.setIndexPage(1);
        }
    }

    @OnClick(R.id.iv_left)
    public void prev() {
        if (pagingScrollHelper != null) {
            pagingScrollHelper.setIndexPage(-1);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_museum_home, R.layout.vertical_activity_museum_home);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    interface OnItemClickListener {
        void itemClick(int position);
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<RobotMenuListBean.ResultBean.MenulistBean> datas;

        private OnItemClickListener listener;

        HomeAdapter(OnItemClickListener listener) {
            super();
            this.datas = new ArrayList<>();
            this.listener = listener;
        }

        void addAll(List<RobotMenuListBean.ResultBean.MenulistBean> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
            BlackgagaLogger.debug("isNetworkEmptyData:" + isNetworkEmptyData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (isPlus()) {
                return new MyViewHolder(LayoutInflater.from(MuseumHomeActivity.this).
                        inflate(R.layout.item_home_list_museum_vertical, parent, false));
            } else {
                return new MyViewHolder(LayoutInflater.from(MuseumHomeActivity.this).
                        inflate(R.layout.item_home_list_museum, parent, false));
            }
        }

        Map<String, Bitmap> preMap = new HashMap<>();

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RobotMenuListBean.ResultBean.MenulistBean menu = datas.get(position);
            holder.iv.setImageResource(Integer.valueOf(menu.getMImg()));
            holder.tv.setText(menu.getMenuName());
            holder.iv.setOnClickListener(v -> {
                if (listener != null) {
                    listener.itemClick(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;

            MyViewHolder(View view) {
                super(view);
                iv = view.findViewById(R.id.iv);
                tv = view.findViewById(R.id.tv);
            }
        }

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

        if (NaviTextHandler.handle(text)) {
            return false;
        }

        if (text.contains(getString(R.string.intelligent_navigation))) {
            BlackgagaLogger.debug("isloadMap" + isLoadMapSuccess);
            if (!isLoadMapSuccess) {
                restoreMapDialog();
            } else {
                jumpActivity(NaviActivity.class);
            }
            return true;
        } else if (text.contains(getString(R.string.consultation_activity))) {
            ProductProxy.newProxyInstance().getContent(null);
            BRouter.jumpActivityByContent("咨询活动");
            return true;
        } else if (text.contains(getString(R.string.museum_service))) {
            ProductProxy.newProxyInstance().getContent(null);
            BRouter.jumpActivityByContent("场馆服务");
            return true;
        } else if (text.contains(getString(R.string.tour_guide))) {
            ProductProxy.newProxyInstance().getContent(null);
            BRouter.jumpActivityByContent("参观指南");
            return true;
        } else if (text.contains(getString(R.string.member_registration))) {
            jumpActivity(VipCenterActivity.class);
            return true;
        } else if (text.contains(getString(R.string.treasure_selection))) {
            jumpActivity(ProductListActivity.class);
            return true;
        } else if (text.contains(getString(R.string.about_museum))) {
            ProductProxy.newProxyInstance().getContent(null);
            BRouter.jumpActivityByContent("关于博物馆");
            return true;
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

    public void goNearByAct() {
        jumpActivity(NearByActivity.class);
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
        mRecyclerView.postDelayed(() -> {
            runOnUiThread(() -> {
                if (!isFinish) {
                    Toast.makeText(MuseumHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MuseumHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(MuseumHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
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
