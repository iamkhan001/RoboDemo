package com.csjbot.blackgaga.cart.pactivity.introduce_list;

import android.content.Context;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.MenuListListener;
import com.csjbot.blackgaga.util.ShopcartUtil;

/**
 * author : chenqi.
 * e_mail : 1650699704@163.com.
 * create_time : 2017/10/18.
 */

public class ProductPresenter implements ProductContract.presenter {
    private ProductContract.view view;
    /**
     * 上下文
     *
     * @return
     */
    private ProductProxy productProxy;

    private RobotMenuListBean listBean = null;
    private Context mc;
    public ProductPresenter(Context context) {
        this.mc = context;
        productProxy = ServerFactory.createProduct();
        ProductProxy.ISUPDATA = false;
    }


    @Override
    public ProductContract.view getView() {
        return view;
    }

    @Override
    public void initView(ProductContract.view view) {
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
    public void initListInfo() {
            productProxy.getRobotMenuList(new MenuListListener() {
                @Override
                public void loadSuccess(String name) {
                }

                @Override
                public void cacheToMore() {
                    Toast.makeText(mc,mc.getResources().getString(R.string.menu_down_failed),10).show();
                }

                @Override
                public void loadFailed(String name) {
                }


                @Override
                public void loadAllSuccess() {
                    if (listBean != null) {
                        view.updateInfor(listBean);
                    }
                }

                @Override
                public void loadError500() {
                    listBean = new RobotMenuListBean();
                    view.updateInfor(listBean);

                }

                @Override
                public void getMenuList(RobotMenuListBean bean) {
                    if (bean != null) {
                        listBean = bean;
                        view.updateInfor(listBean);
                    }
                }

                @Override
                public void onMenuError(Throwable e) {
                }

                @Override
                public void onLocaleMenuList(RobotMenuListBean bean) {
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
