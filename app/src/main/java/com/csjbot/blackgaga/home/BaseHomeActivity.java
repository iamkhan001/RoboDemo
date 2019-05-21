package com.csjbot.blackgaga.home;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.navigation.NaviAction;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.NaviGuideCommentActivity;
import com.csjbot.blackgaga.feature.navigation.NaviTextHandler;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.jpush.service.GetMessageService;
import com.csjbot.blackgaga.localbean.NaviBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.network.CheckEthernetService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.service.BatteryService;
import com.csjbot.blackgaga.service.HomeService;
import com.csjbot.blackgaga.service.LocationService;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.coshandler.listener.OnMapListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.iflytek.cloud.SpeechError;

/**
 * Created by jingwc on 2018/3/28.
 */

public class BaseHomeActivity extends BaseFullScreenActivity{

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    protected boolean isLoadMapSuccess;

    protected boolean isFinish = false;

    protected boolean isPause = false;

    protected IChassis mChassis;

    protected OnMapListener mMapListener;

    NaviTextHandler.PointMatchListener mPointMatchListener = new NaviTextHandler.PointMatchListener() {
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


    NaviAction.NaviActionListener mActionListener = new NaviAction.NaviActionListener() {
        @Override
        public void arrived(NaviBean current) {
            if (isPause)
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
                        startActivity(new Intent(BaseHomeActivity.this, NaviGuideCommentActivity.class));
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
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        RobotManager.getInstance().addListener(mMapListener);
        isLoadMapSuccess = SharedPreUtil.getBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, false);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setBackVisibility(View.INVISIBLE);

        mChassis = ServerFactory.getChassisInstance();

        RobotManager.getInstance().addListener(new OnMapListener() {
            @Override
            public void saveMap(boolean state) {

            }

            @Override
            public void loadMap(boolean state) {
                restoreMapResult(state);
            }
        });

        readLocalLanguage();
        startServices();
        loadData();

        NaviAction.getInstance().registerActionListener(mActionListener);
        NaviAction.getInstance().setSpeaker(mSpeak);
        NaviTextHandler.listener = mPointMatchListener;


        mMapListener = new OnMapListener() {
            @Override
            public void saveMap(boolean state) {
            }

            @Override
            public void loadMap(boolean state) {
                restoreMapResult(state);
            }
        };

        initProductKeyWord();
    }

    private void initProductKeyWord() {
        Constants.ProductKeyWord.initKeywords();
    }

    private void restoreMapResult(boolean isState) {
        runOnUiThread(() -> {
            isFinish = true;
            if (isState) {
                isLoadMapSuccess = true;
                SharedPreUtil.putBoolean(SharedKey.ISLOADMAP, SharedKey.ISLOADMAP, true);
                Toast.makeText(BaseHomeActivity.this, R.string.restore_map_success, Toast.LENGTH_LONG).show();
                jumpActivity(NaviActivity.class);
            } else {
                Toast.makeText(BaseHomeActivity.this, R.string.restore_map_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 启动所有服务
     */
    private void startServices() {

        HomeService.IS_SWITCH_LANGUAGE = true;
        // 控制底层连接Service
        startService(new Intent(this, HomeService.class));

        // 检查网络Service
        startService(new Intent(this, CheckEthernetService.class));

        // 定位Service
        startService(new Intent(this, LocationService.class));

        // JPush长连接消息Service
//        startService(new Intent(this, GetMessageService.class));

        // 电量显示Service
        new Handler().postDelayed(() ->
                startService(new Intent(BaseHomeActivity.this, BatteryService.class))
                , 1000);
    }

    /**
     * 读取本地语言类型
     */
    private void readLocalLanguage() {
        Constants.Language.CURRENT_LANGUAGE =
                SharedPreUtil.
                        getInt(SharedKey.LANGUAGEMODE_NAME
                                , SharedKey.LANGUAGEMODE_KEY
                                , Constants.Language.CHINESE);
    }

    private void loadData() {
        ProductProxy proxy = com.csjbot.blackgaga.model.http.factory.ServerFactory.createProduct();
        proxy.removeMenuList();
        proxy.getRobotMenuList(true);
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

    public void goNaviAct() {
        if (!isLoadMapSuccess) {
            restoreMapDialog();
        } else {
            jumpActivity(NaviActivity.class);
        }
    }

    /* 恢复地图对话框 */
    private void restoreMapDialog() {
        if (RobotManager.getInstance().getConnectState() && !isLoadMapSuccess) {
            showNewRetailDialog(getString(R.string.map_manager), getString(R.string.is_restore_map), new NewRetailDialog.OnDialogClickListener() {
                @Override
                public void yes() {
                    mChassis.loadMap();
                    isFinish = false;
                    checkConnect();
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

    private void checkConnect() {
        new Handler().postDelayed(() -> {
            runOnUiThread(() -> {
                if (!isFinish) {
                    Toast.makeText(BaseHomeActivity.this, R.string.check_linux, Toast.LENGTH_SHORT).show();
                }
            });
        }, Constants.internalCheckLinux);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
