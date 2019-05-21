package com.csjbot.blackgaga.feature.settings.pwd_setting;

import com.csjbot.blackgaga.base.BasePresenter;
import com.csjbot.blackgaga.base.BaseView;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public interface PWDManagementContract {
    interface presenter extends BasePresenter<view> {
        void savePwd(String pwd);

        void judgeNowPwd(String trim);

        void judgeNewPwd(String trim, String old);

        void judgeRepeatPwd(String newP, String reP, String old);

    }

    interface view extends BaseView {
        /*输入重复密码不对*/
        void enterRepeatPwdInsufficientDigit();

        /*输入新密码格式不对*/
        void enterNewPwdInsufficientDigit();

        /*旧密码不对*/
        void oldPwdError();
        /*新密码不一致*/
        void newPwdError();

        /*关闭错误显示*/
        void closeShowText();

        /*允许点击按钮*/
        void openButton();

        /*显示老密码*/
        void showOld(String pwd);

        /*保存成功*/
        void saveSuccess();
    }
}
