package com.csjbot.blackgaga.feature.settings.pwd_setting;

import android.content.Context;

import com.csjbot.blackgaga.util.SharePreferenceTools;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public class PWDManagementPresenter implements PWDManagementContract.presenter {
    private Context context;
    private PWDManagementContract.view view;
    private String pwd = "12345678";
    SharePreferenceTools sp;

    public PWDManagementPresenter(Context context) {
        this.context = context;
        sp = new SharePreferenceTools(context);
        if (sp.getString("pwd_word") != null) {
            pwd = sp.getString("pwd_word");
        } else pwd = "csjbot";
    }


    @Override
    public PWDManagementContract.view getView() {
        return view;
    }

    @Override
    public void initView(PWDManagementContract.view view) {
        this.view = view;
        view.showOld(pwd);
    }

    @Override
    public void releaseView() {

    }


    @Override
    public void savePwd(String pwd) {
        sp.putString("pwd_word", pwd);
        view.saveSuccess();
    }

    @Override
    public void judgeNowPwd(String trim) {
        if (!trim.equals(pwd)) {
            view.oldPwdError();
        } else {
            view.closeShowText();
        }
    }

    @Override
    public void judgeNewPwd(String trim, String old) {
        if (!(8 <= trim.length() && trim.length() <= 16)) {
            view.enterNewPwdInsufficientDigit();
        } else {
            if (!old.equals(pwd)) {
                view.oldPwdError();
            } else {
                view.closeShowText();
            }
        }
    }

    @Override
    public void judgeRepeatPwd(String newP, String reP, String old) {
        if (!(8 <= reP.length() && reP.length() <= 16)) {
            view.enterRepeatPwdInsufficientDigit();
        } else if (!newP.equals(reP)) {
            view.newPwdError();
        } else {
            if (!old.equals(pwd)) {
                view.oldPwdError();
            } else {
                view.closeShowText();
                view.openButton();
            }
        }
    }
}
