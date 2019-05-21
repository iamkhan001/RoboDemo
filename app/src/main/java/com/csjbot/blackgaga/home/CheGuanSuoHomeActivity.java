package com.csjbot.blackgaga.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.csjbot.blackgaga.cart.widget.SpaceItemDecoration;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.aboutus.AboutUsActivity;
import com.csjbot.blackgaga.feature.coupon.CouponActivity;
import com.csjbot.blackgaga.feature.entertainment.EntertainmentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.blackgaga.feature.nearbyservice.NearByActivity;
import com.csjbot.blackgaga.feature.nearbyservice.PoiSearchActivity;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity;
import com.csjbot.blackgaga.feature.search.SearchActivity;
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
import com.csjbot.blackgaga.widget.ChatView;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;

/**
 *
 */
@Route(path = BRouterPath.MAINPAGE_CHEGUANSUO)
public class CheGuanSuoHomeActivity extends BaseMainPageActivity {
    @BindView(R.id.home_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.language)
    LinearLayout mLanguage;

//    @BindView(R.id.iv_indicator1)
//    ImageView iv_indicator1;
//
//    @BindView(R.id.iv_indicator2)
//    ImageView iv_indicator2;

    @BindViews({R.id.iv_indicator1, R.id.iv_indicator2})
    List<ImageView> indicators;

    @BindView(R.id.chat_view)
    ChatView chatView;

    HomeAdapter mAdapter;

    LinearLayoutManager mLayoutManager;

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
    protected void beforeSetContentView() {
        Constants.Scene.CurrentScene = Constants.Scene.CheGuanSuo;
    }

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
        chatView.removeBackgroud();
        new Handler().postDelayed(() -> startService(new Intent(CheGuanSuoHomeActivity.this, BatteryService.class)), 1000);
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


    private void switchIndicatorState(boolean state) {
        for (ImageView imageView : indicators) {
            imageView.setImageResource(R.drawable.indicator);
        }

        if (state) {
            indicators.get(0).setImageResource(R.drawable.indicator_choosed);
        } else {
            indicators.get(1).setImageResource(R.drawable.indicator_choosed);
        }
    }

    List<RobotMenuListBean.ResultBean.MenulistBean> getDefaultMenu() {
        isNetworkEmptyData = true;
        List<RobotMenuListBean.ResultBean.MenulistBean> menus = new ArrayList<>();
        RobotMenuListBean.ResultBean.MenulistBean menu;

//        menu = new RobotMenuListBean.ResultBean.MenulistBean();
//        menu.setMenuName(getString(R.string.fun));
//        menu.setMImg(String.valueOf(R.drawable.iv_entertainment));
//        menus.add(menu);

        // 导航服务
        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.nav));
        menu.setMImg(String.valueOf(R.drawable.iv_navigation));
        menus.add(menu);

        // 服务介绍
        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.service_introduce));
        menu.setMImg(String.valueOf(R.drawable.iv_service_introduction));
        menus.add(menu);


        // 周边服务
        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.local_service));
        menu.setMImg(String.valueOf(R.drawable.iv_peripheral_services));
        menus.add(menu);


        // 行政咨询
        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.consultation_in));
        menu.setMImg(String.valueOf(R.drawable.iv_consultation));
        menus.add(menu);

        // 会员中心
        menu = new RobotMenuListBean.ResultBean.MenulistBean();
        menu.setMenuName(getString(R.string.vip_center));
        menu.setMImg(String.valueOf(R.drawable.iv_member_center));
        menus.add(menu);


        centerIndex = 1;
//        getLog().debug("class:HomeActivity message:homepage data is null, use default data");
        return menus;
    }

    @Override
    public void init() {
        super.init();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter(itemClickListener));
//        mRecyclerView.addOnScrollListener(new CenterScrollListener());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        switchIndicatorState(false);
                    } else {
                        switchIndicatorState(true);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //dx>0:向右滑动,dx<0:向左滑动
                //dy>0:向下滑动,dy<0:向上滑动
                if (dx > 0) {
                    isSlidingToLast = true;
//                    CsjlogProxy.getInstance().debug("向右滑动");
                } else {
                    isSlidingToLast = false;
//                    CsjlogProxy.getInstance().debug("向左滑动");
                }
            }
        });
        mLanguage.setVisibility(View.VISIBLE);

//        mHomeAI = HomeAI.newInstance();
//        mHomeAI.initAI((HomeActivity) context);

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

//        mRecyclerView.postDelayed(() -> {
//            mAdapter.addAll(getDefaultMenu());
//            mLayoutManager.scrollToPosition(centerIndex);
//        }, 1000);
        mAdapter.addAll(getDefaultMenu());
        mLayoutManager.scrollToPosition(centerIndex);
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
                        startActivity(new Intent(CheGuanSuoHomeActivity.this, NaviGuideCommentActivity.class));
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
        Constants.Scene.CurrentScene = Constants.Scene.CheGuanSuo;
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
//               reOpenThread();
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
//                reOpenThread();
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
            actClass = null;
