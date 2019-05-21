package com.csjbot.blackgaga.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.settings.settings_list.SettingsListActivity;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.router.BRouter;
import com.csjbot.blackgaga.router.BRouterKey;
import com.csjbot.blackgaga.router.BRouterPath;
import com.csjbot.blackgaga.util.CheckOutUtil;
import com.csjbot.blackgaga.util.SharePreferenceTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jingwc on 2017/10/20.
 */

@Route(path = BRouterPath.SETTING_CONFIRM)
public class SettingsActivity extends BaseModuleActivity implements SettingsContract.View {

    SettingsContract.Presenter mPresenter;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.tv_navi_setting)
    TextView tvNaviSetting;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @OnClick(R.id.btn_cancel)
    public void viewOnClick(View view) {
        finish();
    }

    private String activityName;
    private boolean guideAll;

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_settings, R.layout.vertical_activity_settings);
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
        getTitleView().setSettingsVisibility(View.GONE);
        activityName = getIntent().getStringExtra(Constants.JUMP_ACTIVITY_NAME);
        guideAll = getIntent().getBooleanExtra(Constants.GUIDE_ALL, false);

        if (!TextUtils.isEmpty(activityName)) {
            if ((Constants.Scene.CurrentScene.equals(Constants.Scene.XingZheng)
                    || Constants.Scene.CurrentScene.equals(Constants.Scene.CheGuanSuo))) {
                tvNaviSetting.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        }

//        mEtPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
        mEtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                login();
                return true;
            }
            return false;
        });

        CheckOutUtil.getInstance().verifyUseName(mEtPassword, this);

        if (!TextUtils.isEmpty(activityName)) {
            speak(R.string.input_pwd_first, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new SettingsPresenter();
        mPresenter.initView(this);

        getTitleView().setSettingsPageBackVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt_login)
    public void login() {
        SharePreferenceTools sharePreferenceTools = new SharePreferenceTools(this);
        if (sharePreferenceTools.getString("pwd_word") != null) {
            if (sharePreferenceTools.getString("pwd_word").equals(mEtPassword.getText().toString().trim())) {
                if (TextUtils.isEmpty(activityName)) {
                    jumpActivity(SettingsListActivity.class);
                } else {
                    BRouter.getInstance()
                            .build(activityName)
                            .withBoolean(BRouterKey.GUIDE_ALL, guideAll)
                            .navigation();
                }
                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
                mEtPassword.setText("");
            }
        } else {
            if (mEtPassword.getText().toString().trim().equals("csjbot")) {
                if (TextUtils.isEmpty(activityName)) {
                    jumpActivity(SettingsListActivity.class);
                } else {
                    BRouter.getInstance()
                            .build(activityName)
                            .withBoolean(BRouterKey.GUIDE_ALL, guideAll)
                            .navigation();
                }

                this.finish();
            } else {
                Toast.makeText(this, getString(R.string.passWord_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (!TextUtils.isEmpty(activityName)) {
//            sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_SHOW));
//        }
    }
}
