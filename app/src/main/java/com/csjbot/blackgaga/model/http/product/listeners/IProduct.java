package com.csjbot.blackgaga.model.http.product.listeners;

import com.csjbot.blackgaga.cart.entity.RobotMenuListBean;
import com.csjbot.blackgaga.cart.entity.RobotSpListBean;
import com.csjbot.blackgaga.cart.pactivity.evaluate.AnswerResponse;
import com.csjbot.blackgaga.feature.clothing.bean.ClothListBean;
import com.csjbot.blackgaga.feature.clothing.bean.ClothTypeBean;
import com.csjbot.blackgaga.model.http.bean.AdInfo;
import com.csjbot.blackgaga.model.http.bean.ContentBean;
import com.csjbot.blackgaga.model.http.bean.ContentTypeBean;
import com.csjbot.blackgaga.model.http.bean.PayResponse;
import com.csjbot.blackgaga.model.http.bean.PayResult;
import com.csjbot.blackgaga.model.http.bean.SceneBean;

import io.reactivex.Observer;
import okhttp3.ResponseBody;

/**
 * Created by jingwc on 2017/9/18.
 */

public interface IProduct {
    /**
     * 获取广告信息
     *
     * @param observer 订阅者
     */
    void getAdInfo(Observer<AdInfo> observer);

    /**
     * 生成支付订单
     */
    void generateOrder(String json, Observer<PayResponse> observer);

    /**
     * 发去评价
     *
     * @param json
     * @param observer
     */
    void sendEvalution(String json, Observer<AnswerResponse> observer);

    /**
     * 查询订单支付情况
     *
     * @param orderid 订单id
     */
    void searchPayResult(String orderid, Observer<PayResult> observer);

    /**
     * 获取到导航列表，包括主页菜单的和产品菜单的
     *
     * @param observer
     */
    void getRobotMenuList(String sn, String language, Observer<RobotMenuListBean> observer);

    /**
     * 全路径获取到url 查询产品列表
     *
     * @param url
     * @param sn
     * @param observer
     */
    void dynamicGetRobotSpList(String url, String sn, Observer<RobotSpListBean> observer);

    /**
     * 指定尾部路径 查询产品列表
     *
     * @param url
     * @param observer
     */
    void fullDynamicGetRobotSpList(String url, Observer<RobotSpListBean> observer);

    /**
     * 下载图片
     *
     * @param url
     * @param observer
     */
    void getProductImage(String url, Observer<ResponseBody> observer);

    /**
     * 获取到场景
     *
     * @param observable
     */
    void getScene(String json, Observer<SceneBean> observable);

    /**
     * 获取到皮肤的压缩包.skin格式
     *
     * @param url
     * @param observer
     */
    void getSkin(String url, Observer<ResponseBody> observer);

    /**
     * 下载内容
     *
     * @param lan
     * @param sn
     * @param observer
     */
    void getContent(String lan, String sn, Observer<ContentBean> observer);

    /**
     * 获取下级菜单
     *
     * @param observer
     */
    void getContentType(String url, Observer<ContentTypeBean> observer);

    /**
     * 获取衣服种类
     *
     * @param sn       sn
     * @param observer
     */
    void getAllClothType(String sn, Observer<ClothTypeBean> observer);

    /**
     * 获取衣服列表
     *
     * @param sn          sn
     * @param secondLevel 衣服种类
     * @param season      季节
     * @param minPrice    最低价
     * @param maxPrice    最高价
     * @param observer
     */
    void getClothingList(String sn, String secondLevel, String season, double minPrice, double maxPrice, Observer<ClothListBean> observer);
}
