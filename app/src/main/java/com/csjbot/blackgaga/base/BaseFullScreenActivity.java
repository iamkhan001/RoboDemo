package com.csjbot.blackgaga.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.BaseAI;
import com.csjbot.blackgaga.bean.ExpressionControlBean;
import com.csjbot.blackgaga.bean.MusicBean;
import com.csjbot.blackgaga.bean.NewsBean;
import com.csjbot.blackgaga.bean.OnUpdateAppEvent;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.dance.DanceActivity;
import com.csjbot.blackgaga.feature.music.MusicActivity;
import com.csjbot.blackgaga.feature.news.NewsActivity;
import com.csjbot.blackgaga.feature.product.productDetail.ProductDetailActivity;
import com.csjbot.blackgaga.feature.product.shopcart.ShoppingCartActivity;
import com.csjbot.blackgaga.feature.settings.SettingsActivity;
import com.csjbot.blackgaga.feature.settings.network.SettingsNetworkActivity;
import com.csjbot.blackgaga.feature.story.StoryActivity;
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
import com.csjbot.blackgaga.home.QiCheHomeActivity;
import com.csjbot.blackgaga.home.QiCheZhanHomeActivity;
import com.csjbot.blackgaga.home.QianTaiHomeActivity;
import com.csjbot.blackgaga.home.ShangShaHomeActivity;
import com.csjbot.blackgaga.home.ShiPinHomeActivity;
import com.csjbot.blackgaga.home.ShouJiHomeActivity;
import com.csjbot.blackgaga.home.ShuiWuHomeActivity3;
import com.csjbot.blackgaga.home.VerticalScreenHomeActivity;
import com.csjbot.blackgaga.home.XianYangAirportHomeActivity;
import com.csjbot.blackgaga.home.XieDianHomeActivity;
import com.csjbot.blackgaga.home.YanJingHomeActivity;
import com.csjbot.blackgaga.home.YaoDianHomeActivity;
import com.csjbot.blackgaga.home.YiYuanHomeActivity;
import com.csjbot.blackgaga.home.YinHangHomeActivity;
import com.csjbot.blackgaga.home.ZhanGuanHomeActivity;
import com.csjbot.blackgaga.model.http.bean.CustomerServiceBean;
import com.csjbot.blackgaga.model.http.bean.LogoBean;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.workstream.Logo;
import com.csjbot.blackgaga.model.tcp.bean.Position;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.network.NetworkConstants;
import com.csjbot.blackgaga.p2pcamera.CustomerService;
import com.csjbot.blackgaga.p2pcamera.P2PCameraComm;
import com.csjbot.blackgaga.p2pcamera.broadcast.LiveBroadcast;
import com.csjbot.blackgaga.p2pcamera.service.LiveService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.speechrule.ChatControl;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.EnglishChatHandle;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.GsonUtils;
import com.csjbot.blackgaga.util.NumberUtils;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShopcartUtil;
import com.csjbot.blackgaga.util.VolumeUtil;
import com.csjbot.blackgaga.widget.ChatView;
import com.csjbot.blackgaga.widget.LanguageWindow;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.blackgaga.widget.TitleView;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.NTFConstants;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiasuhuei321 on 2017/8/11.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public abstract class BaseFullScreenActivity extends BaseActivity {

    protected TitleView mTitleView;

    private ChatView mChatView;
    protected int mMode;
    protected ISpeak mSpeak;
    protected int temp = 0;
    private BaseAI mBaseAI;
    protected Activity context;


    private NetworkReceiver receiver;

    boolean isResume = false;

    private AlertDialog dialog;

    private NewRetailDialog mDialog;

    private CsjlogProxy mLogProxy;

    private ChatControl mChatControl;

    protected boolean openNoNetworkDialog = true;

    protected volatile String userText;

    protected volatile boolean isDiscard;

    private LiveBroadcast mLiveBroadCast = null;

    private Intent mIntent = null;

    protected CustomerService mCustomerService = null;

    protected volatile boolean isCustomerService;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        context = this;
        //隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        beforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        init();
        afterViewCreated(savedInstanceState);
//        initP2PService();
    }

    protected void beforeSetContentView() {

    }

    ;

    private void initP2PService() {
//        String SN = ProductProxy.SN;
//        String SN = "040010100002";
//         String SN = "040019100002";
//        String SN = "040020100002";
        String SN = "040021100002";

        com.csjbot.blackgaga.model.http.factory.ServerFactory.createCustomerService().getCustomerServiceInfo(SN, new Observer<CustomerServiceBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull CustomerServiceBean customerServiceBean) {
                if (customerServiceBean != null && customerServiceBean.getResult() != null) {
                    P2PCameraComm.ConnectionInfo.USER = customerServiceBean.getResult().getAccount();
                    P2PCameraComm.ConnectionInfo.PASSWD = customerServiceBean.getResult().getPasswd();
                    P2PCameraComm.ConnectionInfo.RELAYADDR = customerServiceBean.getResult().getRelayAddr();
                    P2PCameraComm.ConnectionInfo.SERVER_ADDRESS = customerServiceBean.getResult().getServerAddr();
                    P2PCameraComm.ConnectionInfo.ENABLED = customerServiceBean.getResult().isEnabled();

                    CsjlogProxy.getInstance().info("USER:" + P2PCameraComm.ConnectionInfo.USER);
                    CsjlogProxy.getInstance().info("SERVER_ADDRESS:" + P2PCameraComm.ConnectionInfo.SERVER_ADDRESS);
                    CsjlogProxy.getInstance().info("ENABLED:" + P2PCameraComm.ConnectionInfo.ENABLED);

                    if (mLiveBroadCast == null) {
                        mLiveBroadCast = new LiveBroadcast();
                        mCustomerService = new CustomerService();
                        IntentFilter mFilter = new IntentFilter();
                        mFilter.addAction(P2PCameraComm.ACTION_VIDEO_STATUS);
                        mFilter.addAction(P2PCameraComm.ACTION_NOTIFY);
                        mFilter.addAction(P2PCameraComm.ACTION_RENDER_JOIN);
                        mFilter.addAction(P2PCameraComm.ACTION_RENDER_LEAVE);
                        mFilter.addAction(P2PCameraComm.ACTION_MESSAGE);
                        mFilter.addAction(P2PCameraComm.ACTION_LOGIN);
                        mFilter.addAction(P2PCameraComm.ACTION_LOGIN1);
                        mFilter.addAction(P2PCameraComm.ACTION_LOGIN0);
                        mFilter.addAction(P2PCameraComm.ACTION_LOGOUT);
                        mFilter.addAction(P2PCameraComm.ACTION_CONNECT);
                        mFilter.addAction(P2PCameraComm.ACTION_DISCONNECT);
                        mFilter.addAction(P2PCameraComm.ACTION_OFFLINE);
                        mFilter.addAction(P2PCameraComm.ACTION_LAN_SCAN_RESULT);
                        mFilter.addAction(P2PCameraComm.ACTION_FORWARD_ALLOC_REPLY);
                        mFilter.addAction(P2PCameraComm.ACTION_FORWARD_FREE_REPLY);
                        mFilter.addAction(P2PCameraComm.ACTION_VIDEO_CAMERA);
                        mFilter.addAction(P2PCameraComm.ACTION_SVR_NOTIFY);
                        registerReceiver(mLiveBroadCast, mFilter);
                        mLiveBroadCast.setListener((String title, String message) -> {
                            CsjlogProxy.getInstance().info("title:" + title);
                            CsjlogProxy.getInstance().info("message:" + message);

                            if (title.equals("Message")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(message);
                                    String msgId = jsonObject.getString("msg_id");
                                    switch (msgId) {
                                        case P2PCameraComm.SEND_TEXT_MSG:
                                            String text = jsonObject.getString("text");
                                            runOnUiThread(() -> {
                                                if (!mSpeak.isSpeaking()) {
                                                    speak(text);
                                                    setRobotChatMsg(text);
                                                }
                                            });
                                            break;

                                        case NTFConstants.SPEECH_READ_STOP_NTF:
                                            mSpeak.stopSpeaking();
                                            break;
                                        case NTFConstants.ROBOT_COMPLEX_ACTION_NTF:
                                        case NTFConstants.ROBOT_SET_VOLUME_NTF:
                                            Intent intent = new Intent("CustomerHelpService");
                                            intent.putExtra("json", message);
                                            sendBroadcast(intent);
                                            break;
                                        default:
                                            RobotManager.getInstance().transparentTransmission(message);
                                            break;
                                    }

                                    if (msgId.equals(P2PCameraComm.SEND_TEXT_MSG)) {
//                                        String text = jsonObject.getString("text");
//                                        runOnUiThread(() -> {
//                                            if (!mSpeak.isSpeaking()) {
//                                                speak(text);
//                                                setRobotChatMsg(text);
//                                            }
//                                        });

                                    } else if (msgId.equals(P2PCameraComm.REQ_HUMAN_INTERVENTI)) {
                                        isCustomerService = true;
//                            mTitleView.setCustomerServiceImage(R.drawable.head_customerservice_btn2);
                                    } else if (msgId.equals(P2PCameraComm.REQ_HANGUP_HUMAN_INTERVENTI)) {
                                        isCustomerService = false;
//                            mTitleView.setCustomerServiceImage(R.drawable.head_customerservice_btn);
                                    } else if (msgId.equals(P2PCameraComm.RESP_HUMAN_SERVICE)) {
                                        String msg = jsonObject.getString("message");
                                        if (!TextUtils.isEmpty(msg) && msg.equals("ok")) {
//                                mTitleView.setCustomerServiceImage(R.drawable.head_customerservice_btn2);
                                            isCustomerService = true;
                                        } else {
//                                mTitleView.setCustomerServiceImage(R.drawable.head_customerservice_btn);
                                            speak(R.string.connect_customer_failed);
                                            isCustomerService = false;
                                        }
                                    } else {
                                        RobotManager.getInstance().transparentTransmission(message);
                                    }
                                } catch (JSONException e) {
                                    CsjlogProxy.getInstance().error(e.toString());
                                }
                            }
                        });
                    }

                    if (mIntent == null) {
                        mIntent = new Intent(context, LiveService.class);
                        startService(mIntent);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CsjlogProxy.getInstance().info("onError:e" + e.toString());
            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        addSpeechListener();
        isResume = true;
//        updateLogo();
        refreshLogo();
    }


    public void unRegLiveBroadCast() {
        if (mLiveBroadCast != null) {
            unregisterReceiver(mLiveBroadCast);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(updateLogoReceiver);
        EventBus.getDefault().unregister(this);
    }

    public abstract int getLayoutId();

    protected int getCorrectLayoutId(@LayoutRes int defaultLayoutId, @LayoutRes int verticalLayoutId) {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? verticalLayoutId
                : defaultLayoutId;
    }

    /**
     * 是否启用聊天窗口
     *
     * @return
     */
    public boolean isOpenChat() {
        return false;
    }

    /**
     * 是否启用Title
     *
     * @return
     */
    public boolean isOpenTitle() {
        return false;
    }

    /**
     * 不强制要求子类实现这个方法，你可以不初始化
     */
    public void init() {
        initTitle();
        initChat();
        initSpeak();
//        downLoadLogo();

        mBaseAI = BaseAI.newInstance();
        mBaseAI.initAI(this);
        receiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter(NetworkConstants.SEND_ACTION);
        registerReceiver(receiver, intentFilter);
        IntentFilter logoFilter = new IntentFilter(Constants.UPDATE_LOGO);
        registerReceiver(updateLogoReceiver, logoFilter);

        initWelcomeSpeakText();

        initDialog();

        if (mLogProxy == null) {
            mLogProxy = CsjlogProxy.getInstance();
            mLogProxy.initLog(getApplicationContext());
        }

        mChatControl = ChatControl.newInstance(this);
        String initText = mChatControl.initChild();
        if (!TextUtils.isEmpty(initText)) {
            prattle(initText);
        }

//        if (ping(null)) {
//            mHandlerNetWord.sendEmptyMessage(1001);
//        } else {
//            mHandlerNetWord.sendEmptyMessage(1002);
//        }
    }

    public ChatControl getChatControl() {
        return mChatControl;
    }

    public CsjlogProxy getLog() {
        return mLogProxy;
    }

    private void initDialog() {
        mDialog = new NewRetailDialog(this);
    }

    protected void showNewRetailDialog(String title, String text, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(int title, int text, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(String title, String text, String yes, String no, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setYes(yes);
            mDialog.setNo(no);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void showNewRetailDialog(int title, int text, int yes, int no, NewRetailDialog.OnDialogClickListener listener) {
        if (mDialog != null) {
            mDialog.setTitle(title);
            mDialog.setContent(text);
            mDialog.setYes(yes);
            mDialog.setNo(no);
            mDialog.setListener(listener);
            mDialog.show();
        }
    }

    protected void dismissNewRetailDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    void initWelcomeSpeakText() {
        CharSequence text = "";
        switch (Constants.Language.CURRENT_LANGUAGE) {
            case Constants.Language.CHINESE:
                text = initChineseSpeakText();
                break;
            case Constants.Language.ENGELISH_US:
                text = initEnglishSpeakText();
                break;
            case Constants.Language.JAPANESE:
                text = initJapanSpeakText();
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        speak(text.toString());
        setRobotChatMsg(text);
    }

    protected CharSequence initChineseSpeakText() {
        return null;
    }

    protected CharSequence initEnglishSpeakText() {
        return null;
    }

    protected CharSequence initJapanSpeakText() {
        return null;
    }

    protected boolean isDisableEntertainment() {
        return false;
    }

    protected boolean isHideUserText() {
        return false;
    }


    /**
     * 由子类重写该方法,主要用于关键字定向打断(比如产品关键字可打断对话)
     *
     * @param text
     * @return
     */
    protected boolean keywordInterrupt(String text) {

        return false;
    }

    /**
     * 添加语音监听事件
     */
    private void addSpeechListener() {
        RobotManager.getInstance().addListener((json, type) -> {
            switch (type) {
                case Robot.SPEECH_ASR_ONLY:
                    try {
                        String text = new JSONObject(json).getString("text");

                        if (Constants.Language.isEnglish()) {
                            englishHandle(text);
                        } else if (Constants.Language.isChinese() || Constants.Language.isJapanese()) {
                            userText = text;
                            if (isInterruptSpeech()) {
                                isDiscard = false;
                                runOnUiThread(() -> {
                                    if (!isHideUserText()) {

                                        if (mCustomerService != null) {
                                            mCustomerService.uploadChatMsg(userText, ProductProxy.SN, CustomerService.CUSTOMER);
                                        }

                                        setCustomerChatMsg(text);
                                    }
                                });
                            } else {
                                if (!mSpeak.isSpeaking()) {
                                    isDiscard = false;
                                    runOnUiThread(() -> {
                                        if (!isHideUserText()) {

                                            if (mCustomerService != null) {
                                                mCustomerService.uploadChatMsg(userText, ProductProxy.SN, CustomerService.CUSTOMER);
                                            }

                                            setCustomerChatMsg(text);
                                        }
                                    });
                                } else {
                                    if (keywordInterrupt(text)) {
                                        isDiscard = false;
                                    } else {
                                        isDiscard = true;
                                    }

                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Robot.SPEECH_LAST_RESULT:
                    try {
                        if (isDiscard) {
                            return;
                        }

                        if (Constants.Language.isEnglish()) {
                            return;
                        }

                        JSONObject result = new JSONObject(json).getJSONObject("result");
                        String text = result.getString("text");

                        String service = "";
                        try {
                            service = result.getJSONObject("data").getString("service");
                        } catch (Exception e) {
                        }


                        String answerText = "";

                        try {
                            answerText = result.getJSONObject("data").getJSONObject("data").getString("answer");
                        } catch (Exception e) {
                            CsjlogProxy.getInstance().debug("answerText not in answer");
                            e.printStackTrace();
                            try {
                                answerText = result.getJSONObject("data").getString("message");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                CsjlogProxy.getInstance().debug("answerText not in message");
                            }
                        }

                        CsjlogProxy.getInstance().info("service:" + service);


                        if (answerText.contains("#$#$")) {
                            String replaceName;

                            if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                                replaceName = "小雪";
                            } else if (BuildConfig.robotType.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
                                replaceName = "艾米";
                            } else {
                                replaceName = "爱丽丝";
                            }

                            answerText = answerText.replace("#$#$", replaceName);
                            CsjlogProxy.getInstance().info("answerText:" + answerText);
                        }
                        String filterText = answerText;

                        if (service.equals("CHAT")) {
                            runOnUiThread(() -> onSpeechMessage(text, filterText));
                        } else if (service.equals(MusicBean.service)) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data")
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(MusicActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else if (service.equals(NewsBean.service)) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data")
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(NewsActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else if (service.equals("STORY")) {
                            if (isDisableEntertainment()) {
                                return;
                            }
                            String data = result
                                    .getJSONObject("data").toString();
                            Bundle bundle = new Bundle();
                            bundle.putString("data", data);
                            if (this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())) {
                                runOnUiThread(() -> onShowMessage(data));
                            } else {
                                jumpActivity(StoryActivity.class, bundle);
                                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                                        || this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                                    this.finish();
                                }
                            }
                        } else {
                            runOnUiThread(() -> onSpeechMessage(text, filterText));
                        }
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().info("json解析出错");
                    }
                    break;
                case Robot.SPEECH_CUSTOMER_SERVICE:
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                        String msgId = jsonObject.getString("msg_id");
                        String text = jsonObject.getString("text");
                        runOnUiThread(() -> {

                            if (!mSpeak.isSpeaking()) {
                                speak(text);
                                setRobotChatMsg(text);
                            }
                        });
                    } catch (JSONException e) {
                        CsjlogProxy.getInstance().error("SPEECH_CUSTOMER_SERVICE " + e.toString());
                    }
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 语音是否可被打断
     *
     * @return
     */
    protected boolean isInterruptSpeech() {
        return false;
    }

    protected void onShowMessage(String data) {

    }

    public void setRobotChatMsg(CharSequence text) {
        if (getChatView() != null && (!TextUtils.isEmpty(text))) {
            getChatView().addChatMsg(ChatView.ChatMsgType.ROBOT_MSG, text);
        }
    }

    public void setRobotChatMsg(@StringRes int stringId) {
        if (getChatView() != null) {
            getChatView().addChatMsg(ChatView.ChatMsgType.ROBOT_MSG, getString(stringId));
        }
    }

    public void setCustomerChatMsg(CharSequence text) {
        if (getChatView() != null && (!TextUtils.isEmpty(text))) {
            getChatView().addChatMsg(ChatView.ChatMsgType.CUSTOMER_MSG, text);
        }
    }

    public void setCustomerChatMsg(@StringRes int stringId) {
        if (getChatView() != null) {
            getChatView().addChatMsg(ChatView.ChatMsgType.CUSTOMER_MSG, getString(stringId));
        }
    }

    protected void englishHandle(String text) {
        if (!mSpeak.isSpeaking()) {
            runOnUiThread(() -> {
                setCustomerChatMsg(text);
            });

        }
        String answer = EnglishChatHandle.newInstance().getAnswer(text);
        speak(answer);
        runOnUiThread(() -> setRobotChatMsg(answer));

    }

    private boolean volumeIntercept(String text) {
        if (text.contains(getString(R.string.noise_down))
                || text.contains("音量小一点")
                || text.contains("小一点声音")
                || text.contains("小点声音")
                || text.contains("小点声")) {
            // 调小音量
            ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                    .adjustStreamVolume(
                            AudioManager.STREAM_MUSIC
                            , AudioManager.ADJUST_LOWER
                            , AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        } else if (text.contains(getString(R.string.noise_up))
                || text.contains("音量大一点")
                || text.contains("大一点声音")
                || text.contains("大一点音量")
                || text.contains("大点声")
                || text.contains("大点声音")) {
            // 调大音量
            ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                    .adjustStreamVolume(
                            AudioManager.STREAM_MUSIC
                            , AudioManager.ADJUST_RAISE
                            , AudioManager.FX_FOCUS_NAVIGATION_UP);
            return true;
        }
        return false;
    }

    private boolean danceIntercept(String text) {
        if (text.contains(getString(R.string.dance)) || text.contains(getString(R.string.dance1))
                || text.contains(getString(R.string.dance2)) || text.contains(getString(R.string.dance3))) {
            if (!this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
                jumpActivity(DanceActivity.class);
                if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                        || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())) {
                    this.finish();
                }
                return true;
            }
        }
        return false;
    }

    private boolean productIntercept(String text) {
        String className = this.getClass().getName();
        if (className.equals(ChaoShiHomeActivity.class.getName())
                || className.equals(ChaoShiHomeActivity.class.getName())
                || className.equals(CSJBotHomeActivity.class.getName())
                || className.equals(FuZhuangHomeActivity.class.getName())
                || className.equals(HomeActivity.class.getName())
                || className.equals(JiaDianHomeActivity.class.getName())
                || className.equals(JiChangHomeActivity.class.getName())
                || className.equals(YinHangHomeActivity.class.getName())
                || className.equals(QiCheHomeActivity.class.getName())
                || className.equals(ShangShaHomeActivity.class.getName())
                || className.equals(ShiPinHomeActivity.class.getName())
                || className.equals(ShuiWuHomeActivity3.class.getName())
                || className.equals(YaoDianHomeActivity.class.getName())
                || className.equals(JiuDianHomeActivity.class.getName())
                || className.equals(JiaoYu2HomeActivity.class.getName())
                || className.equals(ZhanGuanHomeActivity.class.getName())
                || className.equals(YiYuanHomeActivity.class.getName())) {
            if (Constants.ProductKeyWord.products.size() == 0) {
                Constants.ProductKeyWord.initKeywords();
            }
            String intentRegEx = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"].*";
            String intentRegEx2 = ".*我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
            if (text.matches(intentRegEx)) {
                String regex = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]+[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex2 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex3 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"].*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                String regex4 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"]";
                for (RobotSpListBean.ResultBean.ProductBean product : Constants.ProductKeyWord.products) {
                    if (text.matches((regex + product.getName() + "+[。|.]"))) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        int number = NumberUtils.getNumber(text);
                        getLog().info("number:" + number);
                        if (number > 0) {
                            bundle.putInt("number", number);
                        }
                        jumpActivity(ProductDetailActivity.class, bundle);
                        return true;
                    } else if (text.matches((regex2 + product.getName() + "+[。|.]"))) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        jumpActivity(ProductDetailActivity.class, bundle);
                        return true;
                    } else if (text.matches(regex3 + product.getName() + "+[。|.]")) {
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        int number = NumberUtils.numberParser(text);
                        getLog().info("number2:" + number);
                        if (number > 0) {
                            bundle.putInt("number", number);
                        }
                        jumpActivity(ProductDetailActivity.class, bundle);
                        return true;
                    } else if (text.matches(regex4 + product.getName() + "+[。|.]")) {
                        getLog().error(text.matches(regex4 + product.getName() + "+[。|.]") + "");
                        getLog().error((regex4 + product.getName() + "+[。|.]"));
                        String gson = new Gson().toJson(product);
                        Bundle bundle = new Bundle();
                        bundle.putString("productBean", gson);
                        jumpActivity(ProductDetailActivity.class, bundle);
                        return true;
                    } else {
                        if (text.contains(product.getName())) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            jumpActivity(ProductDetailActivity.class, bundle);
                            return true;
                        }
                    }
                }
            } else if (text.matches(intentRegEx2)) {
                String regex5 = "我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                String regex6 = "我要[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                for (RobotSpListBean.ResultBean.ProductBean product : Constants.ProductKeyWord.products) {
                    if (text.matches(regex5)) {
                        String s = new StringBuilder(regex5).insert(0, product.getName()).toString();
                        if (text.matches(s)) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            int number = NumberUtils.numberParser(text);
                            getLog().info("number2:" + number);
                            if (number > 0) {
                                bundle.putInt("number", number);
                            }
                            jumpActivity(ProductDetailActivity.class, bundle);
                            return true;
                        }
                    } else if (text.matches(regex6)) {
                        String s = new StringBuilder(regex6).insert(0, product.getName()).toString();
                        if (text.matches(s)) {
                            String gson = new Gson().toJson(product);
                            Bundle bundle = new Bundle();
                            bundle.putString("productBean", gson);
                            int number = NumberUtils.getNumber(text);
                            getLog().info("number:" + number);
                            if (number > 0) {
                                bundle.putInt("number", number);
                            }
                            jumpActivity(ProductDetailActivity.class, bundle);
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    protected boolean onSpeechMessage(String text, String answerText) {
        if (Constants.Language.isEnglish()) {
            return true;
        }

        if (text.contains(getString(R.string.main)) || text.contains(getString(R.string.homepage))) {
            BRouter.toHome();
            return true;
        }

        expressionControl(text);

        if (isOpenActionControl()) {
            if (actionControl(text)) {
                return true;
            }
        }

        // 音量调整拦截
        if (volumeIntercept(text)) {
            return true;
        }

        if (!isDisableEntertainment()) {
            // 跳舞拦截
            if (danceIntercept(text)) {
                return true;
            }
        }

        if (productIntercept(text)) {
            return true;
        }


        String result = mChatControl.handleGlobal(text);
        CsjlogProxy.getInstance().info("handleGlobal:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }
        result = mChatControl.handleChild(text);
        CsjlogProxy.getInstance().info("handleChild:result:" + result);
        if (!TextUtils.isEmpty(result)) {
            if (!result.equals(ChatControl.ACTION)) {
                prattle(result);
            }
            return true;
        }

        return false;
    }

    protected boolean expressionControl(String text) {
        getLog().info("expressionControl");
        if (Constants.expressionControlBean == null) {
            getLog().info("expressionControl:Constants.expressionControlBean:null");
            String filePath = Constants.Path.EXPRESSION_CONFIG_PATH + Constants.Path.EXPRESSION_CONFIG_FILE_NAME;
            File file = new File(filePath);
            getLog().info("expressionControl:filePath:" + file.exists());
            if (file.exists()) {
                new Thread(() -> {
                    String json = FileUtil.readToStringUTF8(filePath);
                    Constants.expressionControlBean = new Gson().fromJson(json, ExpressionControlBean.class);
                }).start();

            } else {
                String json = "{\n" +
                        "\t\"happy\": [\n" +
                        "\t\t\"好看\", \"漂亮\", \"开心\"\n" +
                        "\t],\n" +
                        "\t\"normal\": [],\n" +
                        "\t\"smile\": [\n" +
                        "\t\t\"你好\", \"微笑\"\n" +
                        "\t],\n" +
                        "\t\"sad\": [\n" +
                        "\t\t\"丑\", \"难看\", \"悲伤\"\n" +
                        "\t],\n" +
                        "\t\"angry\": [\n" +
                        "\t\t\"笨蛋\", \"白痴\", \"神经病\", \"生气\"\n" +
                        "\t],\n" +
                        "\t\"surprise\": [\n" +
                        "\t\t\"惊讶\"\n" +
                        "\t]\n" +
                        "}";
                new Thread(() -> {
                    Constants.expressionControlBean = new Gson().fromJson(json, ExpressionControlBean.class);
                    File pathFile = new File(Constants.Path.EXPRESSION_CONFIG_PATH);
                    if (!pathFile.exists()) {
                        pathFile.mkdirs();
                    }
                    try {
                        file.createNewFile();
                        FileUtil.writeToFile(file, json);
                    } catch (IOException e) {
                        getLog().error("e:" + e.toString());
                    }
                }).start();

            }
        } else {
            for (String angry : Constants.expressionControlBean.getAngry()) {
                getLog().info("angry:" + angry);
                if (text.contains(angry)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.ANGRY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
            for (String happy : Constants.expressionControlBean.getHappy()) {
                getLog().info("happy:" + happy);
                if (text.contains(happy)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.HAPPY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
            for (String normal : Constants.expressionControlBean.getNormal()) {
                getLog().info("normal:" + normal);
                if (text.contains(normal)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.NORMAL
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
            for (String sad : Constants.expressionControlBean.getSad()) {
                getLog().info("sad:" + sad);
                if (text.contains(sad)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SADNESS
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
            for (String smile : Constants.expressionControlBean.getSmile()) {
                getLog().info("smile:" + smile);
                if (text.contains(smile)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SMILE
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
            for (String surise : Constants.expressionControlBean.getSurprise()) {
                getLog().info("surise:" + surise);
                if (text.contains(surise)) {
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SURPRISED
                                    , REQConstants.Expression.NO
                                    , 2000);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否开启动作控制命令
     *
     * @return
     */
    protected boolean isOpenActionControl() {
        return true;
    }

    protected boolean actionControl(String text) {

        String forwardRegEx = "[\"前进\"|\"前进一步\"|\"往前走\"|\"往前走一步\"|\"向前走\"|\"向前走一步\"|\"向前走一下\"|\"往前走一下\"]+[。|.]";
        String backwardRegEx = "[\"后退\"|\"后退一步\"|\"往后退\"|\"往后退一步\"|\"向后退\"|\"向后退一步\"|\"向后退一下\"|\"往后退一下\"]+[。|.]";

        String leftRegEx = "[\"左转\"|\"往左转\"|\"向左转\"]+[。|.]";
        String leftDegreeRegEx = "[\"左转\"|\"往左转\"|\"向左转\"]+[0-9]+度+[。|.]";
        String leftDegreeRegEx2 = "[\"左转\"|\"往左转\"|\"向左转\"].*度+[。|.]";

        String rightRegEx = "[\"右转\"|\"往右转\"|\"向右转\"]+[。|.]";
        String rightDegreeRegEx = "[\"右转\"|\"往右转\"|\"向右转\"]+[0-9]+度+[。|.]";
        String rightDegreeRegEx2 = "[\"右转\"|\"往右转\"|\"向右转\"].*度+[。|.]";

        if (text.matches(forwardRegEx)) {
            getLog().info("前进---------");
            new Thread(() -> {
                int i = 0;
                while (i < 5) {
                    ServerFactory.getChassisInstance().moveForward();
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (text.matches(backwardRegEx)) {
            getLog().info("后退---------");
            new Thread(() -> {
                int i = 0;
                while (i < 5) {
                    ServerFactory.getChassisInstance().moveBack();
                    i++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (text.contains("左转") || text.contains("往左转") || text.contains("向左转")) {
            getLog().info("左转意图---------");
            if (text.matches(leftRegEx)) {
                getLog().info("左转---------");
                ServerFactory.getChassisInstance().turnLeft();
            } else if (text.matches(leftDegreeRegEx)) {
                int degree = NumberUtils.getNumber(text);
                if (degree > 360) {
                    degree = 360;
                }
                getLog().info("左转" + degree + "度---------1");
                RobotManager.getInstance().robot.reqProxy.moveAngle(degree);
            } else if (text.matches(leftDegreeRegEx2)) {
                int degree = NumberUtils.numberParser(text);
                if (degree > 360) {
                    degree = 360;
                }
                getLog().info("左转" + degree + "度---------2");
                RobotManager.getInstance().robot.reqProxy.moveAngle(degree);
            } else {
                return false;
            }
        } else if (text.contains("右转") || text.contains("往右转") || text.contains("向右转")) {
            getLog().info("右转意图---------");
            if (text.matches(rightRegEx)) {
                getLog().info("右转---------");
                ServerFactory.getChassisInstance().turnRight();
            } else if (text.matches(rightDegreeRegEx)) {
                int degree = NumberUtils.getNumber(text);
                if (degree > 360) {
                    degree = 360;
                }
                getLog().info("右转" + degree + "度---------1");
                RobotManager.getInstance().robot.reqProxy.moveAngle(-degree);
            } else if (text.matches(rightDegreeRegEx2)) {
                int degree = NumberUtils.numberParser(text);
                if (degree > 360) {
                    degree = 360;
                }
                getLog().info("右转" + degree + "度---------2");
                RobotManager.getInstance().robot.reqProxy.moveAngle(-degree);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    protected void prattle(String answerText) {
        if (isCustomerService) {
            return;
        }
        if (!mSpeak.isSpeaking()) {

            if (mCustomerService != null) {
                mCustomerService.uploadChatMsg(userText, ProductProxy.SN, CustomerService.ROBOT);
            }

            speak(answerText);
            setRobotChatMsg(answerText);
        }
    }


    private void initSpeak() {
        mSpeak = ServerFactory.getSpeakInstance();
    }

    private void initChat() {
        if (isOpenChat()) {
            mChatView = (ChatView) findViewById(R.id.chat_view);
        }
    }

    private void initTitle() {
        if (isOpenTitle()) {
            mTitleView = (TitleView) findViewById(R.id.title_view);
            initImage();
            //            mTitleView.setLanguageImage(R.drawable.iv_language_ch);
            mTitleView.setLanguageText(R.string.language_text);
            mTitleView.setSetLanguageListener(() -> {

                new LanguageWindow(BaseFullScreenActivity.this).showAsDropDown(mTitleView.tvLanguage, 30, 10);
            });
            mTitleView.setCustomerServiceListener(() -> {
                if (isCustomerService) {
                    return;
                }
//                mCustomerService.openCustomerService(ProductProxy.SN);
                Robot.getInstance().callHumanService(Robot.SN);
            });
//            /**
//             * 本地测试换肤用
//             */
//            TODO: 2018/2/1 本地测试换肤用
//            mTitleView.setCustomerServiceListener(() -> {
//
//                switch (temp) {
////                    case 5:
////                        SkinCompatManager.getInstance().loadSkin("kejiguan.skins");
////                        jumpActivity(KeJiGuanHomeActivity.class);
////                        CSJToast.showToast(this, "科技馆", 100);
////                        break;
//                    case 8:
//                        SkinCompatManager.getInstance().loadSkin("shouji.skins");
//                        jumpActivity(ShouJiHomeActivity.class);
//                        CSJToast.showToast(this, "手机", 100);
//                        break;
//                    case 4:
//                        SkinCompatManager.getInstance().loadSkin("xiedian.skins");
//                        jumpActivity(XieDianHomeActivity.class);
//                        CSJToast.showToast(this, "鞋店", 100);
//                        break;
//                    case 7:
//                        SkinCompatManager.getInstance().loadSkin("qichezhan.skins");
//                        jumpActivity(QiCheZhanHomeActivity.class);
//                        CSJToast.showToast(this, "汽车站", 100);
//                        break;
//                    case 2:
//                        SkinCompatManager.getInstance().loadSkin("lvyou.skins");
//                        jumpActivity(LvYouHomeActivity.class);
//                        CSJToast.showToast(this, "旅游", 100);
//                        break;
//                    case 1:
//                        SkinCompatManager.getInstance().loadSkin("yanjing.skins");
//                        jumpActivity(YanJingHomeActivity.class);
//                        CSJToast.showToast(this, "眼镜", 100);
//                        break;
//                    case 2:
//                        SkinCompatManager.getInstance().loadSkin("jiaoyu.skins");
//                        jumpActivity(JiaoYu2HomeActivity.class);
//                        CSJToast.showToast(this, "教育", 100);
//                        break;
//                    case 1:
//                        SkinCompatManager.getInstance().loadSkin("qiantai.skins");
//                        jumpActivity(QianTaiHomeActivity.class);
//                        CSJToast.showToast(this, "前台", 100);
//                        break;
//                    case 3:
//                        SkinCompatManager.getInstance().loadSkin("yiyuan.skins");
//                        jumpActivity(YiYuanHomeActivity.class);
//                        CSJToast.showToast(this, "医院", 100);
//                        break;
//                    case 1:
//                        SkinCompatManager.getInstance().loadSkin("jichang.skins");
//                        jumpActivity(XianYangAirportHomeActivity.class);
//                        CSJToast.showToast(this, "咸阳机场", 100);
//                        break;
//                    default:
//                        temp = 0;
//                        break;
//                }
//                temp++;
//            });
            mTitleView.setSettingsListener(() -> jumpActivity(SettingsActivity.class));
            mTitleView.setHomePageListener(() -> {
                BRouter.toHome();
            });

            mTitleView.setBackListener(BaseFullScreenActivity.this::finish);

            mTitleView.setSettingsPageBackListener(BaseFullScreenActivity.this::finish);
        }
    }

    protected void refreshLogo() {
        getLog().info("refreshLogo");
        if (mTitleView != null && isOpenTitle()) {
            getLog().info("refreshLogo setSDLogo");
            mTitleView.setSDLogo(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
        }
    }

    private void initImage() {
        mMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, 1);
        switch (mMode) {
            case 0:
                mTitleView.setLanguageImage(R.drawable.language_en);
                break;
            case 1:
                mTitleView.setLanguageImage(R.drawable.iv_language_ch);
                break;
            case 2:
                mTitleView.setLanguageImage(R.drawable.language_jp);
                break;
            default:
                break;
        }

    }

    /**
     * 获取当前ChatView
     *
     * @return
     */
    public ChatView getChatView() {
        return mChatView;
    }

    /**
     * 获取当前TitleView
     *
     * @return
     */
    public TitleView getTitleView() {
        return mTitleView;
    }

    /**
     * 对于某些特殊的Activity，你可能需要重写这个方法拿到savedInstanceState
     * 来恢复一些状态
     *
     * @param savedInstanceState
     */
    public void afterViewCreated(Bundle savedInstanceState) {
        // 这个方法，不要写东西，因为很多子类都没有调用
        // super.afterViewCreated
    }


    /**
     * 根据传入的class进行activity的跳转
     *
     * @param c
     */
    public void jumpActivity(Class<? extends Activity> c) {
        if (c != null) {
            startActivity(new Intent(this, c));
//            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

        }
    }

    /**
     * 根据传入的class进行activity的跳转  可传入参数
     */
    public void jumpActivity(Class<? extends Activity> c, Bundle bundle) {
        if (c != null) {
            Intent intent = new Intent(this, c);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


    /**
     * 提供说话功能
     *
     * @param text
     * @param listener
     */
    protected void speak(String text, OnSpeakListener listener) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, listener);
        }
    }

    protected void speak(String text, boolean isSetRobotChatMsg) {
        speak(text, null, isSetRobotChatMsg);
    }

    protected void speak(String text, OnSpeakListener listener, boolean isSetRobotChatMsg) {
        speak(text, listener);
        if (isSetRobotChatMsg) {
            setRobotChatMsg(text);
        }
    }

    protected void speak(@StringRes int stringId, OnSpeakListener listener, boolean isSetRobotChatMsg) {
        speak(getString(stringId), listener, isSetRobotChatMsg);
    }

    protected void speak(@StringRes int stringId, OnSpeakListener listener) {
        speak(getString(stringId), listener, false);
    }

    /**
     * 重新dispatchTouchEvent方法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 提供说话功能
     *
     * @param text
     */
    public void speak(String text) {
        if (!TextUtils.isEmpty(text)) {
            mSpeak.startSpeaking(text, null);
        }

    }

    public void speak(@StringRes int stringId) {
        speak(getString(stringId), null, false);
    }

    public void speak(@StringRes int stringId, boolean isSetRobotChatMsg) {
        speak(getString(stringId), null, isSetRobotChatMsg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
        isResume = false;
    }

    public void stopSpeak() {
        if (mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
    }

    /**
     * 给定关键字，文本将高亮标出关键字，如果传入点击监听，在点击相应的关键字时将会调用监听事件。同一个关键字，如 "周边"
     * 全文中的 "周边" 关键字都会被高亮标出，但是只会不论点击哪一个 "周边" 关键字，调用的都是同一个事件。
     *
     * @param talkText 原文字
     * @param keyword  关键字
     * @param color    高亮颜色
     * @return 处理后的字符
     */
    protected CharSequence getColorfulText(String talkText, String keyword, int color) {
        return RichTextUtil.getColorString(talkText, keyword, color);
    }

    /**
     * 给定关键字，文本将高亮标出关键字，如果传入点击监听，在点击相应的关键字时将会调用监听事件。同一个关键字，如 "周边"
     * 全文中的 "周边" 关键字都会被高亮标出，但是只会不论点击哪一个 "周边" 关键字，调用的都是同一个事件。
     *
     * @param talkText 原文字
     * @param keyword  关键字
     * @param color    高亮颜色
     * @param listener 点击监听
     * @return
     */
    protected CharSequence getColorfulText(String talkText, String keyword, int color, View.OnClickListener listener) {
        return RichTextUtil.getColorString(talkText, keyword, color, listener);
    }

    /**
     * 给定关键字，文本将高亮标出关键字，如果传入点击监听，在点击相应的关键字时将会调用监听事件。同一个关键字，如 "周边"
     * 全文中的 "周边" 关键字都会被高亮标出，但是只会不论点击哪一个 "周边" 关键字，调用的都是同一个事件。
     *
     * @param talkText 原文字
     * @param keywords 关键字集合
     * @param colorMap 关键字对应的高亮颜色Map
     * @return
     */
    protected CharSequence getColorfulText(String talkText, List<String> keywords, Map<String, Integer> colorMap) {
        return RichTextUtil.getColorString(talkText, keywords, colorMap);
    }

    /**
     * 给定关键字，文本将高亮标出关键字，如果传入点击监听，在点击相应的关键字时将会调用监听事件。同一个关键字，如 "周边"
     * 全文中的 "周边" 关键字都会被高亮标出，但是只会不论点击哪一个 "周边" 关键字，调用的都是同一个事件。
     *
     * @param talkText    原文字
     * @param keywords    关键字集合
     * @param colorMap    关键字对应的高亮颜色Map
     * @param listenerMap 关键字对应的点击事件Map
     * @return
     */
    protected CharSequence getColorfulText(String talkText, List<String> keywords, Map<String, Integer> colorMap, Map<String, View.OnClickListener> listenerMap) {
        return RichTextUtil.getColorString(talkText, keywords, colorMap, listenerMap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_BACK));
    }

    public void goShoppingCartAct() {
        if (ShopcartUtil.getShopcartNum() > 0) {
            jumpActivity(ShoppingCartActivity.class);
        } else {
            setRobotChatMsg(R.string.not_need_payment);
            speak(R.string.not_need_payment);
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isResume) {
                return;
            }

            int state = intent.getIntExtra(NetworkConstants.NET_WORK_STATE, NetworkConstants.NETWORK_UNAVAILABLE);
            if (state == NetworkConstants.NETWORK_AVAILABLE) {
                BlackgagaLogger.debug("网络可用");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                networkAvailability();
//                downLoadLogo();
//                new LogoServiceImpl().getLogo(ConfInfoUtil.getSN());
            } else {
                networkUnavailable();
                BlackgagaLogger.debug("网络不可用");
                if (isResume) {
                    showNewRetailDialog(getString(R.string.net_work_nouse), getString(R.string.network_unavailable),
                            getString(R.string.sure), getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
                                @Override
                                public void yes() {
                                    startActivity(new Intent(BaseFullScreenActivity.this, SettingsNetworkActivity.class));
                                    dismissNewRetailDialog();
                                }

                                @Override
                                public void no() {
                                    dismissNewRetailDialog();
                                }
                            });
                }
            }
        }
    }

    public void networkAvailability() {
        CsjlogProxy.getInstance().info("-------------->语音识别可用");
        // 重新打开语音识别服务
//        ServerFactory.getSpeechInstance().startIsr();
    }

    public void networkUnavailable() {
        CsjlogProxy.getInstance().info("-------------->语音识别不可用");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                VolumeUtil.addMediaVolume(this);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                VolumeUtil.cutMediaVolume(this);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void downLoadLogo() {
        Logo.getLogo(Robot.SN, new Observer<LogoBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull LogoBean logoBean) {
                Logo.setLogoBean(logoBean);
                if (isOpenTitle()) {
                    if (logoBean != null && logoBean.getResult() != null && logoBean.getResult().size() > 0) {
                        BlackgagaLogger.debug("sunxy后台获取logo");
                        SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOTYPE, "net");
                        Glide.with(getApplicationContext())
                                .asFile()
                                .load(logoBean.getResult().get(0).getLogourl())
                                .listener(new RequestListener<File>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                        BlackgagaLogger.debug("sunxy后台获取logo Failed");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                        BlackgagaLogger.debug("sunxy后台获取logo ResourceReady");
                                        return false;
                                    }
                                })
                                .into(new SimpleTarget<File>() {
                                    @Override
                                    public void onResourceReady(File resource, Transition<? super File> transition) {
                                        File pathFile = new File(Constants.LOGO_PATH);
                                        if (!pathFile.exists()) {
                                            pathFile.mkdirs();
                                        }
                                        Observable.just(FileUtil.copy(resource, new File(Constants.LOGO_PATH + "logo.jpg")))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(b -> {
                                                    if (b) {
                                                        mTitleView.setSDLogo(Constants.LOGO_PATH + "logo.jpg");
                                                        BlackgagaLogger.debug("sunxy后台获取logo 下载成功");
                                                    } else {
                                                        BlackgagaLogger.debug("sunxy后台获取logo 下载失败");
//                                                        mTitleView.setLogo(R.drawable.small_logo);
                                                    }
                                                });
                                    }
                                });
                    } else {
                        BlackgagaLogger.debug("sunxy后台获取logo 本地图片");
                        if (new File(Constants.LOGO_PATH + "logo.jpg").exists()) {
                            new File(Constants.LOGO_PATH + "logo.jpg").delete();
                        }
                        SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOTYPE, "local");
//                        mTitleView.setLogo(R.drawable.small_logo);
                    }
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

    public void updateLogo() {
        if (isOpenTitle()) {
            LogoBean logo = Logo.getLogo();
            if ((SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGOTYPE, "local").equals("net") && new File(Constants.LOGO_PATH + "logo.jpg").exists())
                    || (logo != null && new File(Constants.LOGO_PATH).exists())) {
                BlackgagaLogger.debug("sunxy后台获取logo net图片");
                mTitleView.setSDLogo(Constants.LOGO_PATH + "logo.jpg");
            } else {
                BlackgagaLogger.debug("sunxy后台获取logo 本地图片");
//                mTitleView.setLogo(R.drawable.small_logo);
            }
        }
    }

    private BroadcastReceiver updateLogoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            updateLogo();
            refreshLogo();
        }
    };

    protected Handler welcomeHandler = new Handler();
    protected Runnable welcomeRb = () -> {
        try {
            String name = getTaskTopAct();
            if (name.equals(JiuDianHomeActivity.class.getName())
                    || name.equals(ChaoShiHomeActivity.class.getName())
                    || name.equals(CSJBotHomeActivity.class.getName())
                    || name.equals(FuZhuangHomeActivity.class.getName())
                    || name.equals(HomeActivity.class.getName())
                    || name.equals(JiaDianHomeActivity.class.getName())
                    || name.equals(JiaoYu2HomeActivity.class.getName())
                    || name.equals(JiChangHomeActivity.class.getName())
                    || name.equals(JiuDianHomeActivity.class.getName())
                    || name.equals(QiCheHomeActivity.class.getName())
                    || name.equals(ShangShaHomeActivity.class.getName())
                    || name.equals(ShiPinHomeActivity.class.getName())
                    || name.equals(ShuiWuHomeActivity3.class.getName())
                    || name.equals(YaoDianHomeActivity.class.getName())
                    || name.equals(YinHangHomeActivity.class.getName())
                    ) {
                if (SharedPreUtil.getBoolean(SharedKey.YINGBINGSETTING, SharedKey.ISACTIVE, false)) {
                    //获取迎宾点
                    String j = SharedPreUtil.getString(SharedKey.YINGBIN_NAME, SharedKey.YINGBIN_KEY);
                    List<Position> positionList = GsonUtils.jsonToObject(j, new TypeToken<List<Position>>() {
                    }.getType());
                    if (positionList == null || positionList.size() <= 0) {
                        new Thread(() -> {
                            ServerFactory.getChassisInstance().moveBack();
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ServerFactory.getChassisInstance().moveBack();
                        }).start();
                        return;
                    }
                    RobotManager.getInstance().robot.reqProxy.navi(positionList.get(0).toJson());
                }
            }
        } catch (Exception e) {
        }

    };


    protected String getTaskTopAct() throws Exception {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> cn = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo taskInfo = cn.get(0);
        ComponentName name = taskInfo.topActivity;
        Activity foregroundActivity = (Activity) (Class.forName(name.getClassName()).newInstance());
        return foregroundActivity.getClass().getName();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void isUpdateApp(OnUpdateAppEvent event) {

        if (event.isUpdateApp()) {
            String className = this.getClass().getName();
            if (className.equals(ChaoShiHomeActivity.class.getName())
                    || className.equals(VerticalScreenHomeActivity.class.getName())
                    || className.equals(CheZhanHomeActivity.class.getName())
                    || className.equals(CSJBotHomeActivity.class.getName())
                    || className.equals(FuZhuangHomeActivity.class.getName())
                    || className.equals(HomeActivity.class.getName())
                    || className.equals(JiaDianHomeActivity.class.getName())
                    || className.equals(JiaJuHomeActivity.class.getName())
                    || className.equals(JiaoYu2HomeActivity.class.getName())
                    || className.equals(JiChangHomeActivity.class.getName())
                    || className.equals(JiuDianHomeActivity.class.getName())
                    || className.equals(KeJiGuanHomeActivity.class.getName())
                    || className.equals(LvYouHomeActivity.class.getName())
                    || className.equals(QianTaiHomeActivity.class.getName())
                    || className.equals(QiCheHomeActivity.class.getName())
                    || className.equals(QiCheZhanHomeActivity.class.getName())
                    || className.equals(ShangShaHomeActivity.class.getName())
                    || className.equals(ShiPinHomeActivity.class.getName())
                    || className.equals(ShouJiHomeActivity.class.getName())
                    || className.equals(ShuiWuHomeActivity3.class.getName())
                    || className.equals(XianYangAirportHomeActivity.class.getName())
                    || className.equals(XieDianHomeActivity.class.getName())
                    || className.equals(YanJingHomeActivity.class.getName())
                    || className.equals(YaoDianHomeActivity.class.getName())
                    || className.equals(YinHangHomeActivity.class.getName())
                    || className.equals(YiYuanHomeActivity.class.getName())
                    || className.equals(AdminHomeActivity.class.getName())
                    || className.equals(CheGuanSuoHomeActivity.class.getName())
                    || className.equals(ZhanGuanHomeActivity.class.getName())) {
                Log.e("检查App更新", "当前网络可用: ");
                //检查程序更新

                if (!mDialog.isShowing()) {
                    showNewRetailDialog(getString(R.string.net_work_nouse), getString(R.string.dialog_update_app),
                            getString(R.string.sure), getString(R.string.cancel), new NewRetailDialog.OnDialogClickListener() {
                                @Override
                                public void yes() {
                                    startActivity(new Intent(BaseFullScreenActivity.this, SettingsActivity.class));
                                    SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", true);
                                    dismissNewRetailDialog();
                                }

                                @Override
                                public void no() {
                                    SharedPreUtil.putBoolean(SharedKey.UPDATE_APP, "check_app", true);
                                    dismissNewRetailDialog();
                                }
                            });
                }

            }
        }
    }

    public void hideVideo() {
        if (!Constants.ChangeLan) {
            sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
        }
        Constants.ChangeLan = false;
    }
}
