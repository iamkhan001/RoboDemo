package com.csjbot.blackgaga.feature.settings;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.blackgaga.model.tcp.proxy.SpeakProxy;
import com.csjbot.blackgaga.service.NuanceService;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.RichTextUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.blackgaga.util.TonePlayer;
import com.csjbot.blackgaga.util.VolumeUtil;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.global.CmdConstants;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.jaygoo.widget.RangeSeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by 孙秀艳 on 2017/10/20.
 * 系统设置  音量设置
 */

public class SettingsVolumeActivity extends BaseModuleActivity implements RangeSeekBar.OnRangeChangedListener {
    private int volume;
    private float volumeProcess;
    private AudioManager mAudioManager;
    private SettingsContentObserver mSettingsContentObserver;
    private int current;

    @BindView(R.id.volume_setting)
    RangeSeekBar volumeSetting;

    @BindView(R.id.tts_setting)
    RangeSeekBar tts_setting;

    @BindView(R.id.goToChooseVoiceSpeaker)
    View goToChooseVoiceSpeaker;

    @BindView(R.id.ll_chinese_language_type)
    LinearLayout ll_chinese_language_type;

    @BindView(R.id.rg_chinese_language_type)
    RadioGroup rg_chinese_language_type;


    @BindView(R.id.auto_speech_recognition_switch)
    CheckBox auto_speech_recognition_switch;

    @BindView(R.id.cb_wakeup)
    CheckBox cb_wakeup;

    @BindView(R.id.tv_internationalization)
    TextView tv_internationalization;

    @BindView(R.id.bt_internationalization)
    TextView bt_internationalization;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

