package com.csjbot.blackgaga.model.http.logo;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.base.BaseImpl;
import com.csjbot.blackgaga.model.http.bean.LogoBean;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jingwc on 2018/3/22.
 */

public class LogoServiceImpl extends BaseImpl {

    public void getLogo(String sn, Observer<LogoBean> observer) {
        scheduler(getRetrofit().getLogo(sn)).subscribe(observer);
    }

    public void getLogo(String sn) {
        if (TextUtils.isEmpty(sn)) {
            SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
            String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            return;
        }
        CsjlogProxy.getInstance().info("getLogo sn:" + sn);
        scheduler(getRetrofit().getLogo(sn)).subscribe(new Observer<LogoBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LogoBean logoBean) {
                CsjlogProxy.getInstance().info("getLogo onNext:");
                if (logoBean != null && logoBean.getResult() != null && logoBean.getResult().size() > 0) {
                    CsjlogProxy.getInstance().info("getLogo onNext:json:" + new Gson().toJson(logoBean));
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, logoBean.getResult().get(0).getLogotitle());
                    Glide.with(BaseApplication.getAppContext())
                            .asFile()
                            .load(Uri.decode(logoBean.getResult().get(0).getLogourl()))
                            .listener(new RequestListener<File>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo Failed");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                    CsjlogProxy.getInstance().info("后台获取logo ResourceReady");
                                    return false;
                                }
                            })
                            .into(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(File resource, Transition<? super File> transition) {
                                    File pathFile = new File(Constants.Path.LOGO_PATH);
                                    if (!pathFile.exists()) {
                                        pathFile.mkdirs();
                                    }
                                    Observable.just(FileUtil.copy(resource, new File(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME)))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(b -> {
                                                if (b) {
                                                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGOURL, Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载成功");
                                                } else {
                                                    CsjlogProxy.getInstance().info("后台获取logo 下载失败");
                                                }
                                            });
                                }
                            });
                } else {
                    SharedPreUtil.putString(SharedKey.LOGO, SharedKey.LOGONAME, "");
                    String path = Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().info("getLogo onError");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public String getLogoName() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGONAME);
    }

    public String getLogoUrl() {
        return SharedPreUtil.getString(SharedKey.LOGO, SharedKey.LOGOURL);
    }

    @Override
    public LogoApiService getRetrofit() {
        return getRetrofit(LogoApiService.class);
    }
}
