package com.csjbot.blackgaga.feature.product.order;

import com.csjbot.blackgaga.model.http.bean.OrderBean;
import com.csjbot.blackgaga.model.http.bean.PayQrCodeBean;
import com.csjbot.blackgaga.model.http.bean.PayResponse;
import com.csjbot.blackgaga.model.http.bean.PayResult;
import com.csjbot.blackgaga.model.http.factory.ServerFactory;
import com.csjbot.blackgaga.model.http.product.ProductProxy;
import com.csjbot.blackgaga.model.http.product.listeners.OrderCodeUrlListener;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.ShopcartUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by 孙秀艳 on 2017/10/25.
 */

public class OrderPresenter implements OrderContract.Presenter{
    private OrderContract.View view;
    private ProductProxy proxy;
    private Disposable searchDis;

    public OrderPresenter() {
        proxy = ServerFactory.createProduct();
    }

    @Override
    public OrderContract.View getView() {
        return view;
    }

    @Override
    public void initView(OrderContract.View view) {
        this.view = view;
//        getCodeUrl();
    }

    @Override
    public void releaseView() {
        if(view != null){
            view = null;
        }
    }

    @Override
    public void getCodeUrl() {
        try {
            view.showLoading();
            proxy.generateOrder(generaterOrderJson(), new OrderCodeUrlListener() {
                @Override
                public void getOrderCodeUrl(PayResponse payResponse) {
                    if (payResponse.getOrderStatus().equals(PayResponse.SUCCESS)) {
                        view.showCode(payResponse.getCodeUrl());
                        // 这里就开始查询支付结果
                        getPayResult(payResponse.getOrderId());
                    }
                }
                @Override
                public void onOrderCodeError() {
                    view.hideLoading();
                }
                @Override
                public void onOrderCodeComplete() {
                    view.hideLoading();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPayResult(String orderid) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        searchDis = d;
                    }
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        proxy.searchPayResult(orderid, new Observer<PayResult>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@NonNull PayResult payResult) {
                                if (payResult.getPayStatus().equals(PayResult.SUCCESS)) {
                                    BlackgagaLogger.debug("支付成功");
                                    view.paySuccess();
                                } else if (payResult.getPayStatus().equals(PayResult.FAIL)){
                                    BlackgagaLogger.debug("支付失败");
                                    view.payError(payResult.getRemark());
                                }
                                if (payResult.getPayStatus().equals(PayResult.SUCCESS) || payResult.getPayStatus().equals(PayResult.FAIL)
                                        || ShopcartUtil.getOrderIsBack()) {
                                    if (!searchDis.isDisposed()) {
                                        searchDis.dispose();
                                    }
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                BlackgagaLogger.error(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        BlackgagaLogger.error(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void cancelSearch() {

    }

    @Override
    public void pullPayQrCodeBean() {
        ServerFactory.createQrCode().getPayQrCode(ProductProxy.SN, new Observer<PayQrCodeBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PayQrCodeBean bean) {
                //获取到了
                view.getPayQrCodeBean(bean);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String generaterOrderJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        // 1.生成32位唯一id
        UUID uuid = UUID.randomUUID();
        String uidStr = uuid.toString();
        String str = uidStr.replace("-", "");
        jsonObject.put("id", str);
        // 2.设备端自行生成虚拟订单
        String orderPseudoNo =
                view.getStartTime() + "-" + view.getOrderCount();
        jsonObject.put("orderPseudoNo", orderPseudoNo);
        // 3.顾客确认下单时间，ISO 8601格式
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String orderTime = sdf.format(new Date());
        jsonObject.put("orderTime", orderTime);
        // 4.订单商品列表
        JSONArray jsonArray = new JSONArray();
        List<OrderBean> orderList = ShopcartUtil.getOrderList();
        // 遍历Map
        for (OrderBean orderBean : orderList) {
            JSONObject j = new JSONObject();
            j.put("itemId", orderBean.getProduct_id());
            j.put("itemQty",orderBean.getSell());
            jsonArray.put(j);
        }
        jsonObject.put("orderList", jsonArray);
        // 5.订单描述
        jsonObject.put("orderDesc", "周黑鸭-食品");
        // 6.PayMethod
        jsonObject.put("payMethod", "NATIVE");
        // 7.robotUid
        jsonObject.put("robotUid", "12345678");
        // 8.robotModel
        jsonObject.put("robotModel", "yingbin");
        // 9.venderCode
        jsonObject.put("venderCode", "ZHY");
        // 10.venderUser
        jsonObject.put("venderUser", "12345678901");
        return jsonObject.toString();
    }
}
