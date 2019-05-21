package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.coshandler.core.Robot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2018/7/10.
 */
public class SettingsSemanticsActivity extends BaseModuleActivity {


    @BindView(R.id.cb_chat)
    CheckBox cb_chat;

    @BindView(R.id.cb_semantics)
    CheckBox cb_semantics;

    @BindView(R.id.et_score)
    EditText et_score;

    @BindView(R.id.bt_unknown_problem_answer)
    Button bt_unknown_problem_answer;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)|| BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS))                ?  R.layout.vertical_activity_settings_semantics : R.layout.activity_settings_semantics;
    }

    @Override
    public void init() {
        super.init();
        if(Constants.Language.isChinese()){
            bt_unknown_problem_answer.setVisibility(View.VISIBLE);
        }else{
            bt_unknown_problem_answer.setVisibility(View.GONE);
        }
        getInfo();
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @OnClick(R.id.bt_ok)
    public void ok(){
        double score;
        try{
            score = Double.valueOf(et_score.getText().toString());
            if(score < 0.7d || score > 1.0d){
                Toast.makeText(context, R.string.input_success_number, Toast.LENGTH_SHORT).show();
                return;
            }
        }catch(Exception e){
            Toast.makeText(context, R.string.input_success_number, Toast.LENGTH_SHORT).show();
            return;
        }
        String json = "{\"sn\":\""+Robot.SN+"\",\"chat\":{\"on\":"+cb_chat.isChecked()+"},\"segment\":{\"on\":"+cb_semantics.isChecked()+",\"score\":"+score+"}}";
        ServerFactory.createApi().updateChatAndSegment(json, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String bodyJson = "";
                try {
                    bodyJson = responseBody.string();
                } catch (IOException e) {
                }
                getLog().info("updateChatAndSegment:onNext");
                getLog().info("updateChatAndSegment:bodyJson:"+bodyJson);
                if(!TextUtils.isEmpty(bodyJson)){
                    try {
                        JSONObject jsonObject = new JSONObject(bodyJson);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if(status.equals("200") && message.equals("ok")){
                            Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
                            speak(R.string.save_success);
                        }else{
                            Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
                            speak(R.string.save_fail);
                        }
                    } catch (JSONException e) {
                        getLog().error("e:"+e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(context, R.string.check_network_connection, Toast.LENGTH_SHORT).show();
                speak(R.string.check_network_connection);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnCheckedChanged(R.id.cb_semantics)
    public void cb_semantics(){
        if(cb_semantics.isChecked()){
            et_score.setFocusable(true);
            et_score.setFocusableInTouchMode(true);
        }else{

            et_score.setFocusable(false);
            et_score.setFocusableInTouchMode(false);
        }
    }

    private void getInfo(){
        ServerFactory.createApi().getChatAndSegmentInfo(Robot.SN, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String bodyJson = "";
                try {
                    bodyJson = responseBody.string();
                } catch (IOException e) {
                }
                getLog().info("getChatAndSegmentInfo:onNext");
                getLog().info("getChatAndSegmentInfo:bodyJson:"+bodyJson);
                if(!TextUtils.isEmpty(bodyJson)){
                    try {
                        JSONObject jsonObject = new JSONObject(bodyJson);
                        boolean isChat = jsonObject.getJSONObject("chat").getBoolean("on");
                        boolean isSemantics = jsonObject.getJSONObject("segment").getBoolean("on");
                        double score = jsonObject.getJSONObject("segment").getDouble("score");
                        setData(isChat,isSemantics,score);
                    } catch (JSONException e) {
                        getLog().error("e:"+e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void setData(boolean isChat,boolean isSemantics,double score){
        runOnUiThread(() -> {
            cb_chat.setChecked(isChat);
            cb_semantics.setChecked(isSemantics);
            et_score.setText(String.valueOf(score));
        });
    }



    @OnClick(R.id.bt_unknown_problem_answer)
    public void bt_unknown_problem_answer(){
        jumpActivity(SettingsUnknownProblemAnswerActivity.class);
    }
}
