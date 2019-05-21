package com.csjbot.blackgaga.base.test;

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
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseActivity;
import com.csjbot.blackgaga.bean.ExpressionControlBean;
import com.csjbot.blackgaga.bean.OnUpdateAppEvent;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.feature.dance.DanceActivity;
import com.csjbot.blackgaga.feature.music.MusicActivity;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.setting.NaviSettingActivity;
import com.csjbot.blackgaga.feature.news.NewsActivity;
import com.csjbot.blackgaga.feature.product.shopcart.ShoppingCartActivity;
import com.csjbot.blackgaga.feature.settings.SettingsActivity;
import com.csjbot.blackgaga.feature.settings.network.SettingsNetworkActivity;
import com.csjbot.blackgaga.feature.story.StoryActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.tts.ISpeak;
import com.csjbot.blackgaga.network.NetworkConstants;
import com.csjbot.blackgaga.p2pcamera.CustomerService;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.speechrule.ChatControl;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.EnglishChatHandle;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.NumberUtils;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.ShopcartUtil;
import com.csjbot.blackgaga.util.VolumeUtil;
import com.csjbot.blackgaga.widget.ChatView;
import com.csjbot.blackgaga.widget.LanguageWindow;
import com.csjbot.blackgaga.widget.NewRetailDialog;
import com.csjbot.blackgaga.widget.TitleView;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.REQConstants;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.coshandler.listener.OnSpeechErrorListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

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

    protected CustomerService mCustomerService = null;

    protected volatile boolean isCustomerService;

    protected Handler mainHandler = new Handler(Looper.getMainLooper());

    protected volatile boolean isChatting = false;

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
    }

    protected void beforeSetContentView() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        addSpeechListener();
        if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
            addSpeechErrorListener();
            addWakeupListener();
        }
        isResume = true;
        refreshLogo();

        if (isOpenChat()) {
            if (getChatView() == null) {
                return;
            }
            if (Constants.isOpenChatView) {
                getChatView().setVisibility(View.VISIBLE);
            } else {
                getChatView().setVisibility(View.GONE);
            }
        }
    }

    private volatile long preDateTime = 0;

    private void addSpeechErrorListener() {
        RobotManager.getInstance().addListener((OnSpeechErrorListener) () -> {
            if (preDateTime > 0) {
                if (System.currentTimeMillis() > (preDateTime + 1000 * 60 * 10)) {
                    runOnUiThread(() -> prattle("网络正在开小差,请稍后"));
                    preDateTime = System.currentTimeMillis();
                }
            } else {
                runOnUiThread(() -> prattle("网络正在开小差,请稍后"));
                preDateTime = System.currentTimeMillis();
            }

        });
    }

    private void addWakeupListener() {
        RobotManager.getInstance().addListener((OnWakeupListener) angle -> {
            mainHandler.postDelayed(() -> prattle("我有什么可以帮您？"), 2000);
        });
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
        return (TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? verticalLayoutId
                : defaultLayoutId;
    }

    /**
     * 是否是大屏
     *
     * @return
     */
    protected boolean isPlus() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS));
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

    protected NewRetailDialog getDialog() {
        return mDialog;
    }

    void initWelcomeSpeakText() {
        CharSequence text = "";
        switch (Constants.Language.CURRENT_LANGUAGE) {
            case Constants.Language.CHINESE:
                text = initChineseSpeakText();
                break;
            case Constants.Language.ENGELISH_US:
            case Constants.Language.ENGELISH_UK:
            case Constants.Language.ENGELISH_AUSTRALIA:
            case Constants.Language.ENGELISH_INDIA:
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

    private String cuteAnswers[] = new String[]{
            "Sorry, I can't get directions there.",
            "What, again?",
            "That's what I figured.",
            "I am racking my brains~",
            "Sorry, I am still a baby learning something. That is out of capacity.",
    };

    private Runnable timeoutMsg = () -> setRobotChatMsg("请不要走开，爱丽丝还在思考中");


    /**
     * 添加语音监听事件
     */
    private void addSpeechListener() {
        RobotManager.getInstance().addListener((json, type) -> {
            switch (type) {
                case Robot.SPEECH_ASR_ONLY:
                    try {
                        String text = new JSONObject(json).getString("text");

                        userText = text;
                        if (isInterruptSpeech()) {
                            isDiscard = false;
                            if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                mainHandler.postDelayed(timeoutMsg, 3000);
                            }
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
                                if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                    mainHandler.postDelayed(timeoutMsg, 3000);
                                }
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
                                    if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                                        mainHandler.postDelayed(timeoutMsg, 3000);
                                    }
                                } else {
                                    isDiscard = true;
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Robot.SPEECH_LAST_RESULT:

                    if (isDiscard) {
                        return;
                    }

                    if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                        mainHandler.removeCallbacks(timeoutMsg);
                    }

                    if (!Constants.Language.isChinese()) {
                        internationalSpeechMessage(json);
                        return;
                    }

                    dealIntent(getIntentType(json), json);

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

    protected void internationalSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (!Constants.Language.isJapanese()) {
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                    return;
                }

            }

            final String tempText = answerText;

            runOnUiThread(() -> {
                if (!mSpeak.isSpeaking()) {
                    speak(tempText);
                    setRobotChatMsg(tempText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void koreanSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                return;
            }

            final String tempText = answerText;

            runOnUiThread(() -> {
                if (!mSpeak.isSpeaking()) {
                    speak(tempText);
                    setRobotChatMsg(tempText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void japaneseSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                return;
            }

            final String tempText = answerText;

            runOnUiThread(() -> {
                if (!mSpeak.isSpeaking()) {
                    speak(tempText);
                    setRobotChatMsg(tempText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void englishSpeechMessage(String json) {
        JSONObject result = null;
        try {
            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");
            String answerText = "";
            try {
                answerText = result.getJSONObject("answer").getString("answer_text");
            } catch (Exception e1) {
                e1.printStackTrace();
                CsjlogProxy.getInstance().debug("english  not in message");
            }


            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            if (TextUtils.isEmpty(answerText) || p.matcher(answerText).find()) {
                answerText = cuteAnswers[new Random().nextInt(cuteAnswers.length - 1)];
            }

            final String tempText = answerText;

            runOnUiThread(() -> {
                if (!mSpeak.isSpeaking()) {
                    speak(tempText);
                    setRobotChatMsg(tempText);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String getIntentType(String json) {
        String empty = "";
        JSONObject result = null;

        try {

            result = new JSONObject(json).getJSONObject("result");
            String text = result.getString("text");

            String service = "";
            try {
                service = result.getJSONObject("data").getString("service");
            } catch (Exception ee) {
            }

            CsjlogProxy.getInstance().info("service:" + service);

            /**************音量控制(开始)*************/
            if (text.contains(getString(R.string.noise_down))
                    || text.contains("音量小一点")
                    || text.contains("小一点声音")
                    || text.contains("小点声音")
                    || text.contains("小点声")) {
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.VOLUME + "\",\"result\":{\"action\":\"small\"}}";
            } else if (text.contains(getString(R.string.noise_up))
                    || text.contains("音量大一点")
                    || text.contains("大一点声音")
                    || text.contains("大一点音量")
                    || text.contains("大点声")
                    || text.contains("大点声音")) {
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.VOLUME + "\",\"result\":{\"action\":\"large\"}}";

            }
            /**************音量控制(结束)*************/


            /**************表情控制(开始)*************/
            if (Constants.expressionControlBean == null) {
                getLog().info("expressionControl:Constants.expressionControlBean:null");
                String filePath = Constants.Path.EXPRESSION_CONFIG_PATH + Constants.Path.EXPRESSION_CONFIG_FILE_NAME;
                File file = new File(filePath);
                getLog().info("expressionControl:filePath:" + file.exists());
                if (file.exists()) {

                    runOnUiThread(() -> new Thread(() -> {
                        String expressionJson = FileUtil.readToStringUTF8(filePath);
                        Constants.expressionControlBean = new Gson().fromJson(expressionJson, ExpressionControlBean.class);
                    }).start());


                } else {
                    String expressionJson = "{\n" +
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
                        Constants.expressionControlBean = new Gson().fromJson(expressionJson, ExpressionControlBean.class);
                        File pathFile = new File(Constants.Path.EXPRESSION_CONFIG_PATH);
                        if (!pathFile.exists()) {
                            pathFile.mkdirs();
                        }
                        try {
                            file.createNewFile();
                            FileUtil.writeToFile(file, expressionJson);
                        } catch (IOException e) {
                            getLog().error("e:" + e.toString());
                        }
                    }).start();

                }
            } else {
                for (String angry : Constants.expressionControlBean.getAngry()) {
                    getLog().info("angry:" + angry);
                    if (text.contains(angry)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"angry\"}}";
                    }
                }
                for (String happy : Constants.expressionControlBean.getHappy()) {
                    getLog().info("happy:" + happy);
                    if (text.contains(happy)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"happy\"}}";
                    }
                }
                for (String normal : Constants.expressionControlBean.getNormal()) {
                    getLog().info("normal:" + normal);
                    if (text.contains(normal)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"normal\"}}";
                    }
                }
                for (String sad : Constants.expressionControlBean.getSad()) {
                    getLog().info("sad:" + sad);
                    if (text.contains(sad)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"sad\"}}";
                    }
                }
                for (String smile : Constants.expressionControlBean.getSmile()) {
                    getLog().info("smile:" + smile);
                    if (text.contains(smile)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"smile\"}}";
                    }
                }
                for (String surprise : Constants.expressionControlBean.getSurprise()) {
                    getLog().info(surprise + surprise);
                    if (text.contains(surprise)) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.EXPRESSION + "\",\"result\":{\"action\":\"surprise\"}}";
                    }
                }
            }
            /**************表情控制(结束)*************/


            /**************动作控制(开始)*************/
            if (isOpenActionControl()) {
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
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"forward\"}}";
                } else if (text.matches(backwardRegEx)) {
                    getLog().info("后退---------");
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"backward\"}}";
                } else if (text.contains("左转") || text.contains("往左转") || text.contains("向左转")) {
                    getLog().info("左转意图---------");
                    if (text.matches(leftRegEx)) {
                        getLog().info("左转---------");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":0}}";
                    } else if (text.matches(leftDegreeRegEx)) {
                        int degree = NumberUtils.getNumber(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("左转" + degree + "度---------1");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":" + degree + "}}";
                    } else if (text.matches(leftDegreeRegEx2)) {
                        int degree = NumberUtils.numberParser(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("左转" + degree + "度---------2");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnLeft\",\"degree\":" + degree + "}}";
                    }
                } else if (text.contains("右转") || text.contains("往右转") || text.contains("向右转")) {
                    getLog().info("右转意图---------");
                    if (text.matches(rightRegEx)) {
                        getLog().info("右转---------");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":0}}";
                    } else if (text.matches(rightDegreeRegEx)) {
                        int degree = NumberUtils.getNumber(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("右转" + degree + "度---------1");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":" + degree + "}}";
                    } else if (text.matches(rightDegreeRegEx2)) {
                        int degree = NumberUtils.numberParser(text);
                        if (degree > 360) {
                            degree = 360;
                        }
                        getLog().info("右转" + degree + "度---------2");
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.ACTION + "\",\"result\":{\"action\":\"turnRight\",\"degree\":" + degree + "}}";
                    }
                }
            }
            /**************动作控制(结束)*************/


            if (!isDisableEntertainment()) {
                if (text.contains(getString(R.string.dance)) || text.contains(getString(R.string.dance1))
                        || text.contains(getString(R.string.dance2)) || text.contains(getString(R.string.dance3))) {
                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.DANCE + "\",\"result\":{}}";
                }
            }

            if (service.equals(Intents.MUSIC)) {
                if (isDisableEntertainment()) {
                    return empty;
                }
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.MUSIC + "\",\"result\":{}}";
            } else if (service.equals(Intents.NEWS)) {
                if (isDisableEntertainment()) {
                    return empty;
                }
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.NEWS + "\",\"result\":{}}";
            } else if (service.equals(Intents.STORY)) {
                if (isDisableEntertainment()) {
                    return empty;
                }
                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.STORY + "\",\"result\":{}}";
            }

        } catch (JSONException e) {
            CsjlogProxy.getInstance().info("json解析出错");
        }

        return empty;
    }


    protected boolean dealIntent(String intentJson, String source) {
        boolean isNotEmptyIntent = true;
        getLog().warn("intentJson:" + intentJson);
        String intent = "";
        try {
            intent = new JSONObject(intentJson).getString("intent");
            getLog().warn("intent:" + intent);
        } catch (JSONException e) {
            getLog().error("intent json 解析失败:" + e.toString());
        }
        switch (intent) {
            case Intents.MUSIC:
                music(source);
                break;
            case Intents.STORY:
                story(source);
                break;
            case Intents.DANCE:
                dance(source);
                break;
            case Intents.NEWS:
                news(source);
                break;
            case Intents.VOLUME:
                volume(intentJson, source);
                break;
            case Intents.EXPRESSION:
                expression(intentJson, source);
                isNotEmptyIntent = false;
                break;
            case Intents.ACTION:
                action(intentJson, source);
                break;
            default:
                isNotEmptyIntent = false;
                break;
        }
        return isNotEmptyIntent;
    }

    protected void music(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void story(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void dance(String source) {
        if (!this.getClass().getSimpleName().equals(DanceActivity.class.getSimpleName())) {
            jumpActivity(DanceActivity.class);
            if (this.getClass().getSimpleName().equals(MusicActivity.class.getSimpleName())
                    || this.getClass().getSimpleName().equals(NewsActivity.class.getSimpleName())
                    || this.getClass().getSimpleName().equals(StoryActivity.class.getSimpleName())) {
                this.finish();
            }
        }
    }

    protected void news(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void chat(String source) {
        JSONObject result = null;
        try {
            result = new JSONObject(source).getJSONObject("result");
            String text = result.getString("text");

            int errorCode = result.getInt("error_code");

            // 未知问题
            if (errorCode == 10119) {
                if (this instanceof NaviActivity || this instanceof NaviSettingActivity) {
                    return;
                }
                String answer = Constants.UnknownProblemAnswer.getAnswer();
                getLog().info("未知问题答案:" + answer);
                runOnUiThread(() -> prattle(answer));
                return;
            }

            try {
                int state = result.getJSONObject("data").getInt("state");
                isChatting = (state == 0);
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
            String filterAnswerText = answerText;

            runOnUiThread(() -> onSpeechMessage(text, filterAnswerText));

        } catch (JSONException e) {

        }

    }

    protected void volume(String intentjson, String source) {
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            if (action.equals("small")) {
                // 调小音量
                ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                        .adjustStreamVolume(
                                AudioManager.STREAM_MUSIC
                                , AudioManager.ADJUST_LOWER
                                , AudioManager.FX_FOCUS_NAVIGATION_UP);
            } else if (action.equals("large")) {
                // 调大音量
                ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                        .adjustStreamVolume(
                                AudioManager.STREAM_MUSIC
                                , AudioManager.ADJUST_RAISE
                                , AudioManager.FX_FOCUS_NAVIGATION_UP);
            }
        } catch (JSONException e) {

        }
    }


    protected void expression(String intentjson, String source) {
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            return;
        }
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            switch (action) {
                case "happy":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.HAPPY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "normal":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.NORMAL
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "smile":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SMILE
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "sad":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SADNESS
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "angry":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.ANGRY
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                case "surprise":
                    RobotManager.getInstance()
                            .robot
                            .reqProxy
                            .setExpression(REQConstants.Expression.SURPRISED
                                    , REQConstants.Expression.NO
                                    , 2000);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {

        }
    }

    protected void action(String intentjson, String source) {
        int degree;
        try {
            JSONObject result = new JSONObject(intentjson).getJSONObject("result");
            String action = result.getString("action");
            switch (action) {
                case "forward":
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
                    break;
                case "backward":
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
                    break;
                case "turnLeft":
                    degree = result.getInt("degree");
                    if (degree != 0) {
                        RobotManager.getInstance().robot.reqProxy.moveAngle(degree);
                    } else {
                        ServerFactory.getChassisInstance().turnLeft();
                    }
                    break;
                case "turnRight":
                    degree = result.getInt("degree");
                    if (degree != 0) {
                        RobotManager.getInstance().robot.reqProxy.moveAngle(-degree);
                    } else {
                        ServerFactory.getChassisInstance().turnRight();
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    protected boolean onSpeechMessage(String text, String answerText) {

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


    /**
     * 是否开启动作控制命令
     *
     * @return
     */
    protected boolean isOpenActionControl() {
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

                new LanguageWindow(BaseFullScreenActivity.this).showAsDropDown(mTitleView.ivLanguage, 30, 10);
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
            mTitleView.setHomePageListener(BRouter::toHome);

            mTitleView.setBackListener(BaseFullScreenActivity.this::finish);

            mTitleView.setSettingsPageBackListener(BaseFullScreenActivity.this::finish);

            if (Constants.Scene.CurrentScene.equals(Constants.Scene.Fuzhuang)) {
                mTitleView.setLanguageText("");
                mTitleView.setSettingsText("");
                mTitleView.setTvHomePageText("");
                mTitleView.setTvBackText("");
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 60;
                mTitleView.getIvSettings().setLayoutParams(lp);


                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mTitleView.getTvHomePage().setLayoutParams(lp2);
                mTitleView.getTvBack().setLayoutParams(lp2);
                mTitleView.getTvLanguage().setLayoutParams(lp2);
                mTitleView.getTvSetBack().setLayoutParams(lp2);


                RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) mTitleView.getSettins().getLayoutParams();
                lp3.rightMargin = 100;
                mTitleView.getSettins().setLayoutParams(lp3);
            }

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
        mMode = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, Constants.Language.CHINESE);
        switch (mMode) {
            case Constants.Language.ENGELISH_US:
                mTitleView.setLanguageImage(R.drawable.local_language_en_us);
                break;
            case Constants.Language.ENGELISH_UK:
                mTitleView.setLanguageImage(R.drawable.local_language_en_uk);
                break;
            case Constants.Language.CHINESE:
                mTitleView.setLanguageImage(R.drawable.local_language_zh);
                break;
            case Constants.Language.JAPANESE:
                mTitleView.setLanguageImage(R.drawable.local_language_jp);
                break;
            case Constants.Language.KOREAN:

                break;
            case Constants.Language.FRANCH_FRANCE:
                mTitleView.setLanguageImage(R.drawable.local_language_fr);
                break;
            case Constants.Language.SPANISH_SPAIN:
                mTitleView.setLanguageImage(R.drawable.local_language_es);
                break;
            case Constants.Language.PORTUGUESE_PORTUGAL:
                mTitleView.setLanguageImage(R.drawable.local_language_pt);
                break;
            case Constants.Language.INDONESIA:
                mTitleView.setLanguageImage(R.drawable.local_language_id);
                break;
            case Constants.Language.RUSSIAN:
                mTitleView.setLanguageImage(R.drawable.local_language_ru);
                break;
            default:
                mTitleView.setLanguageImage(R.drawable.local_language_zh);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    private BroadcastReceiver updateLogoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            updateLogo();
            refreshLogo();
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


    public void hideVideo() {
        if (!Constants.ChangeLan) {
            sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_HIDE));
        }
        Constants.ChangeLan = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void isUpdateApp(OnUpdateAppEvent event) {

    }

}
