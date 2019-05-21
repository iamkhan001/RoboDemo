package com.csjbot.blackgaga.feature.vipcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.ai.VipCenterAI;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.ConfInfoUtil;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnFaceSaveListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by xiasuhuei321 on 2017/10/18.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */
public class FuZhuangVipCenterActivity extends BaseModuleActivity {

    private RobotManager mRobotManager;
    private boolean mPausePreview;
    private boolean mCorrectPhoto;
    private Drawable mPhoto;

    @BindView(R.id.iv_preview)
    ImageView iv_preview;

    @BindView(R.id.iv_preview_photo)
    ImageView iv_preview_photo;

    @BindView(R.id.ll_register)
    LinearLayout ll_register;

    @BindView(R.id.ll_register_success)
    LinearLayout ll_register_success;

    @BindView(R.id.ll_take_photo)
    LinearLayout ll_take_photo;

    @BindView(R.id.ll_complete_and_retake)
    LinearLayout ll_complete_and_retake;

    @BindView(R.id.bt_complete)
    Button bt_complete;

    @BindView(R.id.bt_retake)
    Button bt_retake;

    @BindView(R.id.bt_take_photo)
    Button bt_take_photo;

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;

    private Bitmap bmpPhoto;

    private volatile boolean isTakeIn;

    private Handler mHandler = null;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
//        return getCorrectLayoutId(R.layout.activity_fuzhuang_vip, R.layout.vertical_activity_fuzhuang_vip);
        return R.layout.vertical_activity_fuzhuang_vip;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        mHandler = new Handler();
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    private Runnable finishRb = FuZhuangVipCenterActivity.this::finish;

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void init() {
        super.init();
        setRobotChatMsg("注册之后,爱丽丝就将能认识您，可以更好地为您服务!");

        mRobotManager = RobotManager.getInstance();
        mRobotManager.addListener(new OnCameraListener() {
            @Override
            public void response(Bitmap reponse) {
                if (!mPausePreview) {
                    runOnUiThread(() -> iv_preview.setImageBitmap(bmpPhoto = reponse));
                }

            }
        });
        mRobotManager.addListener(new OnSnapshotoListener() {
            @Override
            public void response(String response) {
                try {
                    BlackgagaLogger.debug("response:" + response);
                    int errorCode = new JSONObject(response).getInt("error_code");
                    mCorrectPhoto = errorCode == 0;
                } catch (JSONException e) {
                    BlackgagaLogger.error(e.toString());
                }
            }
        });
        mRobotManager.cameraConnect(this);
    }

    @Override
    protected CharSequence initChineseSpeakText() {
        return "您可以点击屏幕上的按钮，爱丽丝就将给您自动拍照哟!";
    }

    @Override
    protected CharSequence initEnglishSpeakText() {
        return "您可以点击屏幕上的按钮，爱丽丝就将给您自动拍照哟!";
    }

    @Override
    protected CharSequence initJapanSpeakText() {
        return "您可以点击屏幕上的按钮，爱丽丝就将给您自动拍照哟!";
    }