//            if (position != mLayoutManager.getCenterItemPosition()) {
//                mLayoutManager.smoothScrollToPosition(mRecyclerView, null, position);
//                isDelayed = true;
//            }
            RobotMenuListBean.ResultBean.MenulistBean menu = mAdapter.datas.get(position);

            String name = menu.getMenuName();

            if (name.equals(getString(R.string.nav))) {
                BlackgagaLogger.debug("isloadMap" + isLoadMapSuccess);
                if (!isLoadMapSuccess) {
                    restoreMapDialog();
                } else {
                    actClass = NaviActivity.class;
                }
            } else if (name.equals(getString(R.string.fun))) {
                actClass = EntertainmentActivity.class;
            } else if (name.equals(getString(R.string.product))) {
                actClass = ProductListActivity.class;
            } else if (name.equals(getString(R.string.local_service))) {
                actClass = NearByActivity.class;
            } else if (name.equals(getString(R.string.check))) {
                actClass = SearchActivity.class;
            } else if (name.equals(getString(R.string.vip_center))) {
                actClass = VipCenterActivity.class;
            } else if (name.equals(getString(R.string.Coupon))) {
                actClass = CouponActivity.class;
            } else if (name.equals(getString(R.string.about))) {
                actClass = AboutUsActivity.class;
            }else if(name.equals(getString(R.string.service_introduce))){
                goServiceIntroduction();
            }else if(name.equals("行政咨询")){
                goAdminConsult();
            }
            if (actClass != null && isDelayed) {
                new Handler().postDelayed(() -> jumpActivity(actClass), 500);
            } else {
                jumpActivity(actClass);
            }
        }
    };

    public void goServiceIntroduction(){
        BRouter.jumpActivityByContent("服务介绍");
    }

    public void goAdminConsult(){
        BRouter.jumpActivityByContent("行政咨询");
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_home_cheguansuo, R.layout.vertical_activity_home_cheguansuo);
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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        List<RobotMenuListBean.ResultBean.MenulistBean> datas;

        OnItemClickListener listener;

        private int VIEW_TYPE_TEXT_UP = 0;
        private int VIEW_TYPE_TEXT_DOWN = 1;

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
        public int getItemViewType(int position) {
            if (datas.get(position).getMenuName().equals(getResources().getString(R.string.nav))
                    || datas.get(position).getMenuName().equals(getResources().getString(R.string.local_service))
                    || datas.get(position).getMenuName().equals(getResources().getString(R.string.vip_center))
                    ) {
                return VIEW_TYPE_TEXT_UP;
            } else {
                return VIEW_TYPE_TEXT_DOWN;
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_TEXT_UP) {
                return new MyViewHolder(LayoutInflater.from(
                        CheGuanSuoHomeActivity.this).inflate(R.layout.item_home_list_up, parent,
                        false));
            } else {
                return new MyViewHolder(LayoutInflater.from(
                        CheGuanSuoHomeActivity.this).inflate(R.layout.item_home_list_down, parent,
                        false));
            }
        }

        Map<String, Bitmap> preMap = new HashMap<>();

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RobotMenuListBean.ResultBean.MenulistBean menu = datas.get(position);
//            if (!isNetworkEmptyData) {
//                if (preMap.get(menu.getMImg()) == null) {
//                    GlideApp.with(context).asFile().load(menu.getMImg()).into(new SimpleTarget<File>() {
//                        @Override
//                        public void onResourceReady(File resource, Transition<? super File> transition) {
//                            Bitmap bitmap = BitmapFactory.decodeFile(resource.getAbsolutePath());
//                            holder.iv.setBackground(null);
//                            holder.iv.setImageBitmap(bitmap);
//                            preMap.put(menu.getMImg(), bitmap);
//                        }
//                    });
//                } else {
//                    holder.iv.setImageBitmap(preMap.get(menu.getMImg()));
//                }
//            } else {
//                holder.iv.setImageResource(Integer.valueOf(menu.getMImg()));
//            }
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

            ImageView iv;

            TextView tv;

            MyViewHolder(View view) {
                super(view);
                iv = view.findViewById(R.id.iv);
                tv = view.findViewById(R.id.tv);
            }
        }

    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
//        NaviTextHandler.init(this);
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

//        if (productIntent(text)) {
//            return true;
//        }

        if (nearbyIntent(text)) {
            return true;
        }

        if (text.contains(getString(R.string.nav))) {
            goNaviAct();
        } else if (text.contains(getString(R.string.fun))) {
            goEntertainmentAct();
        }else if (text.contains(getString(R.string.local_service))) {
            goNearByAct();
        } else if (text.contains(getString(R.string.vip_center))) {
            goVIPCenterAct();
        }else if(text.contains(getString(R.string.service_introduce))){
            goServiceIntroduction();
        }else if(text.contains("行政咨询")){
            goAdminConsult();
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
        jumpActivity(CouponActivity.class);
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
                    Toast.makeText(CheGuanSuoHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CheGuanSuoHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(CheGuanSuoHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NaviTextHandler.activity = null;
        NaviAction.getInstance().unregisterActionListener(actionListener);
        Constants.Scene.CurrentScene = "";
        hideVideo();
    }
}
