package com.csjbot.blackgaga.model.http.product.TransactionOrder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.csjbot.blackgaga.BaseApplication;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.model.http.bean.OrderBean;
import com.csjbot.blackgaga.util.BlackgagaLogger;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by 孙秀艳 on 2017/10/23.
 */

public class TransactionOrderProxy implements ITransactionOrder {

    private ITransactionOrder iTransactionOrder;

    public static ITransactionOrder newProxyInstance(){
        return new TransactionOrderProxy();
    }

    private TransactionOrderProxy(){
        iTransactionOrder = new TransactionOrderImpl();
    }

    @Override
    public void getProductDetailInfo(Observer<OrderBean> observer) {
        iTransactionOrder.getProductDetailInfo(observer);
    }

    public void getProductDetailInfo(ProductDetailListener listener) {
        //是否需要判断本地存有该数据 是否需要每次都请求数据
        getProductDetailInfo(new Observer<OrderBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull OrderBean productDetailBean) {
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public interface ProductDetailListener{
        void getProductDetail(OrderBean productDetailBean);
    }
}
