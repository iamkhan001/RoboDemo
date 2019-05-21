package com.csjbot.blackgaga.feature;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 导航声控设置
 */
public class SettingsSoundControlActivity extends BaseModuleActivity {

    @BindView(R.id.et_navi_pause_text1)
    EditText et_navi_pause_text1;

    @BindView(R.id.et_navi_pause_text2)
    EditText et_navi_pause_text2;

    @BindView(R.id.et_navi_pause_text3)
    EditText et_navi_pause_text3;

    @BindView(R.id.et_navi_resume_text1)
    EditText et_navi_resume_text1;

    @BindView(R.id.et_navi_resume_text2)
    EditText et_navi_resume_text2;

    @BindView(R.id.et_navi_resume_text3)
    EditText et_navi_resume_text3;

    @BindView(R.id.et_navi_end_text1)
    EditText et_navi_end_text1;

    @BindView(R.id.et_navi_end_text2)
    EditText et_navi_end_text2;

    @BindView(R.id.et_navi_end_text3)
    EditText et_navi_end_text3;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)                || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_navi_sound_control : R.layout.activity_settings_navi_sound_control;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setBackVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    public void init() {
        super.init();
        String pauseJson = SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_PAUSE);
        String resumeJson = SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_RESUME);
        String endJson = SharedPreUtil.getString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_END);

        if(!TextUtils.isEmpty(pauseJson)){
            ArrayList<String> pauseList = new Gson().fromJson(pauseJson,new TypeToken<ArrayList<String>>(){}.getType());
            if(pauseList != null && pauseList.size() > 0){
                for (int i = 0;i < pauseList.size();i++){
                    EditText editText = null;
                    if(i == 0){
                        editText = et_navi_pause_text1;
                    }else if(i == 1){
                        editText = et_navi_pause_text2;
                    }else if(i == 2){
                        editText = et_navi_pause_text3;
                    }
                    editText.setText(pauseList.get(i));
                }
            }
        }

        if(!TextUtils.isEmpty(resumeJson)){
            ArrayList<String> resumeList = new Gson().fromJson(pauseJson,new TypeToken<ArrayList<String>>(){}.getType());
            if(resumeList != null && resumeList.size() > 0){
                for (int i = 0;i < resumeList.size();i++){
                    EditText editText = null;
                    if(i == 0){
                        editText = et_navi_resume_text1;
                    }else if(i == 1){
                        editText = et_navi_resume_text2;
                    }else if(i == 2){
                        editText = et_navi_resume_text3;
                    }
                    editText.setText(resumeList.get(i));
                }
            }
        }

        if(!TextUtils.isEmpty(endJson)){
            ArrayList<String> endList = new Gson().fromJson(pauseJson,new TypeToken<ArrayList<String>>(){}.getType());
            if(endList != null && endList.size() > 0){
                for (int i = 0;i < endList.size();i++){
                    EditText editText = null;
                    if(i == 0){
                        editText = et_navi_end_text1;
                    }else if(i == 1){
                        editText = et_navi_end_text2;
                    }else if(i == 2){
                        editText = et_navi_end_text3;
                    }
                    editText.setText(endList.get(i));
                }
            }
        }
    }

    @OnClick(R.id.bt_save)
    public void save(){
       List<String> naviPauseList = new ArrayList<>();
       String pause1 = et_navi_pause_text1.getText().toString().trim();
       String pause2 = et_navi_pause_text2.getText().toString().trim();
       String pause3 = et_navi_pause_text3.getText().toString().trim();
       if(!TextUtils.isEmpty(pause1)){
           naviPauseList.add(pause1);
       }
       if(!TextUtils.isEmpty(pause2)){
           for (String str : naviPauseList){
               if(str.equals(pause2)){
                   speak(getString(R.string.keyword_repeat));
                   return;
               }
           }
           naviPauseList.add(pause2);
       }
       if(!TextUtils.isEmpty(pause3)){
           for (String str : naviPauseList){
               if(str.equals(pause3)){
                   speak(getString(R.string.keyword_repeat));
                   return;
               }
           }
           naviPauseList.add(pause3);
       }

        List<String> naviResumeList = new ArrayList<>();
        String resume1 = et_navi_resume_text1.getText().toString().trim();
        String resume2 = et_navi_resume_text2.getText().toString().trim();
        String resume3 = et_navi_resume_text3.getText().toString().trim();
        if(!TextUtils.isEmpty(resume1)){
            naviPauseList.add(resume1);
        }
        if(!TextUtils.isEmpty(resume2)){
            for (String str : naviPauseList){
                if(str.equals(resume2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviResumeList){
                if(str.equals(resume2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            naviPauseList.add(resume2);
        }
        if(!TextUtils.isEmpty(resume3)){
            for (String str : naviPauseList){
                if(str.equals(resume2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviResumeList){
                if(str.equals(resume3)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            naviPauseList.add(resume3);
        }


        List<String> naviEndList = new ArrayList<>();
        String end1 = et_navi_end_text1.getText().toString().trim();
        String end2 = et_navi_end_text2.getText().toString().trim();
        String end3 = et_navi_end_text3.getText().toString().trim();
        if(!TextUtils.isEmpty(end1)){
            naviPauseList.add(end1);
        }
        if(!TextUtils.isEmpty(end2)){
            for (String str : naviPauseList){
                if(str.equals(end2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviResumeList){
                if(str.equals(end2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviEndList){
                if(str.equals(end2)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            naviPauseList.add(end2);
        }
        if(!TextUtils.isEmpty(end3)){
            for (String str : naviPauseList){
                if(str.equals(end3)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviResumeList){
                if(str.equals(end3)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            for (String str : naviEndList){
                if(str.equals(end3)){
                    speak(getString(R.string.keyword_repeat));
                    return;
                }
            }
            naviPauseList.add(end3);
        }

        SharedPreUtil.putString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_PAUSE,naviPauseList);
        SharedPreUtil.putString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_RESUME,naviResumeList);
        SharedPreUtil.putString(SharedKey.NAVI_SOUND_CONTROL,SharedKey.NAVI_SOUND_CONTROL_END,naviEndList);

        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        speak(getString(R.string.save_success));
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        return true;
    }



    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }
}
