package com.csjbot.blackgaga.model.http.product;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.model.http.product.listeners.BaseBackstageListener;
import com.csjbot.blackgaga.util.FileUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/3.
 * @Package_name: BlackGaGa
 */

public class DownLoadResTools {
    /**
     * 下载scene图片资源
     *
     * @param file
     * @param listener
     */
    public static void downLoadSceneResByGildeApp(String url, File file, BaseBackstageListener listener) {
        Glide.with(BaseApplication.getAppContext()).asFile().load(url).listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                //下载失败 缩略图可以
                if (listener != null)
                    listener.loadAllSuccess();
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                Observable.just(FileUtil.copy(resource, file)).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(b -> {
                            if (listener != null) {
                                listener.loadAllSuccess();
                            }
                        }, e -> {
                            if (listener != null) {
                                listener.cacheToMore();
                            }
                        });
            }
        });
    }

    /**
     * 获取到所有sp子类下面的照片或者video
     *
     * @param robotSpListBean
     * @return
     */
    public static int getResQuantity(int SP_SIZE, RobotSpListBean robotSpListBean) {
        int size = robotSpListBean.getResult().getProduct().size();
        List<RobotSpListBean.ResultBean.ProductBean> beam = robotSpListBean.getResult().getProduct();
        SP_SIZE = SP_SIZE + size;
        for (int i = 0; i < size; i++) {
            SP_SIZE = SP_SIZE + beam.get(i).getViewUrl().size();
        }
        return SP_SIZE;
    }

    /**
     * 下载图片的名字
     *
     * @param url
     * @return
     */
    public static String getMenuImageName(String url) {
        if (!url.equals("") && url != null) {
            String[] ss = url.split("/");
            for (int i = 0; i < ss.length; i++) {
                if ((i + 1) == ss.length) {
                    return ss[i];
                }
            }
        }
        return "";
    }

    public static String getOneSpURL(RobotMenuListBean bean, String menuid) {
        String spUrl = "";
        if (bean != null) {
            RobotMenuListBean.ResultBean resultBean = (RobotMenuListBean.ResultBean) bean.getResult();
            List<RobotMenuListBean.ResultBean.MenulistBean> list = (List<RobotMenuListBean.ResultBean.MenulistBean>) resultBean.getMenulist();
            for (RobotMenuListBean.ResultBean.MenulistBean d : list) {
                if (menuid.equals(d.getMenuid())) {
                    spUrl = d.getMURL().toString();
                }
            }
        }
        return spUrl;
    }

    /**
     * 获取当前时间
     */
    public static String getData(){
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(new Date());
    }
}
