package com.csjbot.blackgaga.cart.pactivity.introduce;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.SpListListener;
import com.csjbot.blackgaga.util.ShopcartUtil;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 */

public class ProductIntruductionPresenter implements ProductIntruductionContract.presenter {
    private ProductIntruductionContract.view view;

    private ProductProxy productProxy;

    /**
     * 上下文
     *
     * @param context
     */
    private Context context;

    RobotSpListBean spListBean = null;


    public ProductIntruductionPresenter(Context context) {
        this.context = context;
        productProxy = ServerFactory.createProduct();
        ProductProxy.ISUPDATA = false;
    }

    @Override
    public ProductIntruductionContract.view getView() {
        Log.e("chen initView", "init");
        return view;
    }

    @Override
    public void initView(ProductIntruductionContract.view view) {
        Log.e("chen initView", "init");
        this.view = view;
    }

    @Override
    public void releaseView() {
    }

    @Override
    public int getCount() {
        return ShopcartUtil.getShopcartNum();
    }


    @Override
    public boolean addCartCount(RobotSpListBean.ResultBean.ProductBean bean) {
        if (ShopcartUtil.isAddShopcart(bean)) {
            ShopcartUtil.addShopcartProduct(bean);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateSp(String menuid) {
            productProxy.robotSpList(menuid,context, new SpListListener() {
                @Override
                public void loadSuccess(String name) {
                }

                @Override
                public void cacheToMore() {
                    Toast.makeText(context, context.getResources().getString(R.string.menu_down_failed), 10).show();
                }

                @Override
                public void loadFailed(String name) {
                }

                @Override
                public void loadAllSuccess() {
                    if (spListBean != null)
                        view.updateInfor(spListBean);
                }

                @Override
                public void loadError500() {

                }

                @Override
                public void getSpList(RobotSpListBean bean) {
                    if (bean != null) {
                        spListBean = bean;
                        view.updateInfor(spListBean);
                    }
                }

                @Override
                public void onSpError(Throwable e) {
                }

                @Override
                public void onLocaleSpList(RobotSpListBean bean) {
                    if (bean != null) {
                        view.updateInfor(bean);
                    }
                }

                @Override
                public void ImageSize(int num) {
                }
            });
    }
}