    @OnClick(R.id.bt_take_photo)
    public void takePhoto() {
        if (isTakeIn) {
            return;
        }
        new Thread(() -> {
            isTakeIn = true;
            runOnUiThread(() -> {
                prattle("请把脸正对着摄像头，准备拍照");
            });
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.three));
                setRobotChatMsg(getString(R.string.three));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.two));
                setRobotChatMsg(getString(R.string.two));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.num_one));
                setRobotChatMsg(getString(R.string.num_one));
            });
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                mPausePreview = true;
                mPhoto = iv_preview.getDrawable();
                ll_complete_and_retake.setVisibility(View.VISIBLE);
                bt_take_photo.setVisibility(View.GONE);
                ServerFactory.getFaceInstance().snapshot();

                isTakeIn = false;
            });
        }).start();
    }

    @OnClick(R.id.bt_complete)
    public void complete() {
        if (!mCorrectPhoto) {
            String errorText = getString(R.string.retake_photo_speak_text);
            speak(errorText);
            setRobotChatMsg(errorText);
            return;
        }
        mCorrectPhoto = false;
        ll_take_photo.setVisibility(View.GONE);
        ll_register.setVisibility(View.VISIBLE);

        iv_preview_photo.setImageDrawable(mPhoto);
    }

    @OnClick(R.id.bt_retake)
    public void retake() {
        mPausePreview = false;
        ll_complete_and_retake.setVisibility(View.INVISIBLE);
        new Thread(() -> {
            runOnUiThread(() -> {
                prattle("请把脸正对着摄像头,准备拍照");
            });
            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.three));
                setRobotChatMsg(getString(R.string.three));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.two));
                setRobotChatMsg(getString(R.string.two));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.num_one));
                setRobotChatMsg(getString(R.string.num_one));
            });
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                mPausePreview = true;
                mPhoto = iv_preview.getDrawable();
                ll_complete_and_retake.setVisibility(View.VISIBLE);
                ServerFactory.getFaceInstance().snapshot();
            });

        }).start();
    }

    @OnClick(R.id.bt_ok)
    public void ok() {
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            String speakText = getString(R.string.input_name);
            speak(speakText);
            setRobotChatMsg(speakText);
            return;
        }

        if (!isUserName(name)) {
            String speakText = getString(R.string.name_format);
            speak(speakText);
            setRobotChatMsg(speakText);
            return;
        }

        mRobotManager.addListener(new OnFaceSaveListener() {
            @Override
            public void response(String response) {
                try {
                    BlackgagaLogger.debug("response:" + response);
                    int errorCode = new JSONObject(response).getInt("error_code");
                    BlackgagaLogger.debug("error_code:-------------------->" + errorCode);
                    if (errorCode == 0) {
                        runOnUiThread(() -> {
                            ll_register.setVisibility(View.GONE);
                            ll_register_success.setVisibility(View.VISIBLE);
                            String text = getString(R.string.register_success_speak_text);
                            speak(text);
                            setRobotChatMsg(text);
                            mHandler.postDelayed(finishRb, 3000);
                        });

                        /** 会员信息上传至服务器 */

                        //人脸唯一性标识符
                        String faceInfoId = new JSONObject(response).getString("person_id");

                        // Bitmap转Base64
                        String resource = null;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmpPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        resource = Base64.encodeToString(bytes, Base64.NO_WRAP);
                        // 释放bitmap
                        bmpPhoto.recycle();
                        bmpPhoto = null;

                        // 获取性别
                        int id = rg_gender.getCheckedRadioButtonId();
                        String sex = "";
                        if (id == R.id.rb_man) {
                            sex = "male";
                        } else if (id == R.id.rb_woman) {
                            sex = "female";
                        }

                        // 获取日期
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                        String date = sDateFormat.format(new Date());

                        getLog().info("vipinfo resource:" + resource);

                        String phone = etPhone.getText().toString().trim();

                        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                            phone = "";
                        }
                        if (TextUtils.isEmpty(faceInfoId)) {
                            faceInfoId = "";
                        }

                        JSONObject jo = new JSONObject();
                        jo.put("sn", ConfInfoUtil.getSN());
                        jo.put("name", name);
                        jo.put("regDate", date);
                        jo.put("sex", sex);
//                        jo.put("age", -1);
                        jo.put("photo", resource);
                        jo.put("company", "");
                        jo.put("telephone", phone);
                        jo.put("faceinfoid", faceInfoId);
                        jo.put("faceinfocode", "");

                        getLog().info("vipinfo json:" + jo.toString());

                        com.csjbot.blackgaga.model.http.factory.ServerFactory.createVip().uploadVipInfo(jo.toString(), new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull ResponseBody responseBody) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getLog().info("会员信息上传至服务失败" + e.toString());
                            }

                            @Override
                            public void onComplete() {
                                getLog().info("会员信息上传至服务成功");
                            }
                        });
                    } else if (errorCode == 40002) {
                        runOnUiThread(() -> {
                            String text = getString(R.string.cannot_repeat_register);
                            speak(text);
                            setRobotChatMsg(text);
                        });
                    } else if (errorCode == 40003) {
                        runOnUiThread(() -> {
                            String text = getString(R.string.name_is_error);
                            speak(text);
                            setRobotChatMsg(text);
                        });
                        return;
                    } else {
                        runOnUiThread(() -> {
                            String text = getString(R.string.register_faild_speak_text);
                            speak(text);
                            setRobotChatMsg(text);
                        });
                    }
                    if (errorCode != 0) {
                        runOnUiThread(() -> {
                            mPausePreview = false;
                            ll_register.setVisibility(View.GONE);
                            ll_take_photo.setVisibility(View.VISIBLE);
                            ll_complete_and_retake.setVisibility(View.GONE);
                            bt_take_photo.setVisibility(View.VISIBLE);
                            et_name.setText("");
                        });
                    }
                } catch (JSONException e) {
                    BlackgagaLogger.error(e.toString());
                }
            }
        });
        ServerFactory.getFaceInstance().faceRegSave(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRobotManager.cameraDisconnect(this);
        mHandler.removeCallbacks(finishRb);
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (text.contains(getString(R.string.retake_photo))) {
            retake();
        } else if (text.contains(getString(R.string.take_photo))) {
            takePhoto();
        } else if (text.contains(getString(R.string.complete)) || text.contains("拍好了")) {
            complete();
        } else if (text.contains(getString(R.string.sure))) {
            ok();
        } else {
            prattle(answerText);
        }
        return true;
    }


    /**
     * 手机号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public boolean isPhone(String str) {
        Pattern p = Pattern.compile("0?(13|14|15|17|18|19)[0-9]{9}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证用户名格式
     *
     * @param str
     * @return
     */
    private boolean isUserName(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{1,5}$");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return m.matches();
        } else {
            Pattern p1 = Pattern.compile("^[A-Za-z]{1,20}$");
            Matcher m1 = p1.matcher(str);
            return m1.matches();
        }
    }
}
