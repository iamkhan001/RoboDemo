package com.csjbot.blackgaga.feature.settings.pwd_setting;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.csjbot.blackgaga.BuildConfig;
import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.advertisement.plus.PlusVideoService;
import com.csjbot.blackgaga.base.BaseFullScreenActivity;
import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.test.BaseModuleActivity;
import com.csjbot.blackgaga.feature.settings.settings_list.SettingsListActivity;
import com.csjbot.blackgaga.util.CheckOutUtil;
import com.csjbot.blackgaga.widget.NewRetailDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public class PWDManagementActivity extends BaseModuleActivity implements PWDManagementContract.view {
    @BindView(R.id.new_pwd)
    EditText newPwd;
    @BindView(R.id.repeat_pwd)
    EditText repeatPwd;
    @BindView(R.id.show_error)
    TextView showError;
    @BindView(R.id.save_pwd)
    Button savePwd;
    @BindView(R.id.now_pwd)
    EditText nowPwd;

    private String errorMsg = "";


    private PWDManagementContract.presenter presenter;

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public int getLayoutId() {
        return (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) ? R.layout.vertical_activity_pwd_setting : R.layout.activity_pwd_setting;
    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingsVisibility(View.GONE);
        getTitleView().setBackVisibility(View.VISIBLE);
        presenter = new PWDManagementPresenter(this);
        presenter.initView(this);
        savePwd.setEnabled(false);
        newPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
        CheckOutUtil.getInstance().verifyUseName(newPwd, this);
        nowPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
        CheckOutUtil.getInstance().verifyUseName(nowPwd, this);
        repeatPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//最多只能输入10个字符
        CheckOutUtil.getInstance().verifyUseName(repeatPwd, this);
        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newPwd.getText() != null) {
                    presenter.judgeNewPwd(newPwd.getText().toString().trim(), nowPwd.getText().toString().trim());
                }
            }
        });

        nowPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nowPwd.getText() != null)
                    presenter.judgeNowPwd(nowPwd.getText().toString().trim());
            }
        });

        repeatPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (repeatPwd.getText() != null) {
                    presenter.judgeRepeatPwd(newPwd.getText().toString().trim(), repeatPwd.getText().toString().trim(), nowPwd.getText().toString().trim());
                }
            }
        });
    }

    @OnClick({R.id.save_pwd})
    public void onClick() {
        if (errorMsg.equals("")) {
            presenter.savePwd(repeatPwd.getText().toString().trim());
            nowPwd.setText(repeatPwd.getText().toString().trim());
            newPwd.setText("");
            repeatPwd.setText("");
            showError.setVisibility(View.GONE);
        } else {
            showHintDialog(errorMsg);
        }
    }

    @Override
    public void enterRepeatPwdInsufficientDigit() {
        showError.setText("");
        showError.setVisibility(View.VISIBLE);
        errorMsg = getString(R.string.pwd_wrong_format);
        showError.setText(R.string.pwd_wrong_format);
    }

//    @Override
//    public void enterNowPwdInsufficientDigit() {
//        showError.setText("");
//        showError.setVisibility(View.VISIBLE);
//        errorMsg = getString(R.string.pwd_enter_error);
//        showError.setText(R.string.pwd_enter_error);
//    }

    @Override
    public void enterNewPwdInsufficientDigit() {
        showError.setText("");
        showError.setVisibility(View.VISIBLE);
        errorMsg = getString(R.string.pwd_wrong_format);
        showError.setText(R.string.pwd_wrong_format);
    }

    @Override
    public void oldPwdError() {
        showError.setText("");
        newPwd.setEnabled(false);
        repeatPwd.setEnabled(false);
        showError.setVisibility(View.VISIBLE);
        errorMsg = getString(R.string.passWord_error);
        showError.setText(getString(R.string.passWord_error));
    }

    private void showHintDialog(String msg) {
        showNewRetailDialog(getString(R.string.pwn_administration), msg, new NewRetailDialog.OnDialogClickListener() {
            @Override
            public void yes() {
                jumpActivity(SettingsListActivity.class);
                dismissNewRetailDialog();
            }

            @Override
            public void no() {
                dismissNewRetailDialog();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(PlusVideoService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void newPwdError() {
        showError.setText("");
        showError.setVisibility(View.VISIBLE);
        savePwd.setEnabled(false);
        errorMsg = getString(R.string.pwd_the_two_input_is_different);
        showError.setText(R.string.pwd_the_two_input_is_different);
    }

    @Override
    public void closeShowText() {
        newPwd.setEnabled(true);
        repeatPwd.setEnabled(true);
        savePwd.setEnabled(false);
        showError.setVisibility(View.GONE);
        errorMsg = "";
    }

    @Override
    public void openButton() {
        savePwd.setEnabled(true);
    }

    @Override
    public void showOld(String pwd) {
        nowPwd.setText(pwd);
    }

    @Override
    public void saveSuccess() {
        showHintDialog(getString(R.string.pwd_updata_success));
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

}