        // 如果不是日语的小雪机器人，就可以选择
        if (!(Constants.Language.isJapanese() && BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW))) {
            goToChooseVoiceSpeaker.setVisibility(View.VISIBLE);
        }

        if(Constants.Language.isChinese() && !Constants.isOpenNuance){
            ll_chinese_language_type.setVisibility(View.VISIBLE);
            String language = SharedPreUtil.getString(SharedKey.CHINESE_LANGUAGE_TYPE,SharedKey.CHINESE_LANGUAGE_TYPE);
            if(!TextUtils.isEmpty(language)){
                if(language.equals("mandarin")){
                    rg_chinese_language_type.check(R.id.rb_mandarin);
                }else if(language.equals("cantonese")){
                    rg_chinese_language_type.check(R.id.rb_cantonese);
                }else if(language.equals("imz")){
                    rg_chinese_language_type.check(R.id.rb_imz);
                }
            }else{
                rg_chinese_language_type.check(R.id.rb_mandarin);
            }
        }

        boolean isChecked = SharedPreUtil.getBoolean(SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, false);
        auto_speech_recognition_switch.setChecked(isChecked);

        auto_speech_recognition_switch.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreUtil.putBoolean(SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, SharedKey.AUTO_SPEECH_RECOGNITION_SWITCH, b);
        });

        boolean wakeupIsChecked = SharedPreUtil.getBoolean(SharedKey.WAKEUP_STOP, SharedKey.WAKEUP_STOP, false);
        cb_wakeup.setChecked(wakeupIsChecked);

        cb_wakeup.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreUtil.putBoolean(SharedKey.WAKEUP_STOP, SharedKey.WAKEUP_STOP, b);
        });

        rg_chinese_language_type.setOnCheckedChangeListener((group, checkedId) -> {
            String accent = "";
            if(checkedId == R.id.rb_mandarin){
                accent = "mandarin";
            }else if(checkedId == R.id.rb_cantonese){
                accent = "cantonese";
            }else if(checkedId == R.id.rb_imz){
                accent = "imz";
            }
            if(!TextUtils.isEmpty(accent)){
                ServerFactory.getSpeechInstance().setLanguageAndAccent(CmdConstants.LanguageType.ZH_CN,accent);
                SharedPreUtil.putString(SharedKey.CHINESE_LANGUAGE_TYPE,SharedKey.CHINESE_LANGUAGE_TYPE,accent);
            }
        });


        int internationalization = SharedPreUtil.getInt(SharedKey.INTERNATIONALIZATION, SharedKey.INTERNATIONALIZATION, 0);
        if(internationalization == 0){
            Constants.isOpenNuance = false;
            SpannableStringBuilder ssb = new RichTextUtil().append(getString(R.string.multilingual_recognition))
                    .append("\t"+getString(R.string.inactivated), getResources().getColor(R.color.pastel_5))
                    .finish();
            tv_internationalization.setText(ssb);
            bt_internationalization.setVisibility(View.VISIBLE);
        }else if(internationalization == 1){
            Constants.isOpenNuance = true;
            SpannableStringBuilder ssb = new RichTextUtil().append(getString(R.string.multilingual_recognition))
                    .append("\t"+getString(R.string.activated), getResources().getColor(R.color.forestgreen))
                    .finish();
            tv_internationalization.setText(ssb);
            bt_internationalization.setVisibility(View.GONE);
        }

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        setupVolumn();
        volumeSetting.setOnRangeChangedListener(this);


        float ttsVolume = SharedPreUtil.getFloat(SharedKey.TTS_VOLUME,SharedKey.TTS_VOLUME,1.0f);
        getLog().info("ttsVolume:"+ttsVolume);
        getLog().info("ttsVolume:value:"+(ttsVolume * 100));
        tts_setting.setValue((ttsVolume * 100));

        tts_setting.setOnRangeChangedListener((view, min, max, isFromUser) -> {
            getLog().info("isFromUser:"+isFromUser+",min:"+min);
            if(!isFromUser){
                float volume;
                switch ((int)min){
                    case 0:
                        volume = 0.0f;
                        break;
                    case 10:
                        volume = 0.1f;
                        break;
                    case 20:
                        volume = 0.2f;
                        break;
                    case 30:
                        volume = 0.3f;
                        break;
                    case 40:
                        volume = 0.4f;
                        break;
                    case 50:
                        volume = 0.5f;
                        break;
                    case 60:
                        volume = 0.6f;
                        break;
                    case 70:
                        volume = 0.7f;
                        break;
                    case 80:
                        volume = 0.8f;
                        break;
                    case 90:
                        volume = 0.9f;
                        break;
                    case 100:
                        volume = 1.0f;
                        break;
                    default:
                        volume = 1.0f;
                            break;
                }

                getLog().info("volume:"+volume);
                SpeakProxy.getInstance().setVolume(volume);
                SharedPreUtil.putFloat(SharedKey.TTS_VOLUME,SharedKey.TTS_VOLUME,volume);
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))
                ? R.layout.vertical_activity_settings_volume : R.layout.activity_settings_volume;

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    private void setupVolumn() {
//        registerVolumeChangeReceiver();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setRankSeekBarProgress(current);
    }

    private void registerVolumeChangeReceiver() {
        mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mSettingsContentObserver);
    }

    private void unregisterVolumeChangeReceiver() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    public class SettingsContentObserver extends ContentObserver {
        Context context;

        public SettingsContentObserver(Context c, Handler handler) {
            super(handler);
            context = c;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            BlackgagaLogger.debug("change currentVolume" + currentVolume);
            setRankSeekBarProgress(currentVolume);
        }
    }

    @Override
    public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
        if (isFromUser) {
            if ((int) min == 0) {
                volume = (int) 0.0f;
            } else if ((int) min == 10) {
                volume = (int) 1.6f;
            } else if ((int) min == 20) {
                volume = (int) 3.2f;
            } else if ((int) min == 30) {
                volume = (int) 4.8f;
            } else if ((int) min == 40) {
                volume = (int) 6.4f;
            } else if ((int) min == 50) {
                volume = (int) 8.0f;
            } else if ((int) min == 60) {
                volume = (int) 9.6f;
            } else if ((int) min == 70) {
                volume = (int) 11.2f;
            } else if ((int) min == 80) {
                volume = (int) 12.8f;
            } else if ((int) min == 90) {
                volume = (int) 14.4f;
            } else if ((int) min == 100) {
                volume = 16;
            }
            //改变系统的音量
            BlackgagaLogger.debug("set volume" + "max" + max + "min" + min + "volume" + volume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            SharedPreUtil.putInt(SharedKey.VOICENAME, SharedKey.VOICEKEY, volume);
        } else {
            if (max == 100) {
                TonePlayer.playUseSoundPool(this, Constants.VOLUME_PATH, 3);
            }
        }
    }

    /**
     * 刻度转换
     */
    private void setRankSeekBarProgress(int currentVolume) {
        if (currentVolume == 0) {
            volumeProcess = 0;
        } else if (currentVolume == 1) {
            volumeProcess = 10;
        } else if (currentVolume == 2) {
            volumeProcess = 10;
        } else if (currentVolume == 3) {
            volumeProcess = 20;
        } else if (currentVolume == 4) {
            volumeProcess = 30;
        } else if (currentVolume == 5) {
            volumeProcess = 30;
        } else if (currentVolume == 6) {
            volumeProcess = 40;
        } else if (currentVolume == 7) {
            volumeProcess = 40;
        } else if (currentVolume == 8) {
            volumeProcess = 50;
        } else if (currentVolume == 9) {
            volumeProcess = 60;
        } else if (currentVolume == 10) {
            volumeProcess = 60;
        } else if (currentVolume == 11) {
            volumeProcess = 70;
        } else if (currentVolume == 12) {
            volumeProcess = 80;
        } else if (currentVolume == 13) {
            volumeProcess = 80;
        } else if (currentVolume == 14) {
            volumeProcess = 90;
        } else if (currentVolume == 15) {
            volumeProcess = 100;
        }
        volumeSetting.setValue(volumeProcess);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                int volume = VolumeUtil.addMediaVolume(this);
                setRankSeekBarProgress(volume);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                int cutVolume = VolumeUtil.cutMediaVolume(this);
                setRankSeekBarProgress(cutVolume);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
//        unregisterVolumeChangeReceiver();
        super.onDestroy();
    }

    public void goToChooseVoiceSpeaker(View view) {
        if (Constants.Language.isChinese()) {
            jumpActivity(ChooseChineseSpeakerActivity.class);
        } else {
            jumpActivity(ChooseOtherSpeakerActivity.class);
        }
    }

    public void goToChooseCustomerServiceVoice(View view) {
//        jumpActivity(SettingsChooseCustomerServiceVoiceActivity.class);
    }

    public void openMultilingualRecognition(View view){
        if(TextUtils.isEmpty(Robot.SN)){
            return;
        }
        com.csjbot.blackgaga.model.http.factory.ServerFactory.createApi().getInternationalStatus(Robot.SN, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String bodyJson = "";
                try {
                    bodyJson = responseBody.string();
                } catch (IOException e) {
                }
                CsjlogProxy.getInstance().info("getInternationalStatus:onNext");
                CsjlogProxy.getInstance().info("getInternationalStatus:bodyJson:"+bodyJson);
                if(!TextUtils.isEmpty(bodyJson)){
                    try {
                        JSONObject jsonObject = new JSONObject(bodyJson);
                        String status = jsonObject.getString("status");
                        if(status.equals("200")){
                            int international = jsonObject.getJSONObject("result").getInt("international");
                            SharedPreUtil.putInt(SharedKey.INTERNATIONALIZATION, SharedKey.INTERNATIONALIZATION, international);
                            if(international == 0){
                                Constants.isOpenNuance = false;

                            }else if(international == 1){
                                Constants.isOpenNuance = true;
                                SpannableStringBuilder ssb = new RichTextUtil().append(getString(R.string.multilingual_recognition))
                                        .append("\t"+getString(R.string.activated), getResources().getColor(R.color.forestgreen))
                                        .finish();
                                tv_internationalization.setText(ssb);
                                bt_internationalization.setVisibility(View.GONE);

                                ServerFactory.getSpeechInstance().stopIsr();
                                startService(new Intent(SettingsVolumeActivity.this, NuanceService.class));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, getString(R.string.activation_fails), Toast.LENGTH_SHORT).show();
                        CsjlogProxy.getInstance().error("e:"+e.toString());
                    }
                }else{
                    Toast.makeText(context, getString(R.string.activation_fails), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
