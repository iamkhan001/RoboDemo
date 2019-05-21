package com.csjbot.blackgaga.model.http.workstream;

import com.csjbot.blackgaga.model.http.bean.LogoBean;
import com.csjbot.blackgaga.model.http.factory.RetrofitFactory;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiasuhuei321 on 2017/10/31.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class Logo {
    static LogoBean bean;
    public static boolean isFirst = true;

    public static void getLogo(String sn) {
        RetrofitFactory.create(WorkstreamAPIStore.class)
                .getLogo(sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LogoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LogoBean logoBean) {
                        if(logoBean == null || logoBean.getStatus() == null || logoBean.getResult() == null){
                            return;
                        }
                        bean = logoBean;
                        if (logoBean.getResult() != null && !logoBean.getStatus().toString().equals("") && logoBean.getResult().size() >= 1) {
                            SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGO, logoBean.getResult().get(0).getLogourl());
                            SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, logoBean.getResult().get(0).getLogotitle());
                        }
//                        BaseApplication.getAppContext().sendBroadcast(new Intent(Constants.UPDATE_LOGO));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void getLogo(String sn, Observer<LogoBean> observer) {
        RetrofitFactory.create(WorkstreamAPIStore.class)
                .getLogo(sn)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    public static LogoBean getLogo() {
        return bean;
    }

    public static String getDefaultUrl() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGO);
    }

    /**
     * 获取公司的名字
     * 如果没有用初值LOGONAMEMOREN
     * @return
     */
    public static String getLogoName() {
        String name = SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGONAME);
        if (name != null) {
            return name;
        } else
            return "";
    }

    public static void setLogoBean(LogoBean bean) {
        Logo.bean = bean;
    }
}
