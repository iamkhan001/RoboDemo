package com.csjbot.blackgaga.util;

import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.core.RobotManager;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.tcp.chassis.IChassis;
import com.csjbot.blackgaga.model.tcp.factory.ServerFactory;
import com.csjbot.coshandler.listener.OnSpeedSetListener;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiasuhuei321 on 2017/11/1.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class ClearUtil {

    private static IChassis chassis;

    public static void clearUserData(Observer ob) {
        Observable.just(1).map(integer -> {
            clear();
            return true;
        }).observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ob);
    }

    private static void clear() {
        String path = ProductProxy.PRODUCT_IMG_PATH;
        File f = new File(path);
        File[] files = f.listFiles();
        if(f.exists() && files != null && files.length > 0) {
            for (File file : files) {
                if (file.exists()) file.delete();
            }
        }

        // 重置密码为初始密码
        new SharePreferenceTools(BaseApplication.getAppContext())
                .putString("pwd_word", "csjbot");

        chassis = ServerFactory.getChassisInstance();
        chassis.setSpeed(0.5f);
        TimeoutUtil timeoutUtil = new TimeoutUtil();
        // 设置速度
        RobotManager.getInstance().addListener(new OnSpeedSetListener() {
            @Override
            public void setSpeedResult(boolean isSuccess) {
                timeoutUtil.cancel();
                if (isSuccess) {
                    ShellUtils.execCommand("mv /sdcard/blackgaga/nav_data /sdcard/nav_data", true, false);
                    ShellUtils.execCommand("pm clear com.csjbot.blackgaga && reboot", true, false);
                } else {
                    BlackgagaLogger.debug("重置速度失败");
                    ShellUtils.execCommand("pm clear com.csjbot.blackgaga && reboot", true, false);
                }
            }
        });

        timeoutUtil.start(5000, () -> {
            BlackgagaLogger.debug("重置速度超时");
            ShellUtils.execCommand("pm clear com.csjbot.blackgaga && reboot", true, false);
        });

    }
}
