package com.csjbot.blackgaga.feature.settings.synchronous_data_setting;

import android.content.Context;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.MenuListListener;
import com.csjbot.blackgaga.model.http.product.listeners.SpListListener;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/20.
 */

public class SynchronousDataPresenter implements SynchronousDataContract.presenter {
    private Context context;
    private SynchronousDataContract.view view;
    private ProductProxy productProxy;

    //产品列表基数，基于下载图片的数量和当前分配的seek
    private int MENUBASE = 0;

    //产品基数，基于下载图片的数量和当前分配的seek
    private int SPBASE = 0;

    //皮肤包下载基数，基于下载图片的数量和当前分配的seek
    private int SCENEBASE = 0;

    //当前下载的图片数量
    private int img = 0;

    private int fixed = 0;

    private int base = 100 / 2;


    public SynchronousDataPresenter(Context context) {
        this.context = context;
        productProxy = ServerFactory.createProduct();
        productProxy.ISUPDATA = true;
    }

    @Override
    public SynchronousDataContract.view getView() {
        return view;
    }

    @Override
    public void initView(SynchronousDataContract.view view) {
        this.view = view;
    }

    @Override
    public void releaseView() {

    }

    @Override
    public void synMenu() {
        img = 0;
        MENUBASE = 0;
        fixed = 0;
        productProxy.getRobotMenuList(new MenuListListener() {
            @Override
            public void loadSuccess(String name) {
                MENUBASE = MENUBASE + fixed;
                view.updatePr(MENUBASE);
            }

            @Override
            public void cacheToMore() {
                view.cacheError();
            }

            @Override
            public void loadFailed(String name) {
                MENUBASE = MENUBASE + fixed;
                view.updatePr(MENUBASE);
            }

            @Override
            public void loadAllSuccess() {
                view.menuSuccess(MENUBASE);
            }

            @Override
            public void loadError500() {
                view.spSuccess(100);
            }

            @Override
            public void getMenuList(RobotMenuListBean bean) {
            }

            @Override
            public void onMenuError(Throwable e) {
                view.menuFailed();
            }

            @Override
            public void onLocaleMenuList(RobotMenuListBean bean) {
            }

            @Override
            public void ImageSize(int num) {
                img = num;
                if (img != 0)
                    fixed = base / img;
                else fixed = 0;
            }
        });
    }

    @Override
    public void synSp() {
        SPBASE = 0;
        img = 0;
        fixed = 0;
        SPBASE = SPBASE + MENUBASE;
        productProxy.robotSpList("", context, new SpListListener() {
            @Override
            public void loadSuccess(String name) {
                SPBASE = SPBASE + fixed;
                view.updatePr(SPBASE);
            }

            @Override
            public void cacheToMore() {
                view.cacheError();
            }

            @Override
            public void loadFailed(String name) {
                SPBASE = SPBASE + fixed;
                view.updatePr(SPBASE);
            }

            @Override
            public void loadAllSuccess() {
                view.spSuccess(100);
            }

            @Override
            public void loadError500() {
                view.spSuccess(100);
            }

            @Override
            public void getSpList(RobotSpListBean bean) {
            }

            @Override
            public void onSpError(Throwable e) {
                view.spFailed();
            }

            @Override
            public void onLocaleSpList(RobotSpListBean bean) {

            }

            @Override
            public void ImageSize(int num) {
                //算出所有的数据
                img = num;
                if (img != 0)
                    fixed = base / img;
                else fixed = 0;
            }
        });
    }

    @Override
    public void stopUpdate() {
        productProxy.ISUPDATA = false;
    }
}
